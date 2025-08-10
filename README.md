# **pdFocus - API Backend**

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) 
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-brightgreen)](https://spring.io/projects/spring-boot) 
[![Docker](https://img.shields.io/badge/Docker-Ready-blue)](https://www.docker.com/) 
![Status](https://img.shields.io/badge/Status-Backend_MVP_Completo-success)

**Repositório do Projeto:** [github.com/weskbru/pdFocus](https://github.com/weskbru/pdFocus)  
**Autor:** [Wesley Bruno Jp - LinkedIn](https://www.linkedin.com/in/wesley-bruno/) | [GitHub](https://github.com/weskbru)

---

Bem-vindo ao repositório do backend do **pdFocus**, um projeto de SaaS (Software as a Service) para gestão de estudos, projetado para ajudar estudantes a organizar disciplinas, resumos e materiais de forma eficiente.

Este projeto foi desenvolvido como um portfólio prático, com foco na aplicação de conceitos de **Clean Architecture** e nas melhores práticas de engenharia de software para construir uma aplicação robusta, segura e escalável.

---

## ✨ **Visão Geral do Projeto**

O `pdFocus` visa ser uma ferramenta central na vida de um estudante, permitindo:
* **Organização:** Cadastrar disciplinas e agrupar todo o conteúdo relacionado.
* **Criação:** Escrever e gerenciar resumos de estudo.
* **Armazenamento:** Fazer o upload de materiais de apoio, como PDFs, slides e imagens.
* **Revisão:** (Futuro) Acompanhar o ciclo de revisões para otimizar o aprendizado.

Este repositório contém exclusivamente o **backend da aplicação**, que expõe uma API RESTful completa para ser consumida por um futuro front-end.

---

## 🏛️ **Arquitetura**

A fundação do projeto é a **Clean Architecture**, utilizando o padrão de **Portas e Adaptadores**. Esta abordagem garante um sistema desacoplado, testável e fácil de manter.

### **Diagrama da Arquitetura do Projeto**
<img width="3840" height="2582" alt="Diagrama da Arquitetura" src="https://github.com/user-attachments/assets/23093a18-d551-4ac3-b788-4ac6ff3d32e7" />

As camadas são divididas da seguinte forma:

* **`core` (Domínio):** O coração do sistema. Contém os modelos de negócio (`Usuario`, `Disciplina`, etc.) como classes Java puras, sem nenhuma dependência de frameworks.
* **`application` (Casos de Uso):** A camada que orquestra a lógica de negócio. Organizada por funcionalidade (`disciplina`, `resumo`), define o que a aplicação pode fazer através de interfaces (`...UseCase`).
* **`infra` (Infraestrutura):** A camada externa que lida com os detalhes técnicos:
    * **API REST:** `Controllers` que expõem os casos de uso via HTTP.
    * **Persistência:** `Adapters` que implementam as portas de repositório usando Spring Data JPA e PostgreSQL.
    * **Segurança:** Configuração do Spring Security e lógica de JWT.
    * **Armazenamento:** `Adapter` para guardar arquivos físicos no disco.

---

## 🚀 **Principais Funcionalidades Implementadas**

* **Sistema de Autenticação e Autorização Completo** com Spring Security e Tokens JWT.
* **API RESTful com CRUD completo** para `Disciplinas` e `Resumos`, com todas as operações contextualizadas pelo usuário autenticado.
* **Funcionalidade de Upload de `Materiais`**, com separação entre o armazenamento físico de arquivos e a persistência dos metadados.
* **Tratamento de Erros Global** com `@ControllerAdvice` para respostas HTTP consistentes (`404`, `400`, `409`).

---

## 🛠️ **Stack Tecnológica**

| Categoria      | Tecnologia                               |
| -------------- | ---------------------------------------- |
| **Linguagem** | Java 17                                  |
| **Framework** | Spring Boot 3                            |
| **Segurança** | Spring Security, JSON Web Tokens (JWT)   |
| **Persistência** | Spring Data JPA, Hibernate               |
| **Base de Dados** | PostgreSQL (Produção), H2 (Desenvolvimento) |
| **Deploy** | Docker                                   |
| **Build** | Gradle com Kotlin DSL                    |
| **Testes** | JUnit 5, Mockito                         |

---

## 🏁 **Como Executar o Projeto**

### **Pré-requisitos**
* Java 17+
* Gradle 8.5+
* PostgreSQL instalado e rodando
* Docker Desktop instalado e rodando

### **1. Configuração Local**
1.  Clone o repositório:
    ```bash
    git clone https://github.com/weskbru/pdFocus.git
    ```
2.  Navegue até a pasta do projeto:
    ```bash
    cd pdFocus
    ```
3.  Crie um banco de dados PostgreSQL chamado `pdfocus_db`.
4.  Configure suas credenciais no arquivo `src/main/resources/application.properties`:
    ```properties
    spring.datasource.username=seu_usuario_postgres
    spring.datasource.password=sua_senha_postgres
    ```
5.  Execute a aplicação pelo seu IDE ou via Gradle:
    ```bash
    ./gradlew bootRun
    ```
A API estará disponível em `http://localhost:8080`.

### **2. Executando com Docker**
1.  Construa o arquivo `.jar` da aplicação:
    ```bash
    ./gradlew build
    ```
2.  Construa a imagem Docker:
    ```bash
    docker build -t pdfocus-api .
    ```
3.  Rode o contêiner. Certifique-se de que o PostgreSQL está configurado para aceitar conexões externas (como fizemos na configuração do `postgresql.conf`):
    ```bash
    docker run --rm --name pdfocus-container -p 8080:8080 pdfocus-api
    ```

---

## 🗺️ **Próximos Passos**

* [ ] Desenvolvimento do front-end (React/Vue/Angular).
* [ ] Implementação de uma ferramenta de migração de banco de dados (Flyway/Liquibase).
* [ ] Configuração de um pipeline de CI/CD com GitHub Actions.

---

## 👨‍💻 **Autor**

**Wesley Bruno Jp**  
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Wesley_Bruno-blue?logo=linkedin)](https://www.linkedin.com/in/wesley-bruno/)  
[![GitHub](https://img.shields.io/badge/GitHub-weskbru-black?logo=github)](https://github.com/weskbru)  
