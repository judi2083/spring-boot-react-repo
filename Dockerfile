# Dockerfile

# Use official OpenJDK as base image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Use wildcard in case version changes
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8081

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]