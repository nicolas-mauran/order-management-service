# Implementation Proposal

## Goal

Build a compact but credible Order Management API that demonstrates:

- Tactical DDD
- Hexagonal architecture
- TDD and quality gates
- CI/CD discipline

## Scope

Aggregate: `Order`

- Commands: create, cancel, fill
- Queries: get by id, list all
- Statuses: `NEW`, `FILLED`, `CANCELLED`, `REJECTED`

## Sprint Plan

## Sprint 1 - Create + Get (Vertical Slice)

Deliverables:

- Domain model (`Order`, enums, invariants)
- Domain unit tests (pure JUnit/AssertJ)
- Use case: `CreateOrderUseCase`
- In-memory repository adapter (temporary)
- API:
  - `POST /orders`
  - `GET /orders/{id}`
- Baseline CI workflow

Acceptance criteria:

- Domain rules enforced in tests
- HTTP create/get endpoints green
- CI runs tests and coverage report

## Sprint 2 - List + Cancel + Persistence

Deliverables:

- Use cases: `ListOrdersUseCase`, `CancelOrderUseCase`
- PostgreSQL adapter with JPA mapping
- Testcontainers integration tests
- API:
  - `GET /orders`
  - `POST /orders/{id}/cancel`

Acceptance criteria:

- Cancel only allowed from `NEW`
- Persistence adapter passes integration tests
- API contract documented

## Sprint 3 - Fill + Observability + Packaging

Deliverables:

- Use case: `FillOrderUseCase`
- API:
  - `POST /orders/{id}/fill`
- OpenAPI docs (`springdoc`)
- Actuator endpoints
- Dockerfile + `docker-compose.yml`
- Static analysis in CI (Checkstyle, PMD, SpotBugs)

Acceptance criteria:

- Full user journey tested end-to-end
- Quality gates enforced in CI
- Project is ready for public portfolio presentation

## Non-Goals

- Matching engine
- Multi-bounded-context orchestration
- Event-driven choreography
- Advanced pricing/risk logic

## Risk Management

- Keep model focused on one aggregate
- Introduce value objects only when signal is clear (`Symbol`, `Quantity`, `Money`)
- Avoid framework leakage into domain

