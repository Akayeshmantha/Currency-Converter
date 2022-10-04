//package de.c24.finacc.klt.rest;
//
//import de.c24.finacc.klt.currency_converter.dto.RatesDto;
//import de.c24.finacc.klt.currency_converter.dto.SymbolResponseDto;
//import de.c24.finacc.klt.currency_converter.service.ExchangeService;
//import de.c24.finacc.klt.currency_converter.service.impl.ExchangeServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.cache.Cache;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.math.BigDecimal;
//import java.util.LinkedHashMap;
//import java.util.Objects;
//
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class ExchangeServiceImplTest {
//    private ExchangeService exchangeService;
//
//    @Mock
//    private Cache cache;
//
//    @Mock
//    private WebClient webClient;
//
//    private SymbolResponseDto symbolResponseDto;
//    private RatesDto ratesDto;
//
//
////    @Before
////    public void createMocks() {
////        MockitoAnnotations.initMocks(this);
////    }
//
//    @BeforeEach
//    public void setup() {
//        this.exchangeService  = new ExchangeServiceImpl(webClient, cache);
//        final LinkedHashMap<String, String> symbols = new LinkedHashMap<>();
//        symbols.put("EUR", "Euro");
//        symbols.put("USD", "US Dollar");
//        symbols.put("LKR", "Sri Lanka Rupees");
//
//        symbolResponseDto = new SymbolResponseDto("success" , symbols);
//        ratesDto = RatesDto.builder()
//                .baseCurrency("EUR")
//                .toCurrency("LKR")
//                .baseConversion(new BigDecimal("354.90"))
//                .convertedValue(new BigDecimal("354.90"))
//                .build();
//    }
//
//    @Test
//    public void shouldGetValidCurrencySymbolList() {
//        when(cache.get("all-symbols", SymbolResponseDto.class)).thenReturn(symbolResponseDto);
//        String a = Objects.requireNonNull(exchangeService.getSymbols().block()).getStatus();
//
////        assertTrue(exchangeService.getSymbols().block().getStatus().equals("true"));
//    }
//
//}
