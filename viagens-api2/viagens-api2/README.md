# ✈️ API RESTful — Agência de Viagens

API desenvolvida com **Java 17** e **Spring Boot 3.2** para gerenciar destinos de viagem,
permitindo cadastro, listagem, pesquisa, avaliação e exclusão.

---

## 🚀 Como executar

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Comandos

```bash
# Instalar dependências e compilar
mvn clean install

# Iniciar a aplicação
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

> O banco H2 em memória é criado automaticamente.
> Console disponível em `http://localhost:8080/h2-console`
> (JDBC URL: `jdbc:h2:mem:viagens-db`, usuário: `sa`, senha: em branco)

---

## 📁 Estrutura do Projeto

```
viagens-api/
├── pom.xml
└── src/
    └── main/
        ├── java/com/agencia/viagens/
        │   ├── ViagemApiApplication.java       ← Ponto de entrada
        │   ├── controller/
        │   │   └── DestinoController.java      ← Endpoints REST
        │   ├── service/
        │   │   └── DestinoService.java         ← Regras de negócio
        │   ├── repository/
        │   │   └── DestinoRepository.java      ← Acesso ao banco
        │   ├── model/
        │   │   └── Destino.java                ← Entidade JPA
        │   ├── dto/
        │   │   ├── DestinoRequestDTO.java      ← Entrada (POST)
        │   │   ├── DestinoResponseDTO.java     ← Saída (GET/PATCH)
        │   │   └── AvaliacaoDTO.java           ← Entrada (PATCH avaliar)
        │   └── exception/
        │       ├── DestinoNotFoundException.java
        │       └── GlobalExceptionHandler.java ← Erros padronizados
        └── resources/
            └── application.properties
```

---

## 📋 Endpoints

| Método   | Rota                          | Descrição                              | Status |
|----------|-------------------------------|----------------------------------------|--------|
| `POST`   | `/api/destinos`               | Cadastrar novo destino                 | 201    |
| `GET`    | `/api/destinos`               | Listar todos os destinos               | 200    |
| `GET`    | `/api/destinos/pesquisa`      | Pesquisar por nome e/ou localização    | 200    |
| `GET`    | `/api/destinos/{id}`         | Visualizar destino específico          | 200    |
| `PATCH`  | `/api/destinos/{id}/avaliar` | Avaliar destino (nota 1-10)            | 200    |
| `DELETE` | `/api/destinos/{id}`         | Excluir destino                        | 204    |

---

## 🔍 Exemplos de Requisições

### 1. Cadastrar Destino — `POST /api/destinos`

**Request body:**
```json
{
  "nome": "Paris",
  "localizacao": "França",
  "descricao": "A romântica Cidade Luz, repleta de arte, gastronomia e cultura.",
  "idioma": "Francês",
  "moeda": "Euro (EUR)"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nome": "Paris",
  "localizacao": "França",
  "descricao": "A romântica Cidade Luz, repleta de arte, gastronomia e cultura.",
  "idioma": "Francês",
  "moeda": "Euro (EUR)",
  "notaMedia": 0.0,
  "totalAvaliacoes": 0
}
```

---

### 2. Listar Destinos — `GET /api/destinos`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Paris",
    "localizacao": "França",
    "descricao": "...",
    "idioma": "Francês",
    "moeda": "Euro (EUR)",
    "notaMedia": 9.0,
    "totalAvaliacoes": 2
  }
]
```

---

### 3. Pesquisar Destinos — `GET /api/destinos/pesquisa`

Parâmetros opcionais: `nome` e `localizacao`

```
GET /api/destinos/pesquisa?nome=rio
GET /api/destinos/pesquisa?localizacao=brasil
GET /api/destinos/pesquisa?nome=paris&localizacao=franca
```

**Response (200 OK):**
```json
[
  {
    "id": 2,
    "nome": "Rio de Janeiro",
    "localizacao": "Brasil",
    "notaMedia": 8.5,
    ...
  }
]
```

---

### 4. Visualizar Destino — `GET /api/destinos/{id}`

```
GET /api/destinos/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "Paris",
  "localizacao": "França",
  "descricao": "A romântica Cidade Luz, repleta de arte, gastronomia e cultura.",
  "idioma": "Francês",
  "moeda": "Euro (EUR)",
  "notaMedia": 9.0,
  "totalAvaliacoes": 2
}
```

---

### 5. Avaliar Destino — `PATCH /api/destinos/{id}/avaliar`

**Request body:**
```json
{
  "nota": 10
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "Paris",
  "notaMedia": 9.5,
  "totalAvaliacoes": 3,
  ...
}
```

> **Fórmula da média acumulada:**
> `novaMedia = (mediaAtual × totalAnterior + novaNota) / (totalAnterior + 1)`

---

### 6. Excluir Destino — `DELETE /api/destinos/{id}`

```
DELETE /api/destinos/1
```

**Response:** `204 No Content`

---

## ⚠️ Respostas de Erro

### 404 — Destino não encontrado
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "erro": "Recurso não encontrado",
  "mensagem": "Destino com id 99 não encontrado."
}
```

### 400 — Erro de validação
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "erro": "Erro de validação",
  "campos": {
    "nome": "O nome do destino é obrigatório",
    "nota": "A nota máxima permitida é 10"
  }
}
```

---

## 🏗️ Modelo de Dados

| Campo            | Tipo    | Obrigatório | Descrição                          |
|------------------|---------|-------------|------------------------------------|
| `id`             | Long    | Auto        | Identificador único                |
| `nome`           | String  | ✅           | Nome do destino                    |
| `localizacao`    | String  | ✅           | País, cidade ou região             |
| `descricao`      | String  | ❌           | Descrição detalhada                |
| `idioma`         | String  | ❌           | Idioma principal falado            |
| `moeda`          | String  | ❌           | Moeda local                        |
| `notaMedia`      | Double  | Auto (0.0)  | Média das avaliações (0.0 – 10.0) |
| `totalAvaliacoes`| Integer | Auto (0)    | Quantidade de avaliações           |

---

## 🧪 Testes

```bash
mvn test
```

Os testes unitários cobrem:
- Cadastro de destino
- Listagem de destinos
- Visualização por id (sucesso e 404)
- Cálculo de média na 1ª avaliação
- Recálculo de média em avaliações subsequentes
- Exclusão (sucesso e 404)

---

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- Spring Validation (Bean Validation / Jakarta)
- H2 Database (in-memory)
- Lombok
- JUnit 5 + Mockito
