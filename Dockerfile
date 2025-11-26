# --------- FASE 1: BUILD ---------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom y descargamos dependencias
COPY pom.xml .
RUN mvn -q -e -B dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Compilamos y generamos el JAR
RUN mvn clean package -DskipTests

# --------- FASE 2: RUN ---------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 👀 Cambia el nombre del JAR si tu pom tiene otro artifactId/version
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Render usa la variable PORT (por defecto 10000)
ENV PORT=10000
EXPOSE 10000

# Arrancamos Spring Boot escuchando en ese puerto
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]
