# Estágio 1: Build
FROM gradle:8.5.0-jdk17 AS builder
WORKDIR /app

# Copia arquivos de configuração primeiro (para cache)
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x ./gradlew
RUN ./gradlew dependencies

# Copia o código fonte
COPY src ./src

# Compila e gera o 'app.jar'
# O clean garante que não usamos lixo antigo
RUN ./gradlew clean bootJar -x test

# Estágio 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o JAR gerado (agora com nome garantido)
COPY --from=builder /app/build/libs/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]