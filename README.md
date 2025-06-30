# LibraryAPI 📚

API RESTful para gerenciar uma biblioteca (livros, autores, usuários e clientes) usando Java com Spring Boot e Docker.

---

## 🧩 Funcionalidades

- **Autores**
  - CRUD (criar, ler, atualizar, excluir)
- **Livros**
  - CRUD de livros (título, autor, gênero, data de publicação, estado, etc.)
- **Usuários**
  - Criar usuário.
- **Clientes**
  - Criar cliente.

---

## ⚙️ Tecnologias utilizadas

- Java 17
- Spring Boot
- Maven
- Banco de dados (configurável: H2, MySQL, PostgreSQL, etc.)
- Docker (containers manuais)
- JSON Web Tokens (JWT)
- Lombok, MapStruct, Swagger

---

## 🔧 Como rodar localmente

1. Clone o repositório:
   ```bash
   git clone https://github.com/MatheusZago/libraryapi.git
   cd libraryapi
   ```

2. Suba o banco de dados com Docker manualmente (exemplo com PostgreSQL):
   ```bash
   docker run --name library-db -e POSTGRES_DB=library -e POSTGRES_USER=seu_user -e POSTGRES_PASSWORD=sua_senha -p 5432:5432 -d --network library-network postgres:16.3
   ```

3. Configure variáveis de ambiente ou `application.yml`:
   ```
   DATASOURCE_URL=jdbc:...
   DATASOURCE_USERNAME:=...
   DATASOURCE_PASSWORD=...
   ```

4. Build e execute:
   ```bash
   mvn clean install
   (exemplo com postgres)
   docker run --name libraryapi -p 8080:8080 -p 9090:9090 -e DATASOURCE_URL=jdbc:postgresql://localhost:5432/library -e DATASOURCE_USERNAME=postgres -e DATASOURCE_PASSWORD=postgres matheusluizago/libraryapi
   ```
   A API estará disponível em `http://localhost:8080`.

5. (Opcional) Acesse a UI do Swagger/OpenAPI em `http://localhost:8080/swagger-ui.html`

---

## 🛠️ Endpoints principais

### Autores (`/authors`)
- `POST /authors` – criar autor  
- `GET /authors/{id}` – buscar por ID  
- `GET /authors/status?(parametros)` – buscar por parâmetros  
- `PUT /authors/{id}` – atualizar  
- `DELETE /authors/{id}` – excluir  

### Livros (`/books`)
- `POST /books/registra` – registrar livro  
- `GET /books/{id}` – buscar por ID  
- `GET /books?(parametros)` – buscar por filtros  
- `PUT /books/{id}` – atualizar  
- `DELETE /books/{id}` – excluir  

### Usuários (`/users`)
- `POST /users` – registrar usuário  

### Clientes (`/clients`)
- `POST /clients` – registrar cliente  

---

## 🤝 Como contribuir

Contribuições são bem-vindas!  
- Veja o `CONTRIBUTING.md`
- Abra issues e PRs com testes e documentação
- Mantenha o padrão de código

---

## 🧑‍💻 Autores e colaboradores

- Matheus Zago *(mantenedor)*

---

## 📄 Licença

Este projeto está licenciado sob a **[sua licença]** – veja o arquivo `LICENSE` para mais detalhes.