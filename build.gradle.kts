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

dependencies {
    // --- Spring Boot Starters ---
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // --- Banco de Dados ---
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")

    // --- JWT ---
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // --- PDF Tools ---
    implementation("org.apache.pdfbox:pdfbox:3.0.5")

    // --- Lombok ---
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // --- Testes ---
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

// Configuração para processar anotações (Lombok/MapStruct)
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Amapstruct.defaultComponentModel=spring")
}

tasks.test {
    useJUnitPlatform()
}

// --- A CORREÇÃO DEPLOY BLINDADO ---

// 1. Define a classe principal globalmente
springBoot {
    mainClass.set("com.pdfocus.boot.PdfocusApplication")
}

// 2. Configura a criação do JAR Executável
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    // Garante que o JAR saiba qual classe rodar
    mainClass.set("com.pdfocus.boot.PdfocusApplication")

    // Força o nome do arquivo final para 'app.jar'.
    // Isso facilita muito para o Docker encontrá-lo.
    archiveFileName.set("app.jar")

    // Garante que duplicações de arquivo não quebrem o build
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// 3. Desativa a geração do JAR simples (não executável) para não confundir
tasks.getByName<org.gradle.api.tasks.bundling.Jar>("jar") {
    enabled = false
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveBaseName.set("app")
    archiveVersion.set("")
    archiveClassifier.set("")
}

configurations.all {
    exclude(group = "commons-logging", module = "commons-logging")
}
