# Weatherly API

A Spring Boot application that fetches weather data from Visual Crossing's API, implements Redis caching, and provides a clean web interface for testing.

![Weatherly API](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

## Features

- **Weather Data**: Fetch current conditions and forecasts for any city
- **Redis Caching**: Optimize performance with 12-hour data caching
- **Rate Limiting**: Protect the API from abuse with Bucket4j rate limiting
- **API Key Authentication**: Secure your API with key-based authentication
- **Web UI**: Beautiful Bootstrap-based interface for easy testing
- **OpenAPI Documentation**: Comprehensive API documentation with Swagger UI
- **Health Check**: Monitor application status with a dedicated health endpoint
- **Docker Support**: Easy containerization with Docker and docker-compose
- **Error Handling**: Consistent error responses with global exception handling

## Tech Stack

- **Spring Boot 3.4.5**: Core framework
- **Spring WebFlux**: For reactive HTTP requests
- **Redis**: For caching weather data
- **Bucket4j**: For rate limiting
- **Lombok**: For reducing boilerplate code
- **SpringDoc OpenAPI**: For API documentation
- **Bootstrap 5**: For the web UI
- **Docker**: For containerization

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/weather/{cityCode}` | GET | Get weather data for a specific city |
| `/api/health` | GET | Check API health status |
| `/` | GET | Redirect to the web UI |
| `/swagger-ui` | GET | API documentation with Swagger UI |
| `/v3/api-docs` | GET | OpenAPI specification in JSON format |

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.8+ (or use the included Maven wrapper)
- Redis server (optional, for caching)
- Visual Crossing API key

### Configuration

Create an `.env` file in the project root with the following variables:

```
# Required
WEATHER_API_KEY=your_visual_crossing_api_key

# Optional
REDIS_HOST=localhost
REDIS_PORT=6379
```

### Running Locally

```bash
# Clone the repository
git clone https://github.com/mdhamed/weatherly.git
cd weatherly

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Using Docker

```bash
# Build and run with Docker Compose
docker-compose up -d
```

This will start both the Weatherly API and Redis container for caching.

## Security Features

### API Key Authentication

The API can be protected with API key authentication. When enabled, all requests to `/api/**` endpoints must include a valid API key in the header.

To enable API key authentication:

```properties
# In .env file or environment variables
WEATHER_API_KEY=your_secret_key
APP_API_KEY_ENABLED=true
```

Then include the API key in your requests:

```bash
curl -X GET "http://localhost:8080/api/weather/london" -H "accept: application/json" -H "X-API-Key: your_secret_key"
```

Note: The same `WEATHER_API_KEY` is used for both accessing the external weather API service and for authenticating requests to this API service.

### Rate Limiting

The API implements rate limiting to prevent abuse. By default, it allows:
- 20 requests capacity
- 10 tokens refilled every minute

Rate limit headers are included in responses:
- `X-Rate-Limit-Remaining`: Number of remaining requests
- `X-Rate-Limit-Retry-After-Seconds`: Seconds to wait when limit is exceeded

## Testing

The application includes unit tests for controllers and services:

```bash
# Run tests
mvn test
```

## API Usage

### Example Request

```bash
curl -X GET "http://localhost:8080/api/weather/london" -H "accept: application/json" -H "X-API-Key: your_api_key_here"
```

### Example Response

```json
{
  "location": "London",
  "resolvedAddress": "London, England, United Kingdom",
  "description": "Clear conditions throughout the day.",
  "currentConditions": {
    "temp": 18.5,
    "feelslike": 18.2,
    "humidity": 72.3,
    "windspeed": 11.5,
    "conditions": "Clear"
  },
  "forecast": [
    {
      "datetime": "2025-04-30",
      "temp": 19.2,
      "feelslike": 19.0,
      "humidity": 68.4,
      "windspeed": 9.8,
      "conditions": "Partly cloudy"
    },
    // More forecast days...
  ]
}
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- [Visual Crossing](https://www.visualcrossing.com/) for the weather data API
- [Spring Boot](https://spring.io/projects/spring-boot) for the framework
- [Bootstrap](https://getbootstrap.com/) for the UI components
