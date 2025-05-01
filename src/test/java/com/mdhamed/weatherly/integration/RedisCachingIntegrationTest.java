package com.mdhamed.weatherly.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.mdhamed.weatherly.config.TestConfig;
import com.mdhamed.weatherly.model.WeatherResponse;
import com.mdhamed.weatherly.service.WeatherService;

/**
 * Integration test for Redis caching
 */
@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class RedisCachingIntegrationTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testRedisCaching() {
        // Given a city code
        String cityCode = "test-city";
        
        // When we call the service
        WeatherResponse response1 = weatherService.getWeatherForCity(cityCode);
        assertNotNull(response1, "Weather response should not be null");
        
        // Then the response should be cached
        Cache cache = cacheManager.getCache("weatherData");
        assertNotNull(cache, "Cache should not be null");
        
        // Verify the cache entry exists
        if (cache != null) {
            Cache.ValueWrapper cachedValue = cache.get(cityCode);
            assertNotNull(cachedValue, "Cached value should not be null");
            
            // Verify the cached value is the same as the response
            if (cachedValue != null) {
                Object cachedObject = cachedValue.get();
                assertNotNull(cachedObject, "Cached object should not be null");
                
                if (cachedObject != null) {
                    assertTrue(cachedObject instanceof WeatherResponse, "Cached object should be a WeatherResponse");
                    
                    WeatherResponse cachedResponse = (WeatherResponse) cachedObject;
                    assertEquals(response1.getLocation(), cachedResponse.getLocation(), "Location should match");
                }
            }
        }
    }
}
