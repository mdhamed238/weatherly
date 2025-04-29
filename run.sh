#!/bin/bash

echo "Starting Weatherly API..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Docker is not installed. Please install Docker to run this application in a container."
    echo "Alternatively, you can run the application directly using 'mvn spring-boot:run'"
    exit 1
fi

# Check if docker-compose is installed
if ! command -v docker compose &> /dev/null; then
    echo "Docker Compose is not installed. Please install Docker Compose to run this application."
    exit 1
fi

# Build and start the containers
echo "Building and starting containers..."
docker compose up --build -d

# Wait for the application to start
echo "Waiting for the application to start..."
sleep 5

# Check if the application is running
echo "Checking if the application is running..."
response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/health)

if [ "$response" -eq 200 ]; then
    echo "Weatherly API is running successfully!"
    echo "You can access the web UI at: http://localhost:8080"
    echo "API endpoints:"
    echo "  - Weather data: http://localhost:8080/api/weather/{cityCode}"
    echo "  - Health check: http://localhost:8080/api/health"
else
    echo "Failed to start Weatherly API. Please check the logs using 'docker compose logs'"
fi
