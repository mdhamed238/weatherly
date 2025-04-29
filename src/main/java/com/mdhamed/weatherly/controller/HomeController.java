package com.mdhamed.weatherly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for redirecting the root URL to the web UI
 */
@Controller
@RequestMapping("/")
@Slf4j
@Tag(name = "Home", description = "Home page redirection")
public class HomeController {

    /**
     * Redirect root URL to the web UI
     * 
     * @return Redirect to the web UI
     */
    @Operation(
        summary = "Redirect to web UI",
        description = "Redirects the root URL to the web UI for interactive testing"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirect to the web UI")
    })
    @GetMapping
    public String home() {
        log.info("Redirecting to web UI");
        return "redirect:index.html";
    }
}
