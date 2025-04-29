package com.mdhamed.weatherly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Configuration for OpenAPI documentation
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI weatherlyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Weatherly API")
                        .description("A Spring Boot Weather API that fetches weather data from Visual Crossing, " +
                                "implements caching, and provides a simple web UI for testing.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mohamed Hamed")
                                .url("https://github.com/mdhamed")
                                .email("contact@mdhamed.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
