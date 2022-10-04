package de.c24.finacc.klt.web.service.impl;

import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
import de.c24.finacc.klt.currency_converter.exception.CurrencyBadRequestException;
import de.c24.finacc.klt.web.service.ExchangeRatesAPIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ExchangeRatesAPIServiceImpl implements ExchangeRatesAPIService {
    private final WebClient webClient;
    @Value("${exchangerates.proxy.symbols: symbols}")
    private String SYMBOL_ENDPOINT;
    @Value("${exchangerates.proxy.baseUrl:}")
    private String BASE_URL;
    @Value("${exchangerates.proxy.latest: latest}")
    private String LATEST_URL;

    public ExchangeRatesAPIServiceImpl(@Qualifier("mvcWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<SymbolDto> getExchangeRateSymbols() {
        return this.webClient.get()
                .uri(SYMBOL_ENDPOINT)
                .retrieve()
                .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(String.class).map(CurrencyBadRequestException::new))
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToFlux(new ParameterizedTypeReference<SymbolDto>() {
                })
                .doOnComplete(() -> log.debug("fetched latest rate symbols om client side"))
                .onErrorResume(e -> Mono.error(new RuntimeException(e)))
                .switchIfEmpty(Mono.error(new RuntimeException("Cannot fetch symbols at this time")));
    }

    @Override
    public Mono<RatesDto> getLatest(String from, String to) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(LATEST_URL)
                        .queryParam("base", from)
                        .queryParam("symbols", to)
                        .build()
                        )
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(CurrencyBadRequestException::new))
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<RatesDto>() {
                })
                .doOnSuccess(ratesDto -> log.debug("fetched and deserialized successfully for latest rates for"+ from + ","+ to))
                .onErrorResume(e -> Mono.error(new RuntimeException(e)))
                .switchIfEmpty(Mono.error(new RuntimeException("Cannot fetch rates")));
    }
}
