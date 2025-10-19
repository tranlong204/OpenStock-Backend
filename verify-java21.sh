#!/bin/bash

# Java 21 Upgrade Verification Script
echo "🔍 Verifying Java 21 upgrade for OpenStock Backend..."

# Check Java version
echo "📋 Checking Java version..."
java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$java_version" -ge 21 ]; then
    echo "✅ Java $java_version detected (Java 21+)"
else
    echo "❌ Java $java_version detected. Please install Java 21 or higher."
    exit 1
fi

# Check Maven version
echo "📋 Checking Maven version..."
mvn_version=$(mvn -version | head -n 1 | cut -d' ' -f3)
echo "✅ Maven $mvn_version detected"

# Verify project structure
echo "📋 Verifying project structure..."
if [ -f "pom.xml" ]; then
    echo "✅ pom.xml found"
else
    echo "❌ pom.xml not found"
    exit 1
fi

if [ -f "src/main/java/com/opendevsociety/openstock/OpenstockBackendApplication.java" ]; then
    echo "✅ Main application class found"
else
    echo "❌ Main application class not found"
    exit 1
fi

# Check Docker configuration
echo "📋 Checking Docker configuration..."
if grep -q "openjdk:21-jdk-slim" Dockerfile; then
    echo "✅ Dockerfile updated for Java 21"
else
    echo "❌ Dockerfile not updated for Java 21"
fi

# Test compilation
echo "📋 Testing project compilation..."
if mvn clean compile -q; then
    echo "✅ Project compiles successfully with Java 21"
else
    echo "❌ Project compilation failed"
    exit 1
fi

echo ""
echo "🎉 Java 21 upgrade verification completed successfully!"
echo "🚀 You can now run the application with: mvn spring-boot:run"
