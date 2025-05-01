package com.mdhamed.weatherly.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.time.Duration;

/**
 * Test configuration that provides mock beans for testing
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    public Bucket testBucket() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(10, Duration.ofSeconds(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Bean
    @Primary
    public WebClient webClient() {
        return mock(WebClient.class);
    }

    @Bean
    @Primary
    public Counter weatherApiCallCounter() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        return Counter.builder("test.weather.api.calls").register(registry);
    }

    @Bean
    @Primary
    public Counter cacheHitCounter() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        return Counter.builder("test.cache.hits").register(registry);
    }

    @Bean
    @Primary
    public Counter cacheMissCounter() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        return Counter.builder("test.cache.misses").register(registry);
    }

    @Bean
    @Primary
    public Timer weatherApiCallTimer() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        return Timer.builder("test.weather.api.time").register(registry);
    }
}
