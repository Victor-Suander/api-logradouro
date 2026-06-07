# API de Logradouros

REST API para gerenciamento hierárquico de endereços: UF → Cidade → Bairro → Logradouro.

---

## Motivação

Este projeto começou como uma atividade proposta pelo instrutor, com o tema de gerenciamento de endereçamento. O que era para ser um exercício acabou se tornando um projeto mais completo, onde aproveitei para aplicar na prática conceitos como testes automatizados, documentação de API, paginação e auditoria de dados.

---

## Tecnologias

- Java 21
- Spring Boot 4.0.6
- Spring Data JPA + Hibernate
- H2 (banco em memória)
- Hibernate Validator
- Lombok
- Springdoc OpenAPI (Swagger UI)
- JUnit 6 + Mockito

---

## Pré-requisitos

- Java 21 instalado
- Maven (ou usar o `mvnw` que já vem no projeto)

---

## Como rodar localmente

```bash
git clone https://github.com/Victor-Suander/api-logradouro.git
cd api-logradouro
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

O banco H2 é em memória — os dados são perdidos ao reiniciar. Para acessar o console do H2:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:apilogradourodb`
- Usuário: `sa` / Senha: *(vazia)*

[PRINT: tela do H2 Console com as tabelas criadas]

---

## Swagger

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

[PRINT: tela inicial do Swagger com os 4 grupos de endpoints expandidos]

---

## Endpoints

Todos os endpoints de listagem retornam resultados paginados. Parâmetros opcionais: `page`, `size`, `sort`.

### UF

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/ufs` | Cadastrar UF |
| GET | `/ufs` | Listar UFs paginadas |
| GET | `/ufs/{id}` | Buscar por ID |
| GET | `/ufs/sigla/{sigla}` | Buscar por sigla (case-sensitive) |
| GET | `/ufs/{id}/cidades` | Listar cidades de uma UF |
| PUT | `/ufs/{id}` | Atualizar UF |
| DELETE | `/ufs/{id}` | Excluir UF |

**POST /ufs**
```json
// Requisição
{
  "sigla": "SP",
  "nome": "São Paulo"
}

