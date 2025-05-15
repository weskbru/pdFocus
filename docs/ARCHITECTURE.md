# Arquitetura do Projeto – PDFocus

## 🧠 Visão Geral

Este projeto adota os princípios da **Clean Architecture**, com separação clara entre:

- **Interface com o usuário (entrada)** → Camada de controllers ou interface CLI/API
- **Casos de uso (application)** → Regras de orquestração
- **Domínio (domain)** → Entidades, regras de negócio puras
- **Infraestrutura (infrastructure)** → Persistência, PDF, segurança, etc.

Essa abordagem permite independência de frameworks, modularidade e facilidade de testes.

---

## 🏗️ Camadas

### 1. **Domain**
- Contém entidades puras (POJOs), validações e regras de negócio.
- Não depende de nenhuma tecnologia externa.
- Pode ser testada sem infraestrutura.

### 2. **Application**
- Implementa os **casos de uso** (ex: autenticar usuário, extrair texto, gerar resumo).
- Orquestra entidades e repositórios.
- Conhece o domínio, mas não conhece a infraestrutura.

### 3. **Infrastructure**
- Conecta com banco de dados, leitura de PDFs, segurança, rede, etc.
- Implementa interfaces definidas no domínio (ex: repositórios).
- Pode usar qualquer framework sem afetar o core.

### 4. **Interface/Controller**
- Camada de entrada do sistema (REST, CLI, etc).
- Traduz requisições em comandos para o application.
- Pode ser trocada por outra interface sem alterar regras.

---

## 🧪 Testabilidade

- O domínio e a aplicação são **totalmente testáveis** sem necessidade de mocks complexos ou banco.
- A infraestrutura é pluggable e pode ser testada por integração.

---

## 🧱 Decisões Arquiteturais

- Evitei uso de Spring ou qualquer framework no core, para manter o acoplamento mínimo.
- Divisão por contexto: cada módulo tem seu domínio, aplicação e infra separados.
- `Main.java` inicializa o sistema manualmente (injeção simples, por composição).

---

## ♻️ Trade-offs

| Decisão                                 | Justificativa                                                                 |
|----------------------------------------|------------------------------------------------------------------------------|
| ❌ Não usei Spring Boot no início       | MVP leve, menor tempo de build e sem acoplamento prematuro                   |
| ✅ Usei Gradle com Kotlin DSL           | Mais seguro e conciso que o Groovy                                           |
| ❌ Não implementei banco relacional     | O MVP foca em lógica, não persistência — deixei preparado para plugar depois |
| ✅ Testes escritos com JUnit 5          | Testes rápidos, focados em domínio e casos de uso                            |

---

## 🚀 Futuras Extensões

- Migrar para Spring Boot caso a complexidade aumente
- Adicionar camada assíncrona para tarefas pesadas (RabbitMQ, Kafka, etc)
- API pública REST ou integração com mensageria
- Deploy em contêiner (Docker) com CI/CD no GitHub Actions

---

## 📌 Referências

- Clean Architecture – Robert C. Martin
- Hexagonal Architecture (Ports & Adapters)
- Effective Java – Joshua Bloch
- 12 Factor App Principles
