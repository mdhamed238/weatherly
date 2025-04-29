package com.mdhamed.weatherly.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdhamed.weatherly.model.WeatherResponse;
import com.mdhamed.weatherly.service.WeatherService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for weather API endpoints
 */
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;
    
    /**
     * Get weather data for a specific city
     * 
     * @param cityCode The city code or name to get weather for
     * @return ResponseEntity containing the weather data
     */
    @GetMapping("/{cityCode}")
    public ResponseEntity<WeatherResponse> getWeatherForCity(@PathVariable String cityCode) {
        log.info("Received request for weather data for city: {}", cityCode);
        WeatherResponse weatherResponse = weatherService.getWeatherForCity(cityCode);
        return ResponseEntity.ok(weatherResponse);
    }
}
