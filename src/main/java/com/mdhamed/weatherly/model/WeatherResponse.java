package com.mdhamed.weatherly.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for weather response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String location;
    private String resolvedAddress;
    private String description;
    private CurrentConditions currentConditions;
    private List<DailyForecast> forecast;
}
