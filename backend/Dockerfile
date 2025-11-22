# Multi-stage Docker build for Spring Boot application
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Create uploads directory
RUN mkdir -p /app/uploads

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port (Railway will set PORT env var)
EXPOSE 8080

# Health check (Railway handles this via railway.toml)
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#   CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-8080}/actuator/health || exit 1

# Run the application
# Railway sets PORT environment variable
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} app.jar"]
