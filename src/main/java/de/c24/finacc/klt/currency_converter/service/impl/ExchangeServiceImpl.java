package de.c24.finacc.klt.currency_converter.service.impl;

import de.c24.finacc.klt.currency_converter.dto.LatestResponseDto;
import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolResponseDto;
import de.c24.finacc.klt.currency_converter.exception.CurrencyBadRequestException;
import de.c24.finacc.klt.currency_converter.service.ExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.cache.CacheMono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {
    private final WebClient webClient;
    private final Cache cache;
    private static final String ALL_SYMBOL_KEY = "all-symbols";
    @Value("${exchangerates.symbols: symbols}")
    private String SYMBOL_ENDPOINT;
    @Value("${exchangerates.baseUrl:}")
    private String BASE_URL;
    @Value("${exchangerates.latest: latest?symbols=%s&base=%s}")
    private String LATEST_URL;


    public ExchangeServiceImpl(@Qualifier("exchangeratesClient") WebClient webClient, Cache cache) {
        this.webClient = webClient;
        this.cache = cache;
    }

    @Override
    public Mono<SymbolResponseDto> getSymbols() {
        log.debug("get the latest symbols...");
        return CacheMono.lookup(k -> Mono.justOrEmpty(cache.get(ALL_SYMBOL_KEY, SymbolResponseDto.class))
                .map(Signal::next), ALL_SYMBOL_KEY)
                .onCacheMissResume(this::fetchLatestSymbols)
                .andWriteWith((k, signal) -> Mono.fromRunnable(() ->
                        Optional.ofNullable(signal.get())
                                    .ifPresent(value -> cache.put(ALL_SYMBOL_KEY, value))))
                .doOnSuccess(latestResponseDto -> log.debug("fetched and serialized latest symbols"))
                .onErrorResume(throwable -> Mono.error(new RuntimeException()))
                .switchIfEmpty(Mono.error(new RuntimeException("Cannot deserialize symbols at this time")));
    }

    @Override
    public Mono<RatesDto> getLatest(final String from,final String to) {
        log.debug("get the latest rates...");
        final String key = from.concat("-").concat(to);
        return CacheMono.lookup(k -> Mono.justOrEmpty(cache.get(key, RatesDto.class))
                .map(Signal::next), key)
                .onCacheMissResume(() -> fetchLatestRates(from, to))
                .andWriteWith((k, signal) -> Mono.fromRunnable(() ->
                        Optional.ofNullable(signal.get())
                                .ifPresent(value -> cache.put(key, value))));
    }

    private Mono<RatesDto> fetchLatestRates(final String from,final String to){
        log.debug("Cannot fetch from the cache");
        return this.webClient
                .get()
                .uri(buildLatestUri(from, to))
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(CurrencyBadRequestException::new))
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<LatestResponseDto>() {
                })
                .doOnSuccess(latestResponseDto -> log.debug("fetched latest rates for"+ from + ","+ to))
                .onErrorResume(e -> Mono.error(new RuntimeException(e)))
                .switchIfEmpty(Mono.error(new RuntimeException("Cannot fetch rates at this time")))
                .flatMapMany(symbolResponseDto -> Flux.fromIterable(symbolResponseDto.getTo().entrySet()))
                .map(entry -> RatesDto.builder()
                        .baseCurrency(from)
                        .toCurrency(entry.getKey())
                        .baseConversion(
                                new BigDecimal(entry.getValue())
                        )
                        .build())
                .onErrorResume(e -> Mono.error(new RuntimeException(e)))
                .switchIfEmpty(Mono.error(new RuntimeException("Json deserialization exception thrown")))
                .next()
                .doOnSuccess(ratesDto -> log.debug("fetched and deserialized successfully for latest rates for"+ from + ","+ to));
    }

    private Mono<SymbolResponseDto> fetchLatestSymbols() {
        log.debug("Cannot fetch from the cache");
        return this.webClient
                .get()
                .uri(SYMBOL_ENDPOINT)
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(CurrencyBadRequestException::new))
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<SymbolResponseDto>() {
                })
                .doOnSuccess(latestResponseDto -> log.debug("fetched latest symbols"))
                .onErrorResume(throwable -> Mono.error(new RuntimeException()))
                .switchIfEmpty(Mono.error(new RuntimeException("Cannot fetch symbols at this time")));
    }

    public String buildLatestUri(final String from,final String to) {
        return String.format(BASE_URL.concat(LATEST_URL), to, from)
                .replace("\"", "");
    }
}
