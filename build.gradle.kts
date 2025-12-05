plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.pdfocus"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val lombokVersion = "1.18.30"
val junitVersion = "5.10.0"

dependencies {
    // Web & Security
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2") // Útil para testes locais

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // PDF Tools
    implementation("org.apache.pdfbox:pdfbox:3.0.5")

    // Lombok
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // Testes
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

// Configurações de Compilação
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Amapstruct.defaultComponentModel=spring")
}

// Configuração de Testes
tasks.test {
    useJUnitPlatform()
}

// --- CONFIGURAÇÃO CRÍTICA PARA O DEPLOY ---

// 1. Configuração Global do Spring Boot
springBoot {
    mainClass.set("com.pdfocus.boot.PdfocusApplication")
}

// 2. Configuração Específica da Criação do JAR (A Blindagem)
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    // Garante que o JAR executável saiba onde começar
    mainClass.set("com.pdfocus.boot.PdfocusApplication")

    // Simplifica a vida: Gera o arquivo já com o nome 'app.jar'
    // Isso evita problemas de versão ou nome no Dockerfile
    archiveFileName.set("app.jar")
}

// 3. Desativa a geração do JAR simples (sem dependências) para não confundir o deploy
tasks.getByName<org.gradle.api.tasks.bundling.Jar>("jar") {
    enabled = false
}