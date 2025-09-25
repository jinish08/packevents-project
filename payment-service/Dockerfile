# --- Stage 1: Build the application ---
# Use a Maven image with a specific Java version to build our application
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml first to leverage Docker's layer caching.
# If the pom.xml doesn't change, Maven dependencies won't be re-downloaded.
COPY pom.xml .

# Copy the rest of the source code
COPY src ./src

# Build the application, skipping the tests as they should be run in a CI pipeline
RUN mvn clean install -DskipTests

# --- Stage 2: Create the final, smaller image ---
# Use a minimal Java Runtime Environment (JRE) image
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the built .jar file from the 'builder' stage
COPY --from=builder /app/target/payment-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application runs on
EXPOSE 8082

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]