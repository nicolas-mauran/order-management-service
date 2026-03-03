# Order Management Service
## A Spring Boot 3 / Java 21 DDD-based REST API

[![CI](https://github.com/your-org/order-management-service/actions/workflows/ci.yml/badge.svg)](https://github.com/your-org/order-management-service/actions/workflows/ci.yml)
[![Java 21](https://img.shields.io/badge/Java-21-007396)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2%2B-6DB33F)](https://spring.io/projects/spring-boot)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-blue)](./docs/adr/0001-ddd-tactical-hexagonal.md)

Order Management Service is a pedagogical but production-minded API for fictive equity orders.
It is designed to demonstrate senior-level engineering practices with clear tradeoffs:

- Tactical DDD (rich domain model, explicit invariants)
- Clean/Hexagonal architecture (ports and adapters)
- TDD-first mindset
- CI quality gates (coverage and static analysis)
- Operational readiness (Actuator, OpenAPI, containerized runtime)

## 1. Functional Scope

Core aggregate: `Order`

- Create order: `BUY`/`SELL`, `MARKET`/`LIMIT`
- Get one order
- List orders
- Cancel order (only from `NEW`)
- Fill order (simulated execution, only from `NEW`)

Lifecycle statuses:

- `NEW`
- `FILLED`
- `CANCELLED`
- `REJECTED`

Business invariants are owned by the domain model:

- `quantity > 0`
- `symbol` is required and normalized to uppercase
- `LIMIT` requires `limitPrice > 0`
- `MARKET` requires `limitPrice == null`
- `cancel()` and `fill()` only allowed from `NEW`

## 2. Architecture

- `domain`: Pure Java business model (no Spring dependency)
- `application`: Use cases + inbound/outbound ports
- `adapters/in/web`: REST controllers and API DTOs
- `adapters/out/persistence`: JPA entities, mappers, repository adapters
- `config`: Spring wiring and bean configuration

This keeps policy (business rules) isolated from mechanism (framework and storage).

## 3. Repository Layout

```text
.
|-- .github/
|   `-- workflows/
|       `-- ci.yml
|-- docs/
|   |-- adr/
|   |   `-- 0001-ddd-tactical-hexagonal.md
|   |-- dod.md
|   |-- implementation-plan.md
|   `-- repository-structure.md
|-- src/
|   |-- main/
|   |   |-- java/com/example/ordermanagement/
|   |   |   |-- adapters/
|   |   |   |   |-- in/web/
|   |   |   |   `-- out/persistence/
|   |   |   |-- application/
|   |   |   |   |-- port/in/
|   |   |   |   |-- port/out/
|   |   |   |   `-- usecase/
|   |   |   |-- config/
|   |   |   |-- domain/
|   |   |   `-- OrderManagementApplication.java
|   |   `-- resources/
|   |       `-- application.yml
|   `-- test/
|       |-- java/com/example/ordermanagement/
|       |   |-- architecture/
|       |   |-- domain/
|       |   |-- integration/
|       |   `-- web/
|       `-- resources/
|-- .gitignore
`-- pom.xml
```

More details: [repository-structure.md](./docs/repository-structure.md)

## 4. API Snapshot (Sprint Target)

| Endpoint | Description | Status |
|---|---|---|
| `POST /orders` | Create new order | Implemented |
| `GET /orders/{id}` | Get order detail | Implemented |
| `GET /orders` | List orders | Implemented |
| `POST /orders/{id}/cancel` | Cancel order | Implemented |
| `POST /orders/{id}/fill` | Simulate fill | Implemented |

OpenAPI UI: `/swagger-ui/index.html`  
Raw OpenAPI spec: `/v3/api-docs`

## 5. Engineering Standards

- Java 21, Spring Boot 3.2+
- Maven build with reproducible CI
- JUnit 5 + AssertJ + Testcontainers (PostgreSQL)
- JaCoCo coverage gate: `>= 80%`
- Checkstyle + PMD + SpotBugs (and optional PIT)
- Conventional Commits and mandatory PR review

Definition of Done: [docs/dod.md](./docs/dod.md)

## 6. Delivery Plan (3 Mini Sprints)

Detailed backlog and acceptance criteria are documented in:
[implementation-plan.md](./docs/implementation-plan.md)

Execution tracking artifacts:

- [product-backlog.md](./docs/product-backlog.md)
- [sprint-backlog.md](./docs/sprint-backlog.md)
- [frontend-client-questionnaire.md](./docs/frontend-client-questionnaire.md)
- [frontend-backlog-initial.md](./docs/frontend-backlog-initial.md)

## 7. Quick Start

```bash
docker compose up -d postgres
mvn clean verify
mvn spring-boot:run
```

Stop and cleanup:

```bash
docker compose down
```

Reset local database volume:

```bash
docker compose down -v
```

Health endpoint:

```bash
curl http://localhost:8080/actuator/health
```

Default local DB settings:

- Host: `localhost`
- Port: `5432`
- Database: `orders`
- User: `orders`
- Password: `orders`


## 8. ADRs

- [ADR-0001: Tactical DDD + Hexagonal Architecture](./docs/adr/0001-ddd-tactical-hexagonal.md)

## 9. Why This Project Is Interview-Ready

- Shows architecture decisions with explicit rationale (ADR)
- Demonstrates domain modeling over anemic CRUD services
- Includes test strategy from domain to integration level
- Balances rigor and simplicity (no accidental over-engineering)
