[33mcdfbed6[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mdevelop[m[33m, [m[1;31morigin/develop[m[33m)[m fix(material): Corrige injeção de dependência no MaterialController
[33m51eb741[m feat(disciplina): Implementa endpoints para detalhe e download
[33m57080b3[m refactor(material): Aplica camada de segurança no CRUD de Materiais
[33m674711a[m feat(disciplina): Adiciona endpoint para a visão detalhada da disciplina
[33m6f5473e[m refactor(disciplina): Aplica camada de segurança no CRUD
[33m915bc5b[m test(dashboard): Adiciona teste unitário para o serviço de materiais recentes
[33m4efb9e3[m feat(dashboard): Adiciona endpoint para buscar materiais recentes
[33m6c393bb[m test(dashboard): Adiciona teste unitário para o serviço de estatísticas
[33med5fa9c[m feat(dashboard): Adiciona endpoint para buscar estatísticas
[33meff628f[m test(usuario): Adiciona teste unitário para BuscarUsuarioLogadoService
[33mea474d3[m feat(usuario): Adiciona endpoint para buscar dados do usuário logado A implementação inclui: - **DTO de Resposta (`UsuarioDetalhesResponse`):** Garante que apenas dados públicos e seguros sejam expostos pela API, - **Caso de Uso (`BuscarUsuarioLogadoUseCase`):** Define um contrato claro na camada de aplicação para a lógica de negócio, - **Implementação do Serviço:** Utiliza o `SecurityContextHolder` do Spring Security para obter de forma segura o principal do usuário autenticado a partir do token JWT. - **Controller (`UsuarioController`):** Atua como a porta de entrada da API, com a responsabilidade única de orquestrar a requisição e a resposta HTTP, delegando toda a lógica para a camada de aplicação.
[33mc6cf6b3[m docs: Adicionado CHANGELOG.md para registrar o histórico do projeto
[33ma018cd8[m Merge remote-tracking branch 'origin/develop' into develop
[33mf9272ae[m docs: Adicionado guia completo da API (API_GUIDE.md)
[33me0a8017[m Merge branch 'main' into develop
[33mf06a1db[m feat(infra): Dockerizar a aplicação Spring Boot
[33m316ed31[m feat(infra): Migrar persistência de H2 para PostgreSQL
[33m15dd4ef[m feat(material): Implementar listagem e deleção de materiais
[33m96186da[m feat: Implementar funcionalidade de upload de materiais de estudo
[33m9745f8d[m test(resumo): Adicionar testes unitários para a camada de serviço de Resumo
[33m613a655[m refactor(disciplina): Alinhar funcionalidade com a arquitetura de segurança e adicionar testes
[33mb580317[m refactor(disciplina): Alinhar funcionalidade com a arquitetura de segurança e adicionar testes
[33m3fb86cb[m refactor(disciplina): Alinhar funcionalidade com a arquitetura de segurança multiusuário
[33mbc4bda9[m feat: Implementar autenticação e autorização com Spring Security e JWT
[33m1f2d4ba[m feat: Implementado a funcionalidade de cadastro de usuário com segurança e criptografia
[33m8d1e37e[m feat: Implementar camada de persistência JPA para Usuario
[33m3b5e481[m refactor: Implementar tratamento de exceções global com @ControllerAdvice
[33m075e7d5[m feat: Finalizado API REST CRUD para Resumo
[33md15c361[m feat: Refatorações
[33md04a3b6[m feat: adiciona endpoints de criação e listagem de resumos + refatorações na camada de serviço
[33m99841b0[m feat: Finalizar API REST CRUD para Disciplina e refatorar camada de serviço
[33maa1aee8[m feat: Implementado o adaptador JPA para ResumoRepository
[33m2fac557[m feat: Adicionar persistência JPA para a entidade Resumo
[33md03edbd[m feat(persistence): implementar adaptador JPA para Disciplina
[33m13b22ce[m feat: Implementar listagem de resumos e base da persistência JPA
[33m7815e4a[m feat: Adicionar funcionalidade de listar resumos com filtros e testes
[33m2b10dc7[m feat: Implementa listagem de disciplinas com testes e Javadoc
[33mba168ea[m feat: Implementa caso de uso para criar disciplina
[33mbbda8ff[m refactor: adiciona validações no modelo Resumo, ajusta CriarResumoService e na classe Validador.java, e ResumoTest.java
[33me99a3ad[m feat: adiciona exceções customizadas CampoVazioException e CampoNuloException
[33m75bd6b2[m fix: ajusta testes do CriarResumoService para lidar com exceções customizadas do core
[33m9f711c8[m chore(java): atualiza projeto para Java 17 e define encoding UTF-8 - Atualiza a configuração do Gradle para usar Java 17 (source e target compatibility). - Garante compatibilidade com os testes utilizando JUnit 5 e Mockito.
[33mad6780c[m "chore(test): adiciona Mockito para testes com simulação no JUnit 5"
[33ma26c7d6[m feat: adiciona DisciplinaException e documenta CriarResumoService com JavaDoc
[33m01872e1[m chore: remove arquivos temporários e adiciona .gitignore
[33m57dd1c7[m feat: implementar CriarResumoService com persistência e validação básica
[33mb79c858[m "feat: adiciona interface CriarResumoUseCase com método execute para criação de resumo"
[33m2998b06[m feat: cria DTO CriarResumoCommand com campos e construtor imutável
[33mdcc1f2b[m chore: Reaalizando a aplicação dos comentarios nas classes PdfDocumentoTest e ResumoTeste
[33me6d8a03[m chore: remove arquivos ignorados do versionamento
[33m391a4f8[m fix: reaplicando .gitignore corretamente
[33m542a4b8[m chore: remove arquivos ignorados do versionamento
[33m55475f2[m test: adiciona testes unitários para Resumo com validações completas de campos obrigatórios
[33mdda998c[m test: adiciona testes unitários para classe Resumo com validações completas de campos obrigatórios
[33m2cc2fb1[m feat: adiciona entidade PdfDocumento com validações e testes unitários
[33md4e96d5[m chore: remove arquivos ignorados
[33mfe95184[m feat: adiciona entidade PdfDocumento com atributos básicos
[33m63cec2a[m feat(core): criar entidade Usuario imutável com atributos e getters
[33md5df642[m[33m ([m[1;33mtag: [m[1;33mv1.0.0-backend[m[33m, [m[1;32mmain[m[33m)[m chore: remove PdfocusApplicationTest do projeto
[33mc9706a9[m chore: remove PdfocusApplicationTest do projeto
[33m93ab70c[m remove arquivos deletados localmente
[33m0e5663b[m chore: remove arquivos ignorados do versionamento
[33m4dccb5f[m commit teste
