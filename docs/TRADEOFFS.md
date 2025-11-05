# ⚖️ Trade-offs Técnicos – Pdfocus

## 🧭 Propósito

Este documento registra as **decisões técnicas críticas** tomadas durante o desenvolvimento do **Pdfocus**, explicando os **motivos, benefícios e riscos** de cada escolha.

O objetivo é manter **transparência arquitetural** e permitir que futuras evoluções do sistema sejam feitas com consciência das consequências técnicas já conhecidas.

---

## 🧱 Contexto Geral

O **Pdfocus** evoluiu de um MVP leve para um **micro SaaS completo**, estruturado com **Spring Boot 3**, **arquitetura hexagonal** e princípios de **DDD**.  
Cada decisão aqui registrada reflete o equilíbrio entre **simplicidade, desempenho, clareza e evolução sustentável**.

---

## ⚙️ Principais Decisões e Compensações

| Tema | Decisão                                      | Benefício | Custo / Trade-off |
|------|----------------------------------------------|------------|-------------------|
| **Framework** | Adoção do **Spring Boot 3.x**                | Produtividade, injeção nativa, integração com JPA e Security | Aumenta tempo de build e dependência do ecossistema Spring |
| **Arquitetura** | Aplicar **Hexagonal + Clean Architecture + DDD** | Clareza estrutural, testabilidade e independência de frameworks | Estrutura mais complexa, curva de aprendizado maior |
| **Banco de Dados** | Escolha do **PostgreSQL via JPA (Hibernate)** | Modelo relacional sólido, bom suporte a JSON e tipagem forte | Menos flexível que NoSQL para mudanças rápidas de schema |
| **Segurança** | Implementar **JWT + Spring Security**        | Autenticação stateless, escalável para SaaS | Configuração inicial mais trabalhosa |
| **Build Tool** | Uso de **Gradle Kotlin DSL**                 | Sintaxe moderna, segura e declarativa | Menor base de exemplos que Groovy |
| **Containerização** | Adotar **Docker** para execução e deploy     | Ambientes consistentes e reprodutíveis | Requer setup adicional em ambiente local |
| **Documentação** | Uso de **Swagger/OpenAPI**                   | Documentação automática e padronizada da API | Pequeno overhead de manutenção |
| **Frontend separado** | Isolar interface web em repositório Angular  | Independência entre camadas e deploy modular | Exige sincronização entre versões de API e front |
| **Monólito modular** | Manter um único serviço backend modular      | Simplicidade de deploy, CI/CD e debug | Escalabilidade horizontal limitada |
| **Testes** | Priorizar **JUnit + Mockito + JaCoCo**       | Cobertura de regras de negócio e integração | Falta de testes de carga e performance neste estágio |
| **IA local / futura integração externa** | Manter processamento de resumo local por enquanto | Controle sobre o pipeline e custo zero com APIs | Escalabilidade limitada e ausência de aprendizado contínuo |

---

## 🧠 Decisões Rejeitadas (por enquanto)

| Alternativa | Motivo da rejeição |
|--------------|--------------------|
| **Arquitetura de Microsserviços** | Complexidade desnecessária no estágio atual. Monólito modular atende bem. |
| **MongoDB / DynamoDB** | O modelo relacional cobre o domínio atual com melhor integridade de dados. |
| **Mensageria (Kafka, RabbitMQ)** | Nenhuma demanda assíncrona relevante neste ponto do projeto. |
| **CI/CD completo (deploy automático)** | Priorizado para próxima etapa, após estabilização do core. |
| **Kubernetes / Cloud Deploy** | Docker local é suficiente; migração futura para cloud planejada. |

---

## 🔍 Lições Aprendidas

1. **Frameworks são aliados, mas não arquitetos** – Spring Boot ajuda, mas o domínio precisa permanecer puro.
2. **O código que não depende de nada dura mais** – as camadas de domínio e aplicação continuam independentes.
3. **Simplicidade é força** – evitar microserviços prematuros tornou o desenvolvimento mais rápido e previsível.
4. **Documentar cedo evita dívida técnica** – manter `ARCHITECTURE.md` e `TECH-DECISIONS.md` desde o início acelerou a evolução.
5. **Automação vem depois da clareza** – antes de CI/CD, é preciso garantir estabilidade do core.

---

## 🚀 Planos Futuros

| Tema | Próxima decisão prevista |
|-------|---------------------------|
| **Observabilidade** | Adicionar Spring Actuator + Logs estruturados (ELK Stack) |
| **Performance** | Introduzir cache (Caffeine/Redis) |
| **Automação** | Configurar pipeline CI/CD com GitHub Actions |
| **Escalabilidade** | Avaliar transição gradual para microsserviços ou modularização via Jigsaw |
| **IA e Resumos Inteligentes** | Integrar API externa de IA para resumos contextuais e dinâmicos |

---

## 📚 Fontes de Referência

- *Clean Architecture* – Robert C. Martin
- *Domain-Driven Design* – Eric Evans
- *Patterns of Enterprise Application Architecture* – Martin Fowler
- *12 Factor App* – Heroku
- *Effective Java* – Joshua Bloch

---

> “Cada decisão técnica é um investimento: o retorno vem quando você ainda consegue mudar o sistema sem medo.”  
> — *Martin Fowler*
