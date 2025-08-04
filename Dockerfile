# ---- Build Stage ----
FROM gradle:8.4-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]