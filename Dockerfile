FROM openjdk:17-jdk-slim

LABEL maintainer="library-management-system"
LABEL version="1.0.0"

# Create app directory
WORKDIR /app

# Install dependencies
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Copy the executable jar
COPY target/library-management-system-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]