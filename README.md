# 🛡️ Auth API - Spring Boot com JWT

API de autenticação com Java e Spring Boot utilizando JWT, refresh tokens, controle por roles (USER/ADMIN),
Swagger para documentação, testes integrados e arquitetura hexagonal.

---

## ✅ Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Security
- JSON Web Token (JWT)
- PostgreSQL (via Docker)
- Spring Data JPA
- JUnit 5 + Testcontainers
- Swagger OpenAPI (springdoc)
- Arquitetura Hexagonal (Ports & Adapters)

---

## 📦 Estrutura do Projeto

```
com.example.auth
├── adapter
│ ├── in
│ │ ├── domain
│ │ └── web # Controllers REST
│ └── out
│ ├── entity # Entidades JPA
│ ├── repository # Repositórios JPA
│ ├── Adapter # Adaptadores de persistência
├── application
│ ├── core
│ │ ├── domain # Modelos de domínio
│ │ └── usecase # Casos de uso
│ └── port
│ ├── in # Portas de entrada (interfaces de uso)
│ └── out # Portas de saída (interfaces de persistência)
├── config # Configurações do Spring, Swagger e JWT
├── resources
│ └── application.yml
└── test # Testes automatizados
```

---

## ⚙️ Executando o Projeto

### Pré-requisitos

- Java 17
- Maven 3.8+
- Docker + Docker Compose

### 1. Subir banco de dados PostgreSQL

```bash
docker-compose up -d
```

### 2. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

API: http://localhost:8080  
Swagger: http://localhost:8080/swagger-ui.html

### 3. Executar os testes

```bash
./mvnw test
```

---

## 🔐 Endpoints

### 🔸 Autenticação

| Método | Endpoint             | Descrição                    |
|--------|----------------------|------------------------------|
| POST   | `/api/auth/register` | Registrar novo usuário       |
| POST   | `/api/auth/login`    | Fazer login (JWT + refresh)  |
| POST   | `/api/auth/refresh`  | Gerar novo JWT via refresh   |

### 🔸 Usuário autenticado

| Método | Endpoint        | Descrição                    |
|--------|------------------|------------------------------|
| GET    | `/api/users/me` | Dados do usuário logado      |

### 🔸 Administrativo (requer ROLE_ADMIN)

| Método | Endpoint              | Descrição               |
|--------|------------------------|-------------------------|
| GET    | `/api/admin/users`     | Lista todos os usuários |

---

## 🛠️ Exemplo de application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/authdb
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: chave-super-secreta-de-no-minimo-32-bytes-123456789
  expiration: 3600000
  refresh-expiration: 86400000
```