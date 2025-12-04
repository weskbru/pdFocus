# --- Estágio 1: Build ---
FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /app

# Copia configurações
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./

RUN chmod +x ./gradlew

# Baixa dependências (Cache layer)
RUN ./gradlew dependencies

# Copia o código fonte
COPY src ./src

# [CORREÇÃO CRÍTICA 1]
# Força 'clean' para limpar lixo antigo e 'bootJar' para criar o executável correto
RUN ./gradlew clean bootJar -x test

# [CORREÇÃO CRÍTICA 2]
# Renomeia o JAR gerado para um nome padrão 'app.jar' independente se o projeto se chama 'pdFocus' ou 'pdfocus'
# Isso evita o erro de "File not found" ou pegar o jar errado
RUN find build/libs -name "*SNAPSHOT.jar" -not -name "*plain.jar" -exec cp {} app.jar \;

# --- Estágio 2: Runtime ---
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# [CORREÇÃO CRÍTICA 3]
# Copia apenas o app.jar padronizado
COPY --from=builder /app/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]