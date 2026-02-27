# Étape 1 : Build
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Run
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# On utilise un chemin relatif pour être plus sûr
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]