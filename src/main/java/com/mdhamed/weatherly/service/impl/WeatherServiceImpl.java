package com.mdhamed.weatherly.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mdhamed.weatherly.model.CurrentConditions;
import com.mdhamed.weatherly.model.DailyForecast;
import com.mdhamed.weatherly.model.WeatherResponse;
import com.mdhamed.weatherly.service.WeatherService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
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
    private final Counter weatherApiCallCounter;
    private final Timer weatherApiCallTimer;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    @Override
    @Cacheable(value = "weatherData", key = "#cityCode", unless = "#result == null")
    public WeatherResponse getWeatherForCity(String cityCode) {
        log.debug("Cache miss for city: {}, fetching from API", cityCode);
        cacheMissCounter.increment();
        
        try {
            // If we have a valid API key, try to get real data
            if (apiKey != null && !apiKey.isEmpty() && !apiKey.equals("DUMMY_KEY_FOR_DEVELOPMENT")) {
                log.info("Using real API to fetch weather data for: {}", cityCode);
                weatherApiCallCounter.increment();
                
                return weatherApiCallTimer.record(() -> fetchFromApi(cityCode));
            } else {
                log.warn("No valid API key found, returning mock data for: {}", cityCode);
                return getMockWeatherData(cityCode);
            }
        } catch (Exception e) {
            log.error("Error fetching weather data from API: {}", e.getMessage(), e);
            // Return mock data in case of error
            return getMockWeatherData(cityCode);
        }
    }

    private WeatherResponse fetchFromApi(String cityCode) {
        log.debug("Making HTTP request to weather API for city: {}", cityCode);
        return webClient.get()
                .uri(apiUrl + "/{cityCode}?unitGroup=metric&key={apiKey}", cityCode, apiKey)
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .doOnSuccess(response -> log.debug("Successfully retrieved weather data for city: {}", cityCode))
                .doOnError(error -> log.error("Error retrieving weather data for city: {}", cityCode, error))
                .block();
    }

    private WeatherResponse getMockWeatherData(String cityCode) {
        log.debug("Generating mock weather data for city: {}", cityCode);
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
        log.debug("Generated mock weather data for city: {}", cityCode);
        return response;
    }
    
    /**
     * This method is called by AOP when there's a cache hit
     * It's used to increment the cache hit counter
     * 
     * @param cityCode The city code used as cache key
     */
    public void cacheHit(String cityCode) {
        log.debug("Cache hit for city: {}", cityCode);
        cacheHitCounter.increment();
    }
}
