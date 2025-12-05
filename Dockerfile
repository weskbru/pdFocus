# Estágio 1: Build
FROM gradle:8.5.0-jdk17 AS builder
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x ./gradlew
RUN ./gradlew dependencies

COPY src ./src

RUN ./gradlew clean bootJar -x test

# Estágio 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o JAR correto
COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
