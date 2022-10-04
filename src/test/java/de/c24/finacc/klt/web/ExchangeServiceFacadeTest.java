package de.c24.finacc.klt.web;

import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
import de.c24.finacc.klt.web.facade.ExchangeServiceFacade;
import de.c24.finacc.klt.web.facade.impl.ExchangeServiceFacadeImpl;
import de.c24.finacc.klt.web.service.ExchangeRatesAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceFacadeTest {
    private ExchangeServiceFacade exchangeServiceFacade;

    @Mock
    private ExchangeRatesAPIService exchangeRatesAPIService;

    private SymbolDto symbolDto;
    private RatesDto ratesDto;

    @BeforeEach
    public void setup() {
        this.exchangeServiceFacade = new ExchangeServiceFacadeImpl(exchangeRatesAPIService);
        final LinkedHashMap<String, String> symbols = new LinkedHashMap<>();
        symbols.put("EUR", "Euro");
        symbols.put("USD", "US Dollar");
        symbols.put("LKR", "Sri Lanka Rupees");

        symbolDto = SymbolDto.builder()
                .key("EUR")
                .value("EURO")
                .keyValue("EUR-EURO")
                .build();

        ratesDto = RatesDto.builder()
                .baseCurrency("EUR")
                .toCurrency("LKR")
                .convertedValue(new BigDecimal("356.90"))
                .baseConversion(new BigDecimal("356.90"))
                .build();
    }

    @Test
    public void shouldGetExchangeRateSymbols() {
        when(exchangeRatesAPIService.getExchangeRateSymbols()).thenReturn(Flux.just(symbolDto));

        final List<SymbolDto> symbolDtoList = exchangeServiceFacade.getExchangeRateSymbols().toStream().collect(Collectors.toList());

        assertFalse(symbolDtoList.isEmpty());
        assertTrue(symbolDtoList.contains(symbolDto));
    }

    @Test
    public void shouldGetValueRates() {
        when(exchangeRatesAPIService.getLatest("EUR", "LKR")).thenReturn(Mono.just(ratesDto));

        final RatesDto ratesDto = exchangeServiceFacade.getValueRates("EUR", "LKR").block();

        assertNotNull(ratesDto);
        assertTrue(ratesDto.getBaseCurrency().equals("EUR"));
        assertTrue(ratesDto.getToCurrency().equals("LKR"));
    }

    @Test
    public void getValueRatesOnRequiredValuesNull_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> exchangeServiceFacade.getValueRates("EUR", null).block());
    }
}
