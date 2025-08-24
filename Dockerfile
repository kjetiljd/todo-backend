# Multi-stage build for Spring Boot application
# Use Debian-based images (jammy) for broad multi-arch support (arm64/amd64)
FROM eclipse-temurin:17-jdk-jammy AS build

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradlew.bat .
COPY gradle/ gradle/
COPY build.gradle.kts .

# Make gradlew executable
RUN chmod +x gradlew

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew clean bootJar --no-daemon

RUN ls -la build/libs/

# Runtime stage
FROM eclipse-temurin:17-jre-jammy AS runtime

# Install curl for healthcheck
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

# Create non-root user for security
RUN groupadd -g 1001 spring && \
    useradd -u 1001 -g spring -m -s /usr/sbin/nologin spring

# Set working directory
WORKDIR /app

# Create data directory and set permissions
RUN mkdir -p data && \
    chown -R spring:spring /app

# Copy the built jar from build stage
COPY --from=build /app/build/libs/app-1.0.0.jar app.jar

# Switch to non-root user
USER spring

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -fsS http://localhost:8080/actuator/health >/dev/null || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
