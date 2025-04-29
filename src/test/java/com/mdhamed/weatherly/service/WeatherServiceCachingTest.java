package com.mdhamed.weatherly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.web.reactive.function.client.WebClient;

import com.mdhamed.weatherly.model.CurrentConditions;
import com.mdhamed.weatherly.model.WeatherResponse;
import com.mdhamed.weatherly.service.impl.WeatherServiceImpl;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class WeatherServiceCachingTest {

    @Autowired
    private CacheManager cacheManager;
    
    @Mock
    private WebClient webClient;
    
    @Mock
    private Counter weatherApiCallCounter;
    
    @Mock
    private Timer weatherApiCallTimer;
    
    @Mock
    private Counter cacheHitCounter;
    
    @Mock
    private Counter cacheMissCounter;
    
    @InjectMocks
    private WeatherServiceImpl weatherService;
    
    private WeatherResponse mockResponse;
    
    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setup() {
        // Create a mock response
        mockResponse = new WeatherResponse();
        mockResponse.setLocation("testCity");
        mockResponse.setResolvedAddress("Test City, Test Country");
        mockResponse.setDescription("Test weather data");
        
        CurrentConditions conditions = new CurrentConditions();
        conditions.setTemp(25.0);
        conditions.setFeelslike(26.0);
        conditions.setHumidity(60.0);
        conditions.setWindspeed(10.0);
        conditions.setConditions("Sunny");
        mockResponse.setCurrentConditions(conditions);
        
        // Setup WebClient mock chain
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        
        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString(), any(), any());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(Mono.just(mockResponse)).when(responseSpec).bodyToMono(eq(WeatherResponse.class));
        
        // Clear cache before each test
        cacheManager.getCache("weatherData").clear();
    }
    
    @Test
    public void testCachingMechanism() {
        // First call should miss the cache
        WeatherResponse response1 = weatherService.getWeatherForCity("testCity");
        
        // Verify response
        assertNotNull(response1);
        assertEquals("testCity", response1.getLocation());
        
        // Second call should hit the cache
        WeatherResponse response2 = weatherService.getWeatherForCity("testCity");
        
        // Verify response is the same
        assertNotNull(response2);
        assertEquals("testCity", response2.getLocation());
        
        // Verify cache miss counter was incremented once
        verify(cacheMissCounter, times(1)).increment();
    }
}
