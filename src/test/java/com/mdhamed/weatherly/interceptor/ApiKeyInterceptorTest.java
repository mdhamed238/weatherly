package com.mdhamed.weatherly.interceptor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.mdhamed.weatherly.config.ApiKeyConfig;

@ExtendWith(MockitoExtension.class)
public class ApiKeyInterceptorTest {

    @Mock
    private ApiKeyConfig apiKeyConfig;

    @InjectMocks
    private ApiKeyInterceptor apiKeyInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private static final String HEADER_NAME = "X-API-Key";
    private static final String VALID_API_KEY = "valid-api-key";

    @BeforeEach
    public void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        
        when(apiKeyConfig.getHeaderName()).thenReturn(HEADER_NAME);
        when(apiKeyConfig.getApiKey()).thenReturn(VALID_API_KEY);
    }

    @Test
    public void testPreHandleWhenApiKeyAuthenticationDisabled() throws Exception {
        // Arrange
        when(apiKeyConfig.isEnabled()).thenReturn(false);

        // Act
        boolean result = apiKeyInterceptor.preHandle(request, response, null);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testPreHandleWithValidApiKey() throws Exception {
        // Arrange
        when(apiKeyConfig.isEnabled()).thenReturn(true);
        request.addHeader(HEADER_NAME, VALID_API_KEY);

        // Act
        boolean result = apiKeyInterceptor.preHandle(request, response, null);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testPreHandleWithInvalidApiKey() throws Exception {
        // Arrange
        when(apiKeyConfig.isEnabled()).thenReturn(true);
        request.addHeader(HEADER_NAME, "invalid-api-key");

        // Act
        boolean result = apiKeyInterceptor.preHandle(request, response, null);

        // Assert
        assertFalse(result);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void testPreHandleWithMissingApiKey() throws Exception {
        // Arrange
        when(apiKeyConfig.isEnabled()).thenReturn(true);

        // Act
        boolean result = apiKeyInterceptor.preHandle(request, response, null);

        // Assert
        assertFalse(result);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }
}
