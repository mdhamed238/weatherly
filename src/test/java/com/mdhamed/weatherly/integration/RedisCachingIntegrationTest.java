package com.mdhamed.weatherly.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.mdhamed.weatherly.model.WeatherResponse;
import com.mdhamed.weatherly.service.WeatherService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;

/**
 * Integration test for Redis caching
 * This test uses Testcontainers to spin up a Redis container
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class RedisCachingIntegrationTest {

    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", redisContainer::getFirstMappedPort);
    }

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @MockBean
    private WebClient webClient;
    
    @MockBean
    private Counter weatherApiCallCounter;
    
    @MockBean
    private Timer weatherApiCallTimer;
    
    @MockBean
    private Counter cacheHitCounter;
    
    @MockBean
    private Counter cacheMissCounter;

    @Test
    public void testRedisCaching() {
        // Given a city code
        String cityCode = "test-city";
        
        // When we call the service twice
        WeatherResponse response1 = weatherService.getWeatherForCity(cityCode);
        
        // Then the response should be cached in Redis
        Cache cache = cacheManager.getCache("weatherData");
        assertNotNull(cache);
        
        // Verify the cache entry exists
        Cache.ValueWrapper cachedValue = cache.get(cityCode);
        assertNotNull(cachedValue);
        
        // Verify the cached value is the same as the response
        Object cachedObject = cachedValue.get();
        assertTrue(cachedObject instanceof WeatherResponse);
        WeatherResponse cachedResponse = (WeatherResponse) cachedObject;
        assertEquals(response1.getLocation(), cachedResponse.getLocation());
        
        // Verify Redis keys
        Boolean hasKey = redisTemplate.hasKey("weatherData::" + cityCode);
        assertTrue(hasKey);
    }
}
