# ⚙️ Decisões Técnicas – Pdfocus

## 🧠 Contexto

O **Pdfocus** é um projeto **micro SaaS** desenvolvido com foco em **clareza arquitetural, testabilidade e evolução sustentável**.  
As decisões técnicas foram tomadas com base em três princípios centrais:

- **Simplicidade**: o sistema deve ser compreensível e mantido facilmente.
- **Desacoplamento**: nenhuma parte do domínio depende de frameworks externos.
- **Evolução incremental**: cada tecnologia foi adotada no momento certo da maturação do projeto.

---

## 🧰 Tecnologias Escolhidas

| Tecnologia | Justificativa |
|-------------|----------------|
| **Java 17 (LTS)** | Versão estável e moderna da JVM, com suporte a *records*, *sealed classes* e melhorias de performance. |
| **Spring Boot 3.x** | Framework maduro, com injeção de dependência robusta, configuração declarativa e integração nativa com segurança e JPA. |
| **Spring Security + JWT** | Autenticação e autorização seguras, ideais para aplicações SaaS multiusuário. |
| **Gradle Kotlin DSL** | Build moderno, tipado e mais limpo que o Groovy. Facilita automação e CI/CD. |
| **JUnit 5 + Mockito** | Testes unitários e de integração modulares, rápidos e legíveis. |
| **JaCoCo** | Medição de cobertura de testes e integração com pipelines. |
| **PostgreSQL + JPA (Hibernate)** | Banco de dados relacional estável, excelente suporte a JSON e integração nativa com Spring Data. |
| **Swagger / OpenAPI** | Documentação automática dos endpoints REST. Facilita integração com o frontend e APIs externas. |
| **Docker** | Padroniza ambiente e garante portabilidade entre máquinas e servidores. |
| **Mermaid (Markdown)** | Geração de diagramas de arquitetura diretamente em arquivos `.md`, sem dependências externas. |

---

## 🚫 Tecnologias Não Utilizadas (por enquanto)

| Tecnologia | Motivo |
|-------------|--------|
| **MongoDB / NoSQL** | O modelo relacional atende bem o domínio atual. NoSQL pode ser avaliado futuramente para logs e analytics. |
| **Microsserviços** | O projeto ainda está em fase de consolidação. Monólito modular oferece melhor manutenção e simplicidade. |
| **Mensageria (Kafka, RabbitMQ)** | Não há necessidade de processamento assíncrono neste estágio. Será considerado quando houver eventos distribuídos. |
| **Kubernetes / Cloud Deploy** | A aplicação ainda está em desenvolvimento local; Docker é suficiente para o MVP. |
| **CI/CD completo** | Está planejado, mas ainda não configurado no repositório. |

---

## 🧩 Estratégia de Modularização

A modularização segue os princípios de **arquitetura hexagonal** e **DDD (Domain-Driven Design)**:

- Cada **módulo de negócio** (usuário, resumo, material, disciplina, feedback) tem sua própria estrutura interna de `core`, `application` e `infra`.
- A **infraestrutura** implementa apenas as *ports* (interfaces) declaradas na camada de `application`.
- A camada `boot` centraliza a inicialização da aplicação e a configuração de beans globais.
- O domínio (`core`) **não conhece o Spring**, garantindo isolamento e testabilidade.

📁 Exemplo:
```text
application/
└── resumo/
├── dto/
├── port/
├── service/
└── command/
```
---

## 🔍 Critérios de Escolha

- 🧱 **Desacoplamento** — Cada camada é independente e comunicada por interfaces (ports).
- 🧪 **Testabilidade** — É possível testar regras de negócio sem precisar do Spring ou banco.
- ⚙️ **Sustentabilidade** — As tecnologias escolhidas são padrões de mercado com grande comunidade.
- 🚀 **Escalabilidade futura** — O projeto está pronto para migrar para microsserviços ou cloud sem reescrever o core.

---

## 🧱 Exemplos de Trade-offs

| Decisão | Benefício | Custo Técnico |
|----------|------------|----------------|
| Adotar Spring Boot 3.x | Produtividade e integração de módulos (Web, Security, JPA) | Maior tempo de build e curva de configuração |
| Usar DDD + Hexagonal | Clareza, testabilidade e independência de frameworks | Exige disciplina e estrutura mais complexa |
| Usar Gradle Kotlin DSL | Tipagem forte e build mais limpo | Menos tutoriais disponíveis |
| Escolher PostgreSQL | Consistência e maturidade | Requer configuração extra para Docker e CI |
| Separar domínio e aplicação | Escalabilidade e clareza arquitetural | Necessita mais código boilerplate |
| Incluir Docker | Facilita deploy e testes isolados | Adiciona tempo inicial de setup |
| Usar Swagger | Facilita integração e comunicação entre equipes | Pequeno overhead de configuração |

---

## 🧭 Estratégia de Evolução Técnica

As decisões foram tomadas com foco em **evolução incremental** — o sistema deve crescer sem reescrever suas bases.  
O roadmap técnico atual inclui:

- [ ] Adicionar **pipeline CI/CD** com GitHub Actions
- [ ] Implementar **métricas e monitoramento** via Spring Actuator
- [ ] Integrar API de **IA externa** para geração inteligente de resumos
- [ ] Adicionar **cache** (Redis ou Caffeine) para otimizar performance
- [ ] Introduzir **logs estruturados (ELK Stack)** para análise e rastreamento

---

## 📚 Inspirações e Referências

- [Clean Architecture – Robert C. Martin (Uncle Bob)](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design – Eric Evans](https://www.domainlanguage.com/ddd/)
- [Hexagonal Architecture – Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Effective Java – Joshua Bloch](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [12 Factor App Principles](https://12factor.net/)

---

> “A melhor arquitetura é aquela que permite mudar de ideia sem dor.”  
> — *Martin Fowler*