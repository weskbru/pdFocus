# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/)  
e este projeto segue o [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Unreleased]
### Added
- *(Nenhuma mudança ainda)*

---

## [1.0.0] - 2025-08-10
Esta é a primeira versão estável do backend do **pdFocus**, representando a conclusão do MVP.  
A aplicação está funcional, segura e pronta para ser implantada.

### Added
#### Funcionalidade de Autenticação e Autorização
- Endpoint `POST /auth/register` para cadastro de novos usuários com criptografia de senha (BCrypt).
- Endpoint `POST /auth/login` para autenticação e geração de Tokens JWT.
- Filtro de segurança (`JwtAuthenticationFilter`) para validar tokens em todas as requisições protegidas.

#### Funcionalidade de Disciplinas
- API RESTful com CRUD completo e seguro (`POST`, `GET`, `PUT`, `DELETE`) em `/disciplinas` para o usuário autenticado.

#### Funcionalidade de Resumos
- API RESTful com CRUD completo e seguro (`POST`, `GET`, `PUT`, `DELETE`) em `/resumos`.

#### Funcionalidade de Materiais
- Endpoint `POST /materiais/{disciplinaId}/upload` para upload de arquivos.
- Endpoints `GET` e `DELETE` para listar e apagar materiais.

#### Infraestrutura de Persistência
- Configuração completa com Spring Data JPA para persistência de dados.
- Migração de banco de dados de H2 em memória para PostgreSQL.

#### Infraestrutura de Armazenamento
- Implementado sistema de armazenamento de arquivos físicos no disco local, desacoplado da lógica de negócio através da `MaterialStoragePort`.

#### Infraestrutura de Deploy
- Criado **Dockerfile** com *multi-stage build* para containerizar a aplicação, deixando-a pronta para o deploy.

#### Qualidade e Testes
- Cobertura de testes unitários para todas as classes de serviço (`...Service`) e mappers (`...Mapper`) com JUnit 5 e Mockito.

#### Documentação
- Criado `README.md` completo com a visão geral do projeto, arquitetura e instruções de execução.
- Criado `API_GUIDE.md` com a documentação detalhada de todos os endpoints da API.

---

### Changed
- **Arquitetura:** A camada *application* foi refatorada do padrão *Package by Layer* para *Package by Feature*, melhorando a coesão e a manutenibilidade.
- **Segurança:** Todas as funcionalidades de Disciplina e Resumo foram refatoradas para serem seguras e baseadas no `usuarioId` obtido do token de autenticação, em vez de parâmetros de URL.

---

### Fixed
- Resolvidos múltiplos erros de compilação e de inicialização relacionados a dependências, configurações do Spring Security e conexão com o banco de dados no ambiente Docker.
