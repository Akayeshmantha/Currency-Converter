package de.c24.finacc.klt.currency_converter.configuration;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CachingConfig {
    private static String RATES_CACHE_NAME = "rates";

    @Bean(name = "exchangeRatesAPICache")
    public Cache cacheRates() {
        return new GuavaCache(RATES_CACHE_NAME, CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build());
    }
}
