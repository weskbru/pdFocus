# Estágio 1: Build
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

COPY . .

RUN gradle clean bootJar --no-daemon

# Descobre automaticamente o jar gerado e renomeia para app.jar
RUN cp build/libs/*.jar app.jar

# Estágio 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
