package de.c24.finacc.klt.web.facade;

import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeServiceFacade {
    Flux<SymbolDto> getExchangeRateSymbols();

    Mono<RatesDto> getValueRates(final String from, final  String to);
}
