# Paso 1: Compilación
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Paso 2: Ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/ms-auth-users-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]