# --- Estágio 1: Build da Aplicação com Gradle ---
FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

# DEBUG: Lista arquivos antes do build
RUN ls -la

RUN gradle dependencies --write-locks

COPY src ./src

# DEBUG: Lista arquivos Java para verificar se a classe existe
RUN find . -name "PdfocusApplication.java" -type f

RUN gradle build -x test --stacktrace --info

# DEBUG: Verifica se o JAR foi criado e se contém a classe
RUN ls -la build/libs/
RUN jar tf build/libs/pdFocus-1.0-SNAPSHOT.jar | grep PdfocusApplication || echo "CLASSE NAO ENCONTRADA NO JAR"

# --- Estágio 2: Criação da Imagem Final ---
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/pdFocus-1.0-SNAPSHOT.jar app.jar

# DEBUG: Verifica o JAR na imagem final
RUN jar tf app.jar | grep PdfocusApplication || echo "CLASSE NAO ENCONTRADA NO JAR FINAL"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]