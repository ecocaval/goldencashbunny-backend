# Stage 1: Build the application
FROM ubuntu:latest AS build
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get install -y maven
COPY . .
RUN mvn package -DskipTests && \
    chmod +x target/*.jar

# Stage 2: Create the final Docker image
FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /workspace/target/demo-1.jar app.jar
CMD ["java", "-jar", "/app.jar"]
