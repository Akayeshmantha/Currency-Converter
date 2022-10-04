//package de.c24.finacc.klt.web;
//
//import de.c24.finacc.klt.currency_converter.dto.RatesDto;
//import de.c24.finacc.klt.currency_converter.dto.SymbolDto;
//import de.c24.finacc.klt.web.service.ExchangeRatesAPIService;
//import de.c24.finacc.klt.web.service.impl.ExchangeRatesAPIServiceImpl;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.core.env.Environment;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.math.BigDecimal;
//
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class ExchangeRatesAPIServiceTest {
//    private ExchangeRatesAPIService exchangeRatesAPIService;
//
//    private MockWebServer mockWebServer;
//    private Environment env;
//    private RatesDto ratesDto;
//    private SymbolDto symbolDto;
//
//    @BeforeEach
//    public void setup() {
//        this.exchangeRatesAPIService = new ExchangeRatesAPIServiceImpl(WebClient.create());
//        mockWebServer = new MockWebServer();
//        env = mock(Environment.class);
//        when(this.env.getProperty(anyString())).thenReturn("string");
//
//        symbolDto = SymbolDto.builder()
//                .key("EUR")
//                .value("EURO")
//                .keyValue("EUR-EURO")
//                .build();
//
//        ratesDto = RatesDto.builder()
//                .baseCurrency("EUR")
//                .toCurrency("LKR")
//                .convertedValue(new BigDecimal("356.90"))
//                .baseConversion(new BigDecimal("356.90"))
//                .build();
//    }
//
//    @Test
//    public void getLatest() {
//        String json = "{\"baseCurrency\":\"EUR\",\"toCurrency\":\"LKR\",\"baseConversion\":\"356.90\",\"convertedValue\":\"356.90\"}";
//        this.mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(json));
//        var result = exchangeRatesAPIService.getLatest("EUR", "LKR");
//        assertNotNull(result.block());
//    }
//}
