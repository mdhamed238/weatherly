package com.mdhamed.weatherly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class WeatherlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherlyApplication.class, args);
	}

	@Bean
	public ApplicationListener<ApplicationStartedEvent> applicationStartedListener(Environment environment) {
		return event -> {
			String apiKey = environment.getProperty("WEATHER_API_KEY");
			String maskedKey = apiKey != null ? 
					(apiKey.length() > 4 ? 
							apiKey.substring(0, 4) + "..." + apiKey.substring(apiKey.length() - 4) : 
							"[too short to mask]") : 
					"null";
			
			log.info("Application started with WEATHER_API_KEY: {}", maskedKey);
			log.info("API key is {}null and {}empty and {}the dummy key", 
					apiKey == null ? "" : "not ", 
					(apiKey == null || apiKey.isEmpty()) ? "" : "not ", 
					"DUMMY_KEY_FOR_DEVELOPMENT".equals(apiKey) ? "" : "not ");
		};
	}
}
