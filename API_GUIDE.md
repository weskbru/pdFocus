# **Guia da API - pdFocus v1.0**

Bem-vindo à documentação da API do pdFocus. Esta API permite gerenciar todos os recursos da aplicação, como usuários, disciplinas, resumos e materiais de estudo.

**URL Base:** `http://localhost:8080`

---
## **Autenticação**

A API utiliza Tokens JWT (JSON Web Tokens) para autenticação. Todas as requisições para endpoints protegidos devem incluir um cabeçalho `Authorization` com o token Bearer.

**Formato do Cabeçalho:**

Authorization: Bearer <seu_token_jwt_aqui>


Para obter um token, você deve primeiro se registrar e depois fazer login.

---
## **1. Endpoints de Autenticação (`/auth`)**

Estes endpoints são públicos e não requerem autenticação.

### **1.1 Registrar um Novo Usuário**
* **Endpoint:** `POST /auth/register`
* **Descrição:** Cria um novo usuário no sistema.
* **Corpo da Requisição (Request Body):** `application/json`
    ```json
    {
      "nome": "Nome Completo do Usuario",
      "email": "usuario@email.com",
      "senha": "umaSenhaForte123"
    }
    ```
* **Resposta de Sucesso (201 Created):**
    ```json
    {
        "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
        "nome": "Nome Completo do Usuario",
        "email": "usuario@email.com"
    }
    ```
* **Respostas de Erro:**
    * `409 Conflict`: Se o e-mail já estiver cadastrado.

### **1.2 Autenticar um Usuário (Login)**
* **Endpoint:** `POST /auth/login`
* **Descrição:** Autentica um usuário e retorna um token JWT.
* **Corpo da Requisição (Request Body):** `application/json`
    ```json
    {
      "email": "usuario@email.com",
      "senha": "umaSenhaForte123"
    }
    ```
* **Resposta de Sucesso (200 OK):**
    ```json
    {
        "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ..."
    }
    ```
* **Respostas de Erro:**
    * `401 Unauthorized`: Se as credenciais (e-mail ou senha) forem inválidas.

---
## **2. Endpoints de Disciplinas (`/disciplinas`)**

Todos os endpoints abaixo são protegidos e requerem um token de autenticação.

### **2.1 Listar Disciplinas do Usuário**
* **Endpoint:** `GET /disciplinas`
* **Descrição:** Retorna uma lista de todas as disciplinas pertencentes ao usuário autenticado.

### **2.2 Criar uma Nova Disciplina**
* **Endpoint:** `POST /disciplinas`
* **Corpo da Requisição:** `application/json`
    ```json
    {
      "nome": "Nome da Nova Disciplina",
      "descricao": "Descrição opcional da disciplina."
    }
    ```

### **2.3 Obter uma Disciplina por ID**
* **Endpoint:** `GET /disciplinas/{id}`

### **2.4 Atualizar uma Disciplina**
* **Endpoint:** `PUT /disciplinas/{id}`
* **Corpo da Requisição:** `application/json`
    ```json
    {
      "nome": "Nome Atualizado da Disciplina",
      "descricao": "Descrição atualizada."
    }
    ```

### **2.5 Deletar uma Disciplina**
* **Endpoint:** `DELETE /disciplinas/{id}`

---
## **3. Endpoints de Resumos (`/resumos`)**

Todos os endpoints abaixo são protegidos.

### **3.1 Listar Resumos do Usuário**
* **Endpoint:** `GET /resumos`
* **Descrição:** Retorna uma lista de todos os resumos pertencentes ao usuário autenticado.

### **3.2 Criar um Novo Resumo**
* **Endpoint:** `POST /resumos`
* **Corpo da Requisição:** `application/json`
    ```json
    {
      "disciplinaId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
      "titulo": "Título do Novo Resumo",
      "conteudo": "Conteúdo do resumo..."
    }
    ```

### **3.3 Obter um Resumo por ID**
* **Endpoint:** `GET /resumos/{id}`

### **3.4 Atualizar um Resumo**
* **Endpoint:** `PUT /resumos/{id}`
* **Corpo da Requisição:** `application/json`
    ```json
    {
      "titulo": "Título Atualizado do Resumo",
      "conteudo": "Conteúdo atualizado..."
    }
    ```

### **3.5 Deletar um Resumo**
* **Endpoint:** `DELETE /resumos/{id}`

---
## **4. Endpoints de Materiais (`/materiais`)**

Todos os endpoints abaixo são protegidos.

### **4.1 Fazer Upload de um Material**
* **Endpoint:** `POST /materiais/{disciplinaId}/upload`
* **Descrição:** Faz o upload de um arquivo e o associa a uma disciplina.
* **Corpo da Requisição:** `multipart/form-data`
    * **Chave (Key):** `ficheiro`
    * **Valor (Value):** O arquivo a ser enviado (ex: um PDF).

### **4.2 Listar Materiais de uma Disciplina**
* **Endpoint:** `GET /materiais`
* **Parâmetro de URL (Query Param):** `disciplinaId` (obrigatório)
    * **Exemplo:** `GET /materiais?disciplinaId=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`

### **4.3 Deletar um Material**
* **Endpoint:** `DELETE /materiais/{id}`
