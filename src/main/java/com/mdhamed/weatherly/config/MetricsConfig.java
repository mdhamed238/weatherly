package com.mdhamed.weatherly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

/**
 * Configuration for custom metrics
 */
@Configuration
public class MetricsConfig {

    /**
     * Counter for tracking weather API calls
     */
    @Bean
    public Counter weatherApiCallCounter(MeterRegistry registry) {
        return Counter.builder("api.weather.calls")
                .description("Number of calls to the weather API")
                .register(registry);
    }
    
    /**
     * Timer for measuring weather API response time
     */
    @Bean
    public Timer weatherApiCallTimer(MeterRegistry registry) {
        return Timer.builder("api.weather.response.time")
                .description("Response time of weather API calls")
                .register(registry);
    }
    
    /**
     * Counter for tracking cache hits
     */
    @Bean
    public Counter cacheHitCounter(MeterRegistry registry) {
        return Counter.builder("cache.hits")
                .description("Number of cache hits")
                .register(registry);
    }
    
    /**
     * Counter for tracking cache misses
     */
    @Bean
    public Counter cacheMissCounter(MeterRegistry registry) {
        return Counter.builder("cache.misses")
                .description("Number of cache misses")
                .register(registry);
    }
}
