package com.mdhamed.weatherly.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mdhamed.weatherly.interceptor.ApiKeyInterceptor;
import com.mdhamed.weatherly.interceptor.RateLimitInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * Web configuration for registering interceptors
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RateLimitInterceptor rateLimitInterceptor;
    private final ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Apply API key authentication first
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/**");
        
        // Then apply rate limiting
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/**");
    }
}
