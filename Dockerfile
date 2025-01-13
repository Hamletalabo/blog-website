FROM maven:3.9.9-amazoncorretto-21-al2023 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Use a lightweight image for running the app
FROM openjdk:24-ea-jdk-bullseye

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/blog-website.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]