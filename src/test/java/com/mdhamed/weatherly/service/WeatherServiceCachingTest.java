package com.mdhamed.weatherly.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.mdhamed.weatherly.config.TestConfig;
import com.mdhamed.weatherly.model.WeatherResponse;
import com.mdhamed.weatherly.service.impl.WeatherServiceImpl;

/**
 * Simple test for the WeatherService
 */
@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class WeatherServiceCachingTest {

    @Autowired
    private WeatherServiceImpl weatherService;
    
    @Test
    public void testWeatherServiceReturnsData() {
        // Set up the service with ReflectionTestUtils
        // ReflectionTestUtils.setField(weatherService, "cacheMissCounter", cacheMissCounter);
        // ReflectionTestUtils.setField(weatherService, "apiKey", "test-api-key");
        
        // Mock the service to return a mock response for getMockWeatherData
        // when(weatherService.getWeatherForCity(anyString())).thenCallRealMethod();
        
        // Act - Call the service
        WeatherResponse response = weatherService.getWeatherForCity("testCity");
        
        // Assert - Verify we got a response
        assertNotNull(response);
        assertNotNull(response.getLocation());
    }
}