// Resposta 201
{
  "id": 1,
  "sigla": "SP",
  "nome": "São Paulo",
  "createdAt": "2026-06-07T10:00:00",
  "updatedAt": "2026-06-07T10:00:00"
}
```

**GET /ufs/1/cidades**
```json
// Resposta 200
{
  "content": [
    { "id": 1, "nome": "São Paulo", "ufId": 1, "ufSigla": "SP", "createdAt": "...", "updatedAt": "..." }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "number": 0,
  "size": 10
}
```

---

### Cidade

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/cidades` | Cadastrar cidade |
| GET | `/cidades` | Listar cidades paginadas |
| GET | `/cidades/{id}` | Buscar por ID |
| GET | `/cidades/{id}/bairros` | Listar bairros de uma cidade |
| PUT | `/cidades/{id}` | Atualizar cidade |
| DELETE | `/cidades/{id}` | Excluir cidade |

**POST /cidades**
```json
// Requisição
{
  "nome": "São Paulo",
  "ufId": 1
}

// Resposta 201
{
  "id": 1,
  "nome": "São Paulo",
  "ufId": 1,
  "ufSigla": "SP",
  "createdAt": "2026-06-07T10:00:00",
  "updatedAt": "2026-06-07T10:00:00"
}
```

---

### Bairro

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/bairros` | Cadastrar bairro |
| GET | `/bairros` | Listar bairros paginados |
| GET | `/bairros/{id}` | Buscar por ID |
| GET | `/bairros/{id}/logradouros` | Listar logradouros de um bairro |
| PUT | `/bairros/{id}` | Atualizar bairro |
| DELETE | `/bairros/{id}` | Excluir bairro |

**POST /bairros**
```json
// Requisição
{
  "nome": "Bela Vista",
  "cidadeId": 1
}

// Resposta 201
{
  "id": 1,
  "nome": "Bela Vista",
  "cidadeId": 1,
  "cidadeNome": "São Paulo",
  "createdAt": "2026-06-07T10:00:00",
  "updatedAt": "2026-06-07T10:00:00"
}
```

---

### Logradouro

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/logradouros` | Cadastrar logradouro |
| GET | `/logradouros` | Listar logradouros paginados |
| GET | `/logradouros/{id}` | Buscar por ID |
| GET | `/logradouros/cep/{cep}` | Buscar por CEP |
| PUT | `/logradouros/{id}` | Atualizar logradouro |
| DELETE | `/logradouros/{id}` | Excluir logradouro |

**POST /logradouros**
```json
// Requisição
{
  "nome": "Avenida Paulista",
  "tipo": "AVENIDA",
  "cep": "01310-100",
  "bairroId": 1
}

// Resposta 201
{
  "id": 1,
  "nome": "Avenida Paulista",
  "tipo": "AVENIDA",
  "cep": "01310-100",
  "bairroId": 1,
  "bairroNome": "Bela Vista",
  "cidadeNome": "São Paulo",
  "ufSigla": "SP",
  "createdAt": "2026-06-07T10:00:00",
  "updatedAt": "2026-06-07T10:00:00"
}
```

Tipos de logradouro aceitos: `RUA`, `AVENIDA`, `PRACA`, `TRAVESSA`, `ALAMEDA`, `ESTRADA`, `RODOVIA`, `VIELA`.

CEP aceito nos formatos `99999-999` ou `99999999`.

**GET /logradouros/cep/01310-100**
```json
// Resposta 200 — retorna o logradouro completo com o endereço hierárquico
{
  "id": 1,
  "nome": "Avenida Paulista",
  "tipo": "AVENIDA",
  "cep": "01310-100",
  "bairroId": 1,
  "bairroNome": "Bela Vista",
  "cidadeNome": "São Paulo",
  "ufSigla": "SP",
  "createdAt": "...",
  "updatedAt": "..."
}
```

[PRINT: exemplo de requisição no Swagger para POST /logradouros com resposta 201]

---

## Regras de negócio

- Não é possível excluir uma UF que tenha cidades cadastradas
- Não é possível excluir uma cidade que tenha bairros cadastrados
- Não é possível excluir um bairro que tenha logradouros cadastrados
- Sigla de UF é única e a busca por sigla é case-sensitive
- Nome de cidade e bairro não podem ser duplicados
- CEP de logradouro é único

---

## Como rodar os testes

```bash
./mvnw test
```

São 40 testes distribuídos entre:

- **Testes de validação dos DTOs** — verificam se campos inválidos retornam 400
- **Testes de regras de negócio** — verificam exceções lançadas pelos services
- **Testes do GlobalExceptionHandler** — verificam o formato das respostas de erro
- **Testes de paginação** — verificam a estrutura da resposta paginada
- **Testes dos endpoints hierárquicos** — verificam navegação entre os recursos

[PRINT: resultado do mvn test com BUILD SUCCESS e os 40 testes passando]

---

## Estrutura de pastas

```
src/
├── main/
│   ├── java/com/victors/apilogradouro/
│   │   ├── config/         # OpenApiConfig, JpaAuditingConfig
│   │   ├── controller/     # UfController, CidadeController, BairroController, LogradouroController
│   │   ├── dto/
│   │   │   ├── request/    # DTOs de entrada com validações
│   │   │   └── response/   # DTOs de saída com campos de auditoria
│   │   ├── entity/         # Uf, Cidade, Bairro, Logradouro, Auditavel, TipoLogradouro
│   │   ├── exception/      # GlobalExceptionHandler, exceções customizadas, MensagensErro
│   │   ├── repository/     # Interfaces JPA com queries derivadas
│   │   └── service/        # Interfaces e implementações dos services
│   └── resources/
│       ├── application.properties
│       └── logback-spring.xml  # Logs com rolagem diária, retenção de 7 dias
└── test/
    └── java/com/victors/apilogradouro/
        ├── controller/     # Testes com @WebMvcTest
        └── service/        # Testes com @ExtendWith(MockitoExtension.class)
```

---

## Próximos passos

- [ ] Frontend em React consumindo esta API
- [ ] Migração do H2 para PostgreSQL via Docker Compose
- [ ] Autenticação com Spring Security e JWT
- [ ] Deploy em nuvem (Railway ou Render)

---

## Autor

**Victor Suander Camargo Martins**

- GitHub: [Victor-Suander](https://github.com/Victor-Suander)
- Email: victorsuandercmartins@gmail.com
