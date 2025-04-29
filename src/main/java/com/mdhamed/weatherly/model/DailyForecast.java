package com.mdhamed.weatherly.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for daily weather forecast
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyForecast implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String datetime;
    private Double temp;
    private Double feelslike;
    private Double humidity;
    private Double windspeed;
    private String conditions;
    private String description;
}
