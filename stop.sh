#!/bin/bash

# Stop script for OpenStock Backend

echo "Stopping OpenStock Backend..."

# Find and kill the Spring Boot application process
PROCESS_ID=$(pgrep -f "openstock-backend-0.1.0.jar")

if [ -n "$PROCESS_ID" ]; then
    echo "Found Spring Boot process with PID: $PROCESS_ID"
    kill $PROCESS_ID
    
    # Wait for graceful shutdown
    echo "Waiting for graceful shutdown..."
    sleep 3
    
    # Check if process is still running
    if pgrep -f "openstock-backend-0.1.0.jar" > /dev/null; then
        echo "Process still running, forcing termination..."
        pkill -f "openstock-backend-0.1.0.jar"
        sleep 2
    fi
    
    echo "OpenStock Backend stopped successfully"
else
    echo "No running OpenStock Backend process found"
fi

# Also kill any Java processes that might be related (optional)
# Uncomment the following lines if you want to be more aggressive
# echo "Checking for any remaining Java processes..."
# JAVA_PROCESSES=$(pgrep -f "java.*openstock")
# if [ -n "$JAVA_PROCESSES" ]; then
#     echo "Found additional Java processes, stopping them..."
#     pkill -f "java.*openstock"
# fi

echo "Stop script completed"
