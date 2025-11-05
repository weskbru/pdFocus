# 🧠 Pdfocus — Plataforma de Resumos Inteligentes

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-brightgreen)
![Gradle](https://img.shields.io/badge/Build-Gradle-blue)
![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-yellow)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## 🚀 Visão Geral

**Pdfocus** é uma plataforma **micro SaaS** desenvolvida em **Java + Spring Boot** para automatizar a criação de resumos de estudos a partir de materiais em PDF.

A proposta é transformar o processo de aprendizado em algo mais inteligente e eficiente, permitindo que o usuário envie um material e obtenha um resumo gerado de forma automática e organizada.

> “Aprender é reter — o Pdfocus ajuda você a focar no essencial.”

---

## 🧱 Arquitetura

O projeto segue uma arquitetura **Hexagonal (Ports & Adapters)** com princípios de **DDD (Domain-Driven Design)**, garantindo baixo acoplamento e alta testabilidade.

**Camadas principais:**
- **core** → Regras de negócio e entidades de domínio.
- **application** → Casos de uso (serviços e ports).
- **infra** → Adapters concretos (controllers REST, persistência, segurança, e-mail, storage).
- **boot/config** → Inicialização da aplicação e configuração do Spring.

📘 Documentação detalhada: [`/docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)

---

## 🧰 Stack Tecnológica

| Categoria | Tecnologia |
|------------|-------------|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.x |
| Build Tool | Gradle Kotlin DSL |
| Segurança | Spring Security + JWT |
| Banco de Dados | PostgreSQL / JPA |
| Armazenamento de Arquivos | Local FileSystem (Storage Adapter) |
| Testes | JUnit 5 + Mockito |
| Documentação | Swagger / OpenAPI |
| Containerização | Docker |

---

## ⚙️ Execução do Projeto

### Rodar com Docker

```bash
docker build -t pdfocus .
docker run -p 8080:8080 pdfocus
```

### A aplicação estará disponível em:

👉 http://localhost:8080

### 📡 Endpoints Principais

| Módulo | Endpoint | Descrição |
| :--- | :--- | :--- |
| Autenticação | `POST /auth/login` | Autentica o usuário e retorna token JWT |
| Usuário | `POST /usuarios` | Cria novo usuário |
| Materiais | `POST /materiais` | Faz upload de material PDF |
| Resumos | `POST /resumos/gerar-automatico` | Gera resumo automático via IA local |
| Feedback | `POST /feedback` | Envia feedback do usuário |

Para mais detalhes, acesse a documentação Swagger:
🔗 `/swagger-ui/index.html`

### 🧠 Estrutura de Pastas (Resumo)

```text
src/
 ├── main/
 │    ├── java/com/pdfocus/
 │    │     ├── boot/              # Inicialização da aplicação
 │    │     ├── config/            # Configurações do Spring e segurança
 │    │     ├── core/              # Domínio e regras de negócio
 │    │     ├── application/       # Casos de uso
 │    │     └── infra/             # Adapters REST, DB, Storage, Email
 │    └── resources/
 │          └── application.properties
 └── test/
      └── java/com/pdfocus/
````
### 🧩 Testes

Comando de execução:

```shell
./gradlew test
````

### Os testes cobrem:

* Casos de uso (Services)
* Entidades de domínio
* Mapeamentos JPA
* Handlers de erro

Relatórios gerados em:
`build/reports/tests/test/index.html`

## 📋 Pré-requisitos

Para compilar e rodar o projeto localmente, você precisará de:

* [Java 17 (ou superior)](https://www.oracle.com/java/technologies/downloads/)
* [Gradle 8.x+](https://gradle.org/install/)
* [Docker](https://www.docker.com/get-started/) (Opcional, para rodar em container)

## ⚙️ Configuração

O projeto usa variáveis de ambiente para configurar a conexão com o banco de dados e as chaves de segurança.

1.  **Crie um banco de dados PostgreSQL.**
2.  Na raiz do projeto (`src/main/resources/`), renomeie o arquivo `application.properties.example` (você precisará criar este arquivo) para `application.properties`.
3.  Preencha as variáveis de ambiente necessárias:

    ```properties
    # Exemplo de application.properties
    
    # --- Banco de Dados (PostgreSQL) ---
    spring.datasource.url=jdbc:postgresql://localhost:5432/pdfocus_db
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    
    # --- Configuração do JPA ---
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    
    # --- Segurança (JWT) ---
    # !! Use um valor forte e secreto em produção !!
    jwt.secret.key=SUA_CHAVE_SECRETA_AQUI
    
    # --- Storage (Local) ---
    # Diretório onde os uploads serão salvos
    storage.local.upload-dir=./uploads
    ```

### 🪪 Licença

Este projeto está sob a licença MIT.

Sinta-se livre para estudar e contribuir.

## 👨‍💻 Autor

**Wesley Bruno JP**  
Desenvolvedor Backend • Estudante de ADS • Focado em Arquiteturas Limpa e Micro SaaS  
📫 [LinkedIn](https://www.linkedin.com/in/wesley-bruno/)  
💻 [GitHub](https://github.com/weskbru)

---

## 🧩 Repositórios do Projeto

| Módulo | Repositório | Descrição |
|---------|--------------|------------|
| 🧠 Backend (API) | [pdfocus-backend](https://github.com/weskbru/pdFocus) | API desenvolvida em **Java + Spring Boot**, seguindo arquitetura **Hexagonal + DDD**. Responsável pela autenticação, geração de resumos e controle de usuários. |
| 💻 Frontend (Dashboard) | [pdfocus-frontend](https://github.com/weskbru/pdfocus-frontend) | Interface web moderna em **React + TypeScript**, que consome a API do Pdfocus e oferece uma experiência fluida e responsiva. |

---

### 🌱 Próximos Passos

* Implementar geração de resumos via API externa de IA
* Adicionar pipeline CI/CD (GitHub Actions)
* Melhorar logging e métricas com Spring Actuator
* Criar painel administrativo no dashboard frontend