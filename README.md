# Weatherly API

A Spring Boot Weather API that fetches weather data from Visual Crossing, implements caching, and provides a simple web UI for testing.

## Features

- RESTful API for fetching weather data by city code
- In-memory caching with configurable TTL
- Graceful error handling
- Health check endpoint
- Responsive web UI for testing
- Mock data support for development without API key

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- WebClient for API requests
- Bootstrap for the web UI

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/weatherly.git
   cd weatherly
   ```

2. Build the project:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

The application will start on http://localhost:8080

### Configuration

The application can be configured using environment variables or by modifying the `application.properties` file:

- `WEATHER_API_KEY`: Your Visual Crossing Weather API key (optional, will use mock data if not provided)
- `SERVER_PORT`: The port the application will run on (default: 8080)

## API Endpoints

### Get Weather Data

```
GET /api/weather/{cityCode}
```

Parameters:
- `cityCode`: The city name or code (e.g., london, paris, new-york)

Example Response:
```json
{
  "location": "london",
  "resolvedAddress": "London, United Kingdom",
  "description": "Partly cloudy throughout the day with a chance of rain.",
  "currentConditions": {
    "temp": 15.5,
    "feelslike": 14.0,
    "humidity": 70.0,
    "windspeed": 12.5,
    "conditions": "Cloudy",
    "datetime": "2025-04-29T12:00:00"
  },
  "forecast": [
    {
      "datetime": "2025-04-29",
      "temp": 15.5,
      "feelslike": 14.0,
      "humidity": 70.0,
      "windspeed": 12.5,
      "conditions": "Cloudy",
      "description": "Cloudy throughout the day."
    },
    ...
  ]
}
```

### Health Check

```
GET /api/health
```

Example Response:
```json
{
  "status": "UP",
  "timestamp": "2025-04-29T12:00:00"
}
```

## Web UI

A simple web UI is available at the root URL (http://localhost:8080) for testing the API. It allows you to:

- Enter a city name and view weather data
- See the raw API response
- Check the health of the service

## Development

### Running Tests

```
mvn test
```

### Mock Data

When no valid API key is provided, the service will return mock weather data for development and testing purposes.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
