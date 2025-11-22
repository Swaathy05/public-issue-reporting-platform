#!/bin/bash

echo "===================================="
echo "Starting Public Service Platform"
echo "===================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

echo ""
echo "Building and starting the application..."
echo "This may take a minute on first run..."
echo ""

# Run the application
mvn spring-boot:run

