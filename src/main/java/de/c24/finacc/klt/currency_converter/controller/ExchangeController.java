package de.c24.finacc.klt.currency_converter.controller;

import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
import de.c24.finacc.klt.currency_converter.exception.CurrencyNotFoundException;
import de.c24.finacc.klt.currency_converter.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Exchange Rates Proxy repository
 * entry controller
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/exchange-rate/")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;


    /**
     * Get symbols non blocking endpoint
     * @return SymbolDto contains symbols of valid currencies
     */
    @GetMapping("/symbols")
    public Flux<SymbolDto> getValidCurrencySymbolsList() {
        log.debug("Get currency symbol entry");
        return exchangeService.getSymbols()
                .switchIfEmpty(Mono.error(new CurrencyNotFoundException("Cannot deserialize symbols at this time")))
                .flatMapMany(symbolResponseDto -> Flux.fromIterable(symbolResponseDto.getSymbols().entrySet()))
                .map(entry ->
                        SymbolDto.builder()
                                .key(entry.getKey())
                                .value(entry.getValue())
                                .keyValue(entry.getKey()
                                        .concat("-")
                                        .concat(entry.getValue()))
                                .build());
    }

    /**
     * Get latest conversion rate
     * for the provided valid target
     * and base currencies
     * @param from base currency
     * @param to target currency
     * @return latest rats update every 30 min time
     */
    @GetMapping("/latest")
    public Mono<RatesDto> getLatestConversionRate(@RequestParam(name = "base") final String from, @RequestParam(name = "symbols") final String to) {
        log.debug("Get latest conversion rate entry");
        return exchangeService.getLatest(from, to)
                .switchIfEmpty(Mono.error(new CurrencyNotFoundException("Cannot convert at this time")));
    }
}
