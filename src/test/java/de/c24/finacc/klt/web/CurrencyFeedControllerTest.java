package de.c24.finacc.klt.web;

import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
import de.c24.finacc.klt.web.controller.CurrencyFeedController;
import de.c24.finacc.klt.web.facade.ExchangeServiceFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CurrencyFeedController.class)
public class CurrencyFeedControllerTest {
    @MockBean
    private ExchangeServiceFacade exchangeServiceFacade;
    @Autowired
    private WebTestClient webClient;

    private SymbolDto symbolDto;
    private RatesDto ratesDto;

    @BeforeEach
    public void setUp() {
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
    public void shouldGetValidCurrencySymbolsList() {
        when(exchangeServiceFacade.getExchangeRateSymbols()).thenReturn(Flux.just(symbolDto));
        webClient
                .get().uri("/feed/V1/symbols" )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<SymbolDto>() {
                }).hasSize(1).contains(symbolDto);
    }

    @Test
    public void getValidCurrencySymbolsList_ShouldThrowCurrencyNotFoundException() {
        when(exchangeServiceFacade.getExchangeRateSymbols()).thenReturn(Flux.empty());
        webClient
                .get().uri("/feed/V1/symbols" )
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldGetConversionValues() {

        when(exchangeServiceFacade.getValueRates("EUR", "LKR")).thenReturn(Mono.just(ratesDto));
        webClient
                .get().uri("/feed/V1/rates?from=EUR&to=LKR&toConvertValue=123.1" )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<RatesDto>() {
                }).isEqualTo(ratesDto);
    }

    @Test
    public void gtConversionValues_ShouldThrowCurrencyNotFoundException() {
        when(exchangeServiceFacade.getValueRates("EUR", "LKR")).thenReturn(Mono.empty());
        webClient
                .get().uri("/feed/V1/rates?from=EUR&to=LKR&toConvertValue=123.1" )
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void gtConversionValuesWhenRequireParamsNotAvaialble_ShouldThrowBadRequest() {
        when(exchangeServiceFacade.getValueRates("EUR", "LKR")).thenReturn(Mono.empty());
        webClient
                .get().uri("/feed/V1/rates?from=EUR&to=LKR" )
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
