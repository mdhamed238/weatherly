package com.mdhamed.weatherly.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Aspect for monitoring API performance
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PerformanceMonitoringAspect {

    private final MeterRegistry meterRegistry;

    /**
     * Monitors the performance of controller methods
     */
    @Around("execution(* com.mdhamed.weatherly.controller.*Controller.*(..))")
    public Object monitorControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String metricName = "api.performance." + className + "." + methodName;
        
        Timer timer = Timer.builder(metricName)
                .description("Performance of " + className + "." + methodName)
                .register(meterRegistry);
        
        long startTime = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.nanoTime() - startTime;
            timer.record(executionTime, TimeUnit.NANOSECONDS);
            log.debug("{}.{} execution time: {} ms", className, methodName, executionTime / 1_000_000.0);
        }
    }
    
    /**
     * Monitors the performance of service methods
     */
    @Around("execution(* com.mdhamed.weatherly.service.*Service.*(..))")
    public Object monitorServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String metricName = "service.performance." + className + "." + methodName;
        
        Timer timer = Timer.builder(metricName)
                .description("Performance of " + className + "." + methodName)
                .register(meterRegistry);
        
        long startTime = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.nanoTime() - startTime;
            timer.record(executionTime, TimeUnit.NANOSECONDS);
            log.debug("{}.{} execution time: {} ms", className, methodName, executionTime / 1_000_000.0);
        }
    }
}
