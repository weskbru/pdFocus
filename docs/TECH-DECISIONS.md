# Decisões Técnicas – PDFocus

## ⚙️ Tecnologias Escolhidas

| Tecnologia         | Justificativa                                                                 |
|--------------------|-------------------------------------------------------------------------------|
| **Java 17**        | Estável, LTS, com boas features modernas (records, sealed classes, etc.)     |
| **Gradle + Kotlin DSL** | Mais seguro, expressivo e moderno que o Groovy. Facilita manutenção.           |
| **JUnit 5**        | Leve, modular, padrão do mercado para testes unitários e de integração.       |
| **JaCoCo**         | Geração de relatórios de cobertura de testes, essencial para rastreabilidade.|
| **Arquitetura Limpa** | Permite desacoplamento total de frameworks e foco em regras de negócio.        |
| **Mermaid (README)** | Visualização de arquitetura diretamente via markdown, sem ferramentas externas.|

---

## 🚫 Tecnologias Não Utilizadas (por enquanto)

| Tecnologia          | Motivo da não escolha                                                             |
|---------------------|-----------------------------------------------------------------------------------|
| **Spring Boot**     | MVP precisa ser leve. Spring adicionaria overhead e acoplamento desnecessário.   |
| **Banco de Dados Relacional** | O foco inicial é em lógica de negócios. Persistência é plugável, mas não essencial agora. |
| **ORM (ex: Hibernate)** | Abstrai demais para MVP simples. Prefiro controle total, talvez via JDBC ou mapper simples.  |
| **Docker**          | Será usado numa fase posterior (deploy), mas não no MVP local.                    |
| **Swagger/OpenAPI** | Documentação REST não é prioridade neste momento.                                |

---

## 🏗️ Estratégia de Modularização

- Divisão clara em `domain`, `application`, `infrastructure`
- Separação de pacotes por contexto (ex: usuario, pdf, resumo) para manter o monolito modular
- Interface-driven design: dependência sempre em abstrações

---

## 🔍 Critérios de Escolha

- 🔄 **Flexibilidade**: tudo pode ser substituído sem quebrar o core
- 🧪 **Testabilidade**: fácil escrever testes sem mocks pesados ou ambientes externos
- 🛠️ **MVP-first**: decisões são guiadas por entrega rápida, mas sustentável
- 🧼 **Baixo acoplamento**: o domínio não conhece infraestrutura (e nunca conhecerá)

---

## 🧱 Exemplos de Trade-offs

| Decisão                                 | Benefício                                    | Custo técnico/futuro |
|----------------------------------------|----------------------------------------------|-----------------------|
| Não usar Spring Boot                   | Leveza, controle total                       | Menos “mágica”, mais código manual |
| Evitar banco no início                 | Foco 100% na lógica do SaaS                  | Persistência será adicionada depois |
| Separar domain de application          | Escalabilidade e clareza                     | Exige disciplina no design          |
| Usar Gradle Kotlin DSL                 | Mais moderno e seguro                        | Menor base de tutoriais (vs Groovy) |

---

## 🧭 Próximos Passos Técnicos

- Adicionar camada de persistência simples (H2 ou SQLite) como plugin da infra
- Definir protocolo de comunicação externo (REST, CLI ou fila)
- Introduzir CI/CD leve com GitHub Actions
- Adicionar métricas e logs estruturados

---

## 📚 Inspirações & Fontes

- [Clean Architecture – Uncle Bob](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html)
- [12 Factor App](https://12factor.net/)
- [Hexagonal Architecture – Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Effective Java – Joshua Bloch](https://www.oreilly.com/library/view/effective-java/9780134686097/)

