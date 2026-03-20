# Étape 1 : Build avec Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécution
FROM eclipse-temurin:21-jdk
COPY --from=build /target/*.jar app.jar

# Création du dossier data pour la base de données H2
RUN mkdir -p /data
# Volume pour que les données persistent si besoin
VOLUME /data

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]