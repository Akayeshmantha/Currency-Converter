package de.c24.finacc.klt.web.controller;

import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
import de.c24.finacc.klt.currency_converter.exception.CurrencyNotFoundException;
import de.c24.finacc.klt.web.facade.ExchangeServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/feed/V1")
@RequiredArgsConstructor
public class CurrencyFeedController {
    private final ExchangeServiceFacade exchangeServiceFacade;

    /**
     * feed nonblocking endpoint for the event source
     * @return list of symbols
     */
    @GetMapping("/symbols")
    public Flux<SymbolDto> getValidCurrencySymbolsList() {
        return exchangeServiceFacade.getExchangeRateSymbols()
                .switchIfEmpty(Mono.error(new CurrencyNotFoundException("Cannot deserialize symbols at this time")));
    }

    @GetMapping("/rates")
    public Mono<RatesDto> getConversionValues(@RequestParam(name = "from") final String from,
                                              @RequestParam(name = "to") final String to,
                                              @RequestParam(name = "toConvertValue") final String toConvertValue) {
        final BigDecimal toConvertDecimal = new BigDecimal(toConvertValue);
        return exchangeServiceFacade.getValueRates(from, to).map(ratesDto -> {
            ratesDto.setConvertedValue(ratesDto.getBaseConversion().multiply(toConvertDecimal));
            return ratesDto;
        }).switchIfEmpty(Mono.error(new CurrencyNotFoundException("Cannot deserialize symbols at this time")));
    }
}
