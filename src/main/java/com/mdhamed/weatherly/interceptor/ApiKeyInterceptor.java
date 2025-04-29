package com.mdhamed.weatherly.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mdhamed.weatherly.config.ApiKeyConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Interceptor to validate API key for protected endpoints
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyInterceptor implements HandlerInterceptor {

    private final ApiKeyConfig apiKeyConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Skip API key validation if it's disabled
        if (!apiKeyConfig.isEnabled()) {
            return true;
        }
        
        // Get API key from request header
        String apiKey = request.getHeader(apiKeyConfig.getHeaderName());
        
        // Validate API key
        if (apiKey == null || apiKey.isEmpty() || !apiKey.equals(apiKeyConfig.getApiKey())) {
            log.warn("Invalid or missing API key for request: {}", request.getRequestURI());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid or missing API key");
            return false;
        }
        
        return true;
    }
}
