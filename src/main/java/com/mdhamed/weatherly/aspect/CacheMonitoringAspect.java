package com.mdhamed.weatherly.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.mdhamed.weatherly.service.impl.WeatherServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspect for monitoring cache operations
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheMonitoringAspect {

    private final WeatherServiceImpl weatherService;

    /**
     * Intercepts cache hits for the getWeatherForCity method
     * This works because when there's a cache hit, the method is not actually executed
     * So we can detect cache hits by comparing the execution time
     */
    @Around("execution(* com.mdhamed.weatherly.service.impl.WeatherServiceImpl.getWeatherForCity(..)) && args(cityCode)")
    public Object monitorCachePerformance(ProceedingJoinPoint joinPoint, String cityCode) throws Throwable {
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long executionTime = System.nanoTime() - startTime;
        
        // If execution time is very short (less than 5ms), it's likely a cache hit
        if (executionTime < 5_000_000) {
            weatherService.cacheHit(cityCode);
        }
        
        return result;
    }
}
