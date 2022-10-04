package de.c24.finacc.klt.web.service;

import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRatesAPIService {
    Flux<SymbolDto> getExchangeRateSymbols();

    Mono<RatesDto> getLatest(final String from , final String to);
}
