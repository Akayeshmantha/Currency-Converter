package de.c24.finacc.klt.currency_converter.service;

import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolResponseDto;
import reactor.core.publisher.Mono;

public interface ExchangeService {
    /**
     * get current symbols
     * @return flux of current symbols
     */
    Mono<SymbolResponseDto> getSymbols();

    /**
     * get latest conversion
     * @param from base currency
     * @param to target currency
     * @return latest rates within 30 mins
     */
    Mono<RatesDto> getLatest(final String from , final String to);
}
