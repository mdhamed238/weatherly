package com.mdhamed.weatherly.service;

import com.mdhamed.weatherly.model.WeatherResponse;

/**
 * Service interface for weather operations
 */
public interface WeatherService {
    
    /**
     * Get weather data for a specific city
     * 
     * @param cityCode The city code or name to get weather for
     * @return WeatherResponse containing current conditions and forecast
     */
    WeatherResponse getWeatherForCity(String cityCode);
}
