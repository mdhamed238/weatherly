package com.mdhamed.weatherly.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * Configuration for API key authentication
 */
@Configuration
@Getter
public class ApiKeyConfig {

    @Value("${app.api-key.header-name:X-API-Key}")
    private String headerName;

    @Value("${app.api-key.value:${WEATHER_API_KEY:}}")
    private String apiKey;
    
    @Value("${app.api-key.enabled:false}")
    private boolean enabled;
}
