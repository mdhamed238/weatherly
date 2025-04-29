package com.mdhamed.weatherly.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.mdhamed.weatherly.model.CurrentConditions;
import com.mdhamed.weatherly.model.DailyForecast;
import com.mdhamed.weatherly.model.WeatherResponse;
import com.mdhamed.weatherly.service.WeatherService;

@WebMvcTest(WeatherController.class)
@Import(WeatherControllerTest.TestConfig.class)
public class WeatherControllerTest {

    public static class TestConfig {
        @Bean
        public WeatherService weatherService() {
            return mock(WeatherService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherService weatherService;

    private WeatherResponse mockWeatherResponse;

    @BeforeEach
    public void setup() {
        // Create mock data for testing
        mockWeatherResponse = new WeatherResponse();
        mockWeatherResponse.setLocation("london");
        mockWeatherResponse.setResolvedAddress("London, UK");
        mockWeatherResponse.setDescription("Mock weather data for testing");

        CurrentConditions currentConditions = new CurrentConditions();
        currentConditions.setTemp(15.5);
        currentConditions.setFeelslike(14.0);
        currentConditions.setHumidity(70.0);
        currentConditions.setWindspeed(12.5);
        currentConditions.setConditions("Cloudy");
        currentConditions.setDatetime("2025-04-29T12:00:00");
        mockWeatherResponse.setCurrentConditions(currentConditions);

        List<DailyForecast> forecast = new ArrayList<>();
        DailyForecast day1 = new DailyForecast();
        day1.setDatetime("2025-04-29");
        day1.setTemp(15.5);
        day1.setConditions("Cloudy");
        forecast.add(day1);

        DailyForecast day2 = new DailyForecast();
        day2.setDatetime("2025-04-30");
        day2.setTemp(17.0);
        day2.setConditions("Partly Cloudy");
        forecast.add(day2);

        mockWeatherResponse.setForecast(forecast);
    }

    @Test
    public void testGetWeatherForCity_Success() throws Exception {
        // Given
        String cityCode = "london";
        when(weatherService.getWeatherForCity(cityCode)).thenReturn(mockWeatherResponse);

        // When & Then
        mockMvc.perform(get("/api/weather/{cityCode}", cityCode)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("london"))
                .andExpect(jsonPath("$.resolvedAddress").value("London, UK"))
                .andExpect(jsonPath("$.currentConditions.temp").value(15.5))
                .andExpect(jsonPath("$.forecast").isArray())
                .andExpect(jsonPath("$.forecast.length()").value(2));
    }

    @Test
    public void testGetWeatherForCity_EmptyCityCode() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/weather/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
