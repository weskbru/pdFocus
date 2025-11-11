# 🧱 Arquitetura do Projeto – Pdfocus

## 🧠 Visão Geral

O **Pdfocus** adota os princípios da **Clean Architecture**, combinados com a **Arquitetura Hexagonal (Ports & Adapters)** e conceitos de **Domain-Driven Design (DDD)**.  
Essa abordagem garante um sistema **modular, testável e independente de frameworks**, ideal para evolução de um produto SaaS.

O foco da arquitetura é permitir que o domínio permaneça **puro** — livre de dependências externas — enquanto as camadas de infraestrutura e interface se conectam através de **ports (interfaces)** e **adapters (implementações concretas)**.

---

## 🏗️ Camadas Arquiteturais

### 1. **Core (Domínio)**
- Contém **entidades puras (POJOs)**, **objetos de valor** e **regras de negócio**.
- Não depende de nenhuma tecnologia externa ou framework.
- Inclui exceções específicas do domínio (ex: `ResumoNaoEncontradoException`, `EmailInvalidoException`).
- Pode ser **testado isoladamente** sem necessidade de banco ou rede.

📁 Exemplo de pacote:
```text
core/
├── models/
├── exceptions/
└── shared/
````


### 2. **Application (Casos de Uso)**
- Contém a **lógica de orquestração** entre o domínio e o mundo externo.
- Define **ports (interfaces)** que descrevem o que o domínio precisa (como salvar dados, enviar e-mails, autenticar usuários, etc.).
- Implementa **services e commands** que representam os fluxos de uso principais.
- Não depende da infraestrutura — apenas da definição de contratos.

📁 Exemplo de pacote:
```text
application/
├── resumo/
├── disciplina/
├── material/
├── usuario/
└── feedback/
````

### 3. **Infra (Infraestrutura / Adapters)**
- Implementa os detalhes técnicos definidos nas ports da aplicação.
- Inclui:
    - **Controllers REST** (Spring Web)
    - **Repositórios JPA** (persistência)
    - **Segurança (Spring Security + JWT)**
    - **Envio de e-mails**
    - **Storage de arquivos**
- É a camada mais flexível — pode ser alterada ou substituída sem afetar o domínio.

📁 Exemplo de pacote:
```text
infra/
├── web/
├── persistence/
├── config/
├── security/
├── email/
└── storage/
````
### 4. **Boot / Configuração**
- Ponto de entrada do sistema e inicialização do **Spring Boot**.
- Define beans, injeção de dependências e configurações gerais.
- Permite que todo o resto da aplicação seja carregado de forma limpa e modular.

📁 Exemplo:
```text
boot/
└── PdfocusApplication.java
````

## 🧠 Diagrama de Arquitetura (Visão Hexagonal)

```mermaid
flowchart TB

    subgraph User["👤 Usuário / Frontend (pdfocus-frontend)"]
        UI["Interface Web (Angular + TypeScript)"]
     end

    subgraph Infra["🌐 Infra (Adapters)"]
        Controller["Controllers REST"]
        Persistence["Repositórios JPA"]
        Security["JWT / Autenticação"]
        Email["Envio de E-mails"]
        Storage["Armazenamento de Arquivos"]
    end

    subgraph Application["⚙️ Application (Casos de Uso)"]
        Service["Services / Use Cases"]
        PortsIn["Ports de Entrada"]
        PortsOut["Ports de Saída"]
    end

    subgraph Core["💡 Core (Domínio)"]
        Entities["Entidades de Domínio"]
        Rules["Regras de Negócio"]
        Exceptions["Exceções"]
    end

    User -->|Requisição HTTP| Controller
    Controller -->|Chama Caso de Uso| Service
    Service -->|Aplica Regras| Core
    Service -->|Acessa Adaptadores| Persistence
    Service -->|Autentica| Security
    Service -->|Faz Upload| Storage
    Service -->|Envia Email| Email
    Persistence -->|Salva Dados| DB[(PostgreSQL)]
````

### 🧪 Testabilidade

* O Core e o Application podem ser testados sem frameworks — apenas com JUnit e Mockito.
* A Infra é testada via integração, garantindo que os adapters concretos funcionem corretamente.
* A arquitetura favorece TDD e injeção de dependências controlada.

### 🧱 Decisões Arquiteturais

| Tema | Decisão | Justificativa |
| :--- | :--- | :--- |
| Arquitetura | Hexagonal + DDD + Clean Architecture | Mantém separação de responsabilidades e facilidade de testes |
| Framework | Spring Boot 3.x | Produtividade, robustez e ecossistema maduro |
| Banco de Dados | PostgreSQL via JPA | Consistente, relacional e fácil de integrar com Spring Data |
| Segurança | Spring Security + JWT | Simples, escalável e ideal para SaaS |
| Build Tool | Gradle Kotlin DSL | Sintaxe moderna e manutenção simplificada |
| Empacotamento | Docker | Portabilidade e fácil deploy |
| Documentação | Swagger / OpenAPI | Clareza para desenvolvedores e futuros consumidores de API |

### ♻️ Trade-offs

| Decisão | Justificativa |
| :--- | :--- |
| ❌ A arquitetura inicial sem Spring Boot foi substituída | A maturidade do projeto exigiu gerenciamento robusto e injeção nativa |
| ✅ Mantido domínio puro e desacoplado | Permite testes isolados e evolução modular |
| ❌ A estrutura em módulos foi consolidada em um único projeto monolítico modular | Simplifica deploy e CI/CD no estágio atual |
| ✅ Gradle + Kotlin DSL adotado | Configuração enxuta e segura |
| ✅ Separação entre back e front | Mantém escalabilidade e versionamento independente |

### 🚀 Futuras Extensões

* Implementar geração de resumos via API externa de IA.
* Adicionar pipeline CI/CD (GitHub Actions) para build, teste e deploy automático.
* Incorporar Spring Actuator para métricas e monitoramento.
* Evoluir para arquitetura event-driven (RabbitMQ / Kafka).
* Criar módulo de analytics para acompanhamento de uso.

### 📌 Referências

* Clean Architecture – Robert C. Martin
* Hexagonal Architecture (Ports & Adapters) – Alistair Cockburn
* Domain-Driven Design – Eric Evans
* Effective Java – Joshua Bloch
* 12-Factor App Principles – Heroku