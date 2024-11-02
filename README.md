<h1> Back End Challenge </h1>

Solução do desafio backend da [FCamara2](https://github.com/fcamarasantos/node-react-test)

## Tecnologias

- [Spring Boot](https://spring.io/projects/spring-boot)
- [MySQL](https://www.mysql.com/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [JUnit5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT](https://jwt.io/)



## Como Executar

- Clonar repositório git
```
git clone git@github.com:igorbarret0/FCamara2.git
```

- Construir o projeto:
```
./mvnw clean package
```

A API poderá ser acessada em [localhost:8888](http://localhost:8888).

## API Endpoints

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [Postman](https://www.postman.com/)

-  AUTH
```
POST /api/v1/auth/register - Criar conta
```
```
POST /api/v1/auth/login - Realizar Login
```

-  USER
```
GET /api/v1/users - Obter todos os usuários
```
```
GET /api/v1/users/{user-id} - Obter um usuario pelo ID
```
```
PATCH /api/v1/users - Atualizar dados de um usuário
```

-  BOOK

```
POST /api/v1/books - Salvar um livro
```

```
GET /api/v1/books - Obter todos os livros
```

```
GET /api/v1/book/{book-id} - Obter um livro pelo ID
```

```
PATCH /api/v1/book/{book-id} - Atualizar um livro
```

- COPY
```
POST /api/v1/copies - Gerar cópia de um livro
```

```
POST /api/v1/copies/rent/{book-id} - Alugar a cópia de um livro
```

```
GET /api/v1/copies - Obter todos as cópias
```

```
GET /api/v1/copies/{copy-id} - Obter a cópia de um livro pelo ID
```

```
POST /api/v1/copies/return - Devolver um livro alugado
```