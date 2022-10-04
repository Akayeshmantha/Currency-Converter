package de.c24.finacc.klt.rest;

import de.c24.finacc.klt.currency_converter.controller.ExchangeController;
import de.c24.finacc.klt.currency_converter.dto.RatesDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
import de.c24.finacc.klt.currency_converter.dto.SymbolResponseDto;
import de.c24.finacc.klt.currency_converter.service.ExchangeService;
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
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ExchangeController.class)
public class ExchangeControllerTest {

    @MockBean
    private ExchangeService exchangeService;

    @Autowired
    private WebTestClient webClient;


    private SymbolResponseDto symbolResponseDto;
    private RatesDto ratesDto;


    @BeforeEach
    public void setUp() {
        final LinkedHashMap<String, String> symbols = new LinkedHashMap<>();
        symbols.put("EUR", "Euro");
        symbols.put("USD", "US Dollar");
        symbols.put("LKR", "Sri Lanka Rupees");

        symbolResponseDto = new SymbolResponseDto("success", symbols);

        ratesDto = RatesDto.builder()
                .baseCurrency("EUR")
                .toCurrency("LKR")
                .baseConversion(new BigDecimal("354.90"))
                .convertedValue(new BigDecimal("354.90"))
                .build();
    }

    @Test
    public void shouldGetValidCurrencySymbolList() {
        when(exchangeService.getSymbols()).thenReturn(Mono.just(symbolResponseDto));
        webClient
                .get().uri("/api/v1/exchange-rate/symbols" )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<SymbolDto>() {
                }).hasSize(3);
    }

    @Test
    public void getValidCurrencySymbolList_ShouldThrowCurrencyNotFoundException() {
        when(exchangeService.getSymbols()).thenReturn(Mono.empty());
        webClient
                .get().uri("/api/v1/exchange-rate/symbols")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldGetLatestConversionRate() {
        when(exchangeService.getLatest("EUR", "LKR")).thenReturn(Mono.just(ratesDto));
        webClient
                .get().uri("/api/v1/exchange-rate/latest?base=EUR&symbols=LKR")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(RatesDto.class)
                .isEqualTo(ratesDto);
    }

    @Test
    public void getLatestConversionRate_ShouldThrowCurrencyNotFoundException() {
        when(exchangeService.getLatest("EUR", "LKR")).thenReturn(Mono.empty());
        webClient
                .get().uri("/api/v1/exchange-rate/latest?base=EUR&symbols=LKR")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getLatestConversionRateQithoutRequiredQueryParam_ShouldThrowBadRequest() {
        webClient
                .get().uri("/api/v1/exchange-rate/latest?base=EUR")
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}