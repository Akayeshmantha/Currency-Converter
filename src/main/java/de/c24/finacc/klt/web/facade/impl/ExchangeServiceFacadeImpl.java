package de.c24.finacc.klt.web.facade.impl;

import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
import de.c24.finacc.klt.web.facade.ExchangeServiceFacade;
import de.c24.finacc.klt.web.service.ExchangeRatesAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeServiceFacadeImpl implements ExchangeServiceFacade {
    private final ExchangeRatesAPIService exchangeRatesAPIService;

    @Override
    public Flux<SymbolDto> getExchangeRateSymbols() {
        log.debug("get the latest rates symbol facade...");
        return exchangeRatesAPIService.getExchangeRateSymbols();
    }

    @Override
    public Mono<RatesDto> getValueRates(final String from,final String to) {
        log.debug("get the latest valueRates facade...");
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        return exchangeRatesAPIService.getLatest(from, to);
    }


}
