package com.mdhamed.weatherly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;

import com.mdhamed.weatherly.model.WeatherResponse;

/**
 * Test class for WeatherService implementation
 */
@SpringBootTest
public class WeatherServiceTest {

    @MockBean
    private WebClient webClient;
    
    @Autowired
    private WeatherService weatherService;
    
    @Test
    public void testGetWeatherForCity_Success() {
        // Given a valid city code
        String cityCode = "london";
        
        // When requesting weather data
        WeatherResponse response = weatherService.getWeatherForCity(cityCode);
        
        // Then we should get a valid response
        assertNotNull(response, "Weather response should not be null");
        assertEquals(cityCode, response.getLocation(), "Location should match the requested city");
        assertNotNull(response.getCurrentConditions(), "Current conditions should not be null");
        assertNotNull(response.getForecast(), "Forecast should not be null");
    }
    
    @Test
    public void testGetWeatherForCity_InvalidApiKey() {
        // Given an invalid API key (mock data will be returned)
        String cityCode = "paris";
        
        // When requesting weather data
        WeatherResponse response = weatherService.getWeatherForCity(cityCode);
        
        // Then we should get mock data
        assertNotNull(response, "Weather response should not be null");
        assertEquals(cityCode, response.getLocation(), "Location should match the requested city");
        assertNotNull(response.getCurrentConditions(), "Current conditions should not be null");
        assertNotNull(response.getForecast(), "Forecast should not be null");
    }
    
    @Test
    public void testGetWeatherForCity_ApiError() {
        // Given a city code that would cause an API error
        String cityCode = "error";
        
        // When requesting weather data (mock data will be returned due to error)
        WeatherResponse response = weatherService.getWeatherForCity(cityCode);
        
        // Then we should get mock data as fallback
        assertNotNull(response, "Weather response should not be null");
        assertEquals(cityCode, response.getLocation(), "Location should match the requested city");
        assertNotNull(response.getCurrentConditions(), "Current conditions should not be null");
        assertNotNull(response.getForecast(), "Forecast should not be null");
    }
    
    @Test
    public void testGetWeatherForCity_CacheHit() {
        // Given a city code that was previously requested
        String cityCode = "berlin";
        
        // Request once to cache
        weatherService.getWeatherForCity(cityCode);
        
        // When requesting weather data again
        WeatherResponse response = weatherService.getWeatherForCity(cityCode);
        
        // Then we should get a valid response from cache
        assertNotNull(response, "Weather response should not be null");
        assertEquals(cityCode, response.getLocation(), "Location should match the requested city");
        assertNotNull(response.getCurrentConditions(), "Current conditions should not be null");
        assertNotNull(response.getForecast(), "Forecast should not be null");
    }
}
