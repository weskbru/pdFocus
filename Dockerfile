# --- Estágio 1: Build da Aplicação com Gradle ---
# Usamos uma imagem base que já tem o JDK 17 e o Gradle instalados.
# O 'as builder' dá um nome a este estágio para que possamos nos referir a ele depois.
FROM gradle:8.5.0-jdk17 AS builder

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# Copia os arquivos de build do Gradle para o container.
# Isso aproveita o cache de camadas do Docker. Se as dependências não mudarem,
# o Docker não precisará baixá-las novamente a cada build.
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

# Baixa todas as dependências do projeto.
RUN gradle dependencies --write-locks

# Copia o código-fonte da nossa aplicação para o container.
COPY src ./src

# Executa o comando do Gradle para construir o projeto e gerar o arquivo .jar.
# O '-x test' pula a execução dos testes, tornando o build mais rápido.
RUN gradle build -x test


# --- Estágio 2: Criação da Imagem Final ---
# Usamos uma imagem base muito menor, que contém apenas o necessário para RODAR a aplicação (o Java JRE).
# Isso torna nossa imagem final muito mais leve e segura.
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho.
WORKDIR /app

# [--- ESTA É A CORREÇÃO ---]
# Copia o arquivo .jar específico que descobrimos no build local (pdFocus-1.0-SNAPSHOT.jar).
# Isso remove a ambiguidade do '*.jar'.
COPY --from=builder /app/build/libs/pdFocus-1.0-SNAPSHOT.jar app.jar

# Expõe a porta 8080, informando ao Docker que nossa aplicação escuta nesta porta.
EXPOSE 8080

# O comando que será executado quando o container iniciar.
# Inicia a aplicação Java.
ENTRYPOINT ["java", "-jar", "app.jar"]