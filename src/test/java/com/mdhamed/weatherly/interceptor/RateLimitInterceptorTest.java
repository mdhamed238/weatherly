package com.mdhamed.weatherly.interceptor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
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

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

@ExtendWith(MockitoExtension.class)
public class RateLimitInterceptorTest {

    @Mock
    private Bucket bucket;

    @InjectMocks
    private RateLimitInterceptor rateLimitInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testPreHandleWhenRateLimitNotExceeded() throws Exception {
        // Arrange
        ConsumptionProbe probe = mock(ConsumptionProbe.class);
        when(probe.isConsumed()).thenReturn(true);
        when(probe.getRemainingTokens()).thenReturn(10L);
        when(bucket.tryConsumeAndReturnRemaining(1)).thenReturn(probe);

        // Act
        boolean result = rateLimitInterceptor.preHandle(request, response, null);

        // Assert
        assertTrue(result);
        assertEquals("10", response.getHeader("X-Rate-Limit-Remaining"));
    }

    @Test
    public void testPreHandleWhenRateLimitExceeded() throws Exception {
        // Arrange
        ConsumptionProbe probe = mock(ConsumptionProbe.class);
        when(probe.isConsumed()).thenReturn(false);
        when(probe.getNanosToWaitForRefill()).thenReturn(1_000_000_000L); // 1 second
        when(bucket.tryConsumeAndReturnRemaining(1)).thenReturn(probe);

        // Act
        boolean result = rateLimitInterceptor.preHandle(request, response, null);

        // Assert
        assertFalse(result);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), response.getStatus());
        assertEquals("1", response.getHeader("X-Rate-Limit-Retry-After-Seconds"));
    }
}
