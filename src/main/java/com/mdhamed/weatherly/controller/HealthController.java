package com.mdhamed.weatherly.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for health check endpoint
 */
@RestController
@RequestMapping("/api/health")
@Slf4j
@Tag(name = "Health", description = "Health check endpoints for monitoring")
public class HealthController {

    /**
     * Health check endpoint
     * 
     * @return ResponseEntity with health status
     */
    @Operation(
        summary = "Check API health",
        description = "Returns status information about the API including uptime and version"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Health check successful", 
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.info("Health check requested");
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());
        health.put("service", "Weatherly API");
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }
}
