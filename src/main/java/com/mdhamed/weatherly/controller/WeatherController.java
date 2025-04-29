package com.mdhamed.weatherly.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdhamed.weatherly.model.WeatherResponse;
import com.mdhamed.weatherly.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for weather API endpoints
 */
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Weather", description = "Weather data API endpoints")
public class WeatherController {

    private final WeatherService weatherService;
    
    /**
     * Get weather data for a specific city
     * 
     * @param cityCode The city code or name to get weather for
     * @return ResponseEntity containing the weather data
     */
    @Operation(
        summary = "Get weather data for a city",
        description = "Retrieves current weather conditions and forecast for the specified city code or name"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Weather data retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = WeatherResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid city code"),
        @ApiResponse(responseCode = "500", description = "Server error or error from weather data provider")
    })
    @GetMapping("/{cityCode}")
    public ResponseEntity<WeatherResponse> getWeatherForCity(
            @Parameter(description = "City code or name (e.g., london, paris, new-york)") 
            @PathVariable String cityCode) {
        log.info("Received request for weather data for city: {}", cityCode);
        WeatherResponse weatherResponse = weatherService.getWeatherForCity(cityCode);
        return ResponseEntity.ok(weatherResponse);
    }
}
