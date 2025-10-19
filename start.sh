#!/bin/bash

# Load environment variables from .env file
if [ -f .env ]; then
    # Export variables from .env file, excluding comments and empty lines
    set -a
    source .env
    set +a
    echo "Loaded environment variables from .env file"
else
    echo "Warning: .env file not found"
fi

# Start the Spring Boot application
java -jar target/openstock-backend-0.1.0.jar
