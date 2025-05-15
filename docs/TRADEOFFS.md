# Trade-offs Técnicos – PDFocus

## 💡 O que é um Trade-off?

Toda decisão de arquitetura ou tecnologia implica em **ganhos** e **custos**. Este documento registra essas escolhas para que qualquer pessoa (inclusive eu no futuro) entenda **por que fizemos do jeito que fizemos**.

---

## ⚖️ Trade-offs Relevantes do Projeto

### 1. ❌ Não usar Spring Boot no MVP

- **Benefício:** Inicialização mais rápida, sem dependência de container, controle total sobre as dependências.
- **Custo:** Mais código “manual” (injeção de dependência, configuração de rotas), menos comunidade ajudando com problemas triviais.
- **Mitigação:** A arquitetura permite plugar Spring futuramente sem reescrever a camada de domínio.

---

### 2. ✅ Separar `domain` e `application`

- **Benefício:** Código mais coeso e testável. Casos de uso separados de lógica de infraestrutura.
- **Custo:** Mais estrutura inicial, exige conhecimento de arquitetura.
- **Mitigação:** Comentários, diagrama no README e documentação clara ajudam no onboarding.

---

### 3. ❌ Ignorar persistência no primeiro ciclo

- **Benefício:** Permite focar totalmente na lógica de extração e resumo. Menor tempo para entrega do MVP.
- **Custo:** Algumas funcionalidades ficam incompletas (ex: salvar histórico, login persistente).
- **Mitigação:** Interfaces de repositório já existem — é só implementar depois com H2 ou SQLite.

---

### 4. ✅ Escolher Gradle Kotlin DSL

- **Benefício:** Sintaxe moderna, tipada, melhor suporte em IDEs.
- **Custo:** Curva de aprendizado maior pra quem só conhece Groovy.
- **Mitigação:** Projeto documentado, exemplos simples no `build.gradle.kts`.

---

### 5. ❌ Não usar Docker no início

- **Benefício:** Ambiente local roda mais rápido, menos dependências externas.
- **Custo:** Mais difícil compartilhar a aplicação com outras pessoas.
- **Mitigação:** Planejado para fase 2, junto com CI/CD.

---

### 6. ❌ Sem autenticação real no início

- **Benefício:** MVP mais rápido. Sem dependência de JWT, OAuth, etc.
- **Custo:** Sem controle de acesso real.
- **Mitigação:** Interface de autenticação já isolada. Pode ser plugada depois.

---

## 🤖 Filosofia de Decisão

- Primeiro, **faça funcionar**.
- Depois, **torne limpo e escalável**.
- Só então, **otimize com ferramentas e frameworks.**

---

## 🧭 Pronto para evoluir

As decisões acima **não são finais**. Elas foram feitas com base no estado atual do projeto e podem mudar conforme o produto evolui. Isso é intencional.

A arquitetura foi feita para **absorver mudanças com baixo impacto**, exatamente para que essas trocas (ex: adicionar Spring, persistência real, autenticação segura) sejam naturais.

