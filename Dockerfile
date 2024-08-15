# Stage 1: Build
FROM maven:3.9.7-eclipse-temurin-21-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/github-repo-details-0.0.1-SNAPSHOT.jar /app/github-repo-details.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/github-repo-details.jar"]
