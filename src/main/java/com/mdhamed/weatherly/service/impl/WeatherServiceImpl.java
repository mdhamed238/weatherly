package com.mdhamed.weatherly.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mdhamed.weatherly.model.CurrentConditions;
import com.mdhamed.weatherly.model.DailyForecast;
import com.mdhamed.weatherly.model.WeatherResponse;
import com.mdhamed.weatherly.service.WeatherService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the WeatherService interface
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WebClient webClient;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;
    
    @Value("${app.cache.time-to-live:43200000}")
    private long cacheTtl;

    // Simple in-memory cache
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Override
    public WeatherResponse getWeatherForCity(String cityCode) {
        log.info("Fetching weather data for city: {}", cityCode);
        
        // Check cache first
        CacheEntry cachedEntry = cache.get(cityCode);
        if (cachedEntry != null && !cachedEntry.isExpired()) {
            log.info("Cache hit for city: {}", cityCode);
            return cachedEntry.getData();
        }
        
        log.info("Cache miss for city: {}, fetching from API", cityCode);
        
        try {
            // If we have a valid API key, try to get real data
            if (apiKey != null && !apiKey.isEmpty() && !apiKey.equals("dummy_api_key_for_development")) {
                WeatherResponse response = fetchFromApi(cityCode);
                // Cache the response
                cache.put(cityCode, new CacheEntry(response, cacheTtl));
                return response;
            } else {
                log.warn("No valid API key found, returning mock data");
                WeatherResponse mockData = getMockWeatherData(cityCode);
                // Cache the mock data
                cache.put(cityCode, new CacheEntry(mockData, cacheTtl));
                return mockData;
            }
        } catch (Exception e) {
            log.error("Error fetching weather data from API: {}", e.getMessage(), e);
            // Return mock data in case of error
            WeatherResponse mockData = getMockWeatherData(cityCode);
            return mockData;
        }
    }

    private WeatherResponse fetchFromApi(String cityCode) {
        return webClient.get()
                .uri(apiUrl + "/{cityCode}?unitGroup=metric&key={apiKey}", cityCode, apiKey)
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();
    }

    private WeatherResponse getMockWeatherData(String cityCode) {
        WeatherResponse response = new WeatherResponse();
        response.setLocation(cityCode);
        response.setResolvedAddress(cityCode + ", Mock Country");
        response.setDescription("Mock weather data for development");
        
        // Set current conditions
        CurrentConditions currentConditions = new CurrentConditions();
        currentConditions.setTemp(22.5);
        currentConditions.setFeelslike(23.0);
        currentConditions.setHumidity(65.0);
        currentConditions.setWindspeed(10.5);
        currentConditions.setConditions("Partly Cloudy");
        currentConditions.setDatetime(LocalDateTime.now().toString());
        response.setCurrentConditions(currentConditions);
        
        // Create a 7-day forecast
        List<DailyForecast> forecast = new ArrayList<>();
        LocalDate date = LocalDate.now();
        
        for (int i = 0; i < 7; i++) {
            DailyForecast daily = new DailyForecast();
            daily.setDatetime(date.plusDays(i).toString());
            daily.setTemp(20.0 + i);
            daily.setFeelslike(21.0 + i);
            daily.setHumidity(65.0 - i);
            daily.setWindspeed(10.0 + (i * 0.5));
            daily.setConditions(i % 2 == 0 ? "Sunny" : "Partly Cloudy");
            daily.setDescription("Mock forecast for day " + (i + 1));
            forecast.add(daily);
        }
        
        response.setForecast(forecast);
        return response;
    }
    
    // Inner class for cache entries with expiration
    private static class CacheEntry {
        private final WeatherResponse data;
        private final long expirationTime;
        
        public CacheEntry(WeatherResponse data, long ttlMillis) {
            this.data = data;
            this.expirationTime = System.currentTimeMillis() + ttlMillis;
        }
        
        public WeatherResponse getData() {
            return data;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
