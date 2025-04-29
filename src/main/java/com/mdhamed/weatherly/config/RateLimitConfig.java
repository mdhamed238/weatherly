package com.mdhamed.weatherly.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;

/**
 * Configuration for API rate limiting using Bucket4j
 */
@Configuration
public class RateLimitConfig {

    @Value("${app.rate-limit.capacity:20}")
    private int capacity;

    @Value("${app.rate-limit.refill-tokens:10}")
    private int refillTokens;

    @Value("${app.rate-limit.refill-duration:1}")
    private int refillDuration;

    /**
     * Creates a token bucket for rate limiting
     * Default: 20 requests capacity with 10 tokens refilled every minute
     * 
     * @return Bucket instance for rate limiting
     */
    @Bean
    public Bucket tokenBucket() {
        Bandwidth limit = Bandwidth.classic(capacity, 
                Refill.intervally(refillTokens, Duration.ofMinutes(refillDuration)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
