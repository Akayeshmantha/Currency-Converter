package de.c24.finacc.klt.currency_converter.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Value("${exchangerates.secret}")
    private String EXCHANGE_RATE_KEY;


    @Bean(name = "exchangeratesClient")
    @Description("Web client builder for exchangerates apis")
    public WebClient reactiveWebClientExchangeRate(@Value("${exchangerates.baseUrl}") String serviceBaseUrl){
        return WebClient.builder()
                .defaultHeader("apikey", EXCHANGE_RATE_KEY)
                .baseUrl(serviceBaseUrl)
                .build();
    }

    @Bean(name = "mvcWebClient")
    public WebClient reactiveWebClient(@Value("${exchangerates.proxy.baseUrl}") String serviceBaseUrl){
        return WebClient.builder()
                .baseUrl(serviceBaseUrl)
                .build() ;
    }
}
