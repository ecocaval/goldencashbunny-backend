# Stage 1: Build the application
FROM ubuntu:latest AS build
RUN apt-get update && \
    apt-get install openjdk-17-jdk -y
COPY . .
RUN mvn package -DskipTests

# Stage 2: Create the final Docker image
FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /target/demo-1.jar app.jar

CMD ["java", "-jar", "/app.jar"]
