# Use a lightweight JDK image
FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built jar file into the image
COPY target/iam-ddd-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app will run on
EXPOSE 8082

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
