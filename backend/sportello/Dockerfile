# Stage 1: Compilazione
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copio il pom padre
COPY pom.xml ./
COPY sportello/ ./sportello/

# Compilo con maven e genero il JAR
WORKDIR /app/sportello/
RUN mvn clean package -DskipTests

# Stage 2: Esecuzione
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copio il file generato dallo stage 1
COPY --from=build /app/sportello/target/*.jar sportello.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "sportello.jar"]