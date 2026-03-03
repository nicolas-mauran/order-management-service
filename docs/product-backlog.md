# Product Backlog

Last update: 2026-03-03

## Prioritized Items

| ID | Epic | User Story | Priority | Status |
|---|---|---|---|---|
| PB-01 | Order lifecycle | As a trader, I can create an order with type, side, quantity, and optional limit price. | High | Done |
| PB-02 | Order lifecycle | As a trader, I can fetch one order by id. | High | Done |
| PB-03 | Order lifecycle | As a trader, I can list all orders. | High | Done |
| PB-04 | Order lifecycle | As a trader, I can cancel an order only from `NEW`. | High | Done |
| PB-05 | Order lifecycle | As a trader, I can fill an order only from `NEW`. | High | Done |
| PB-06 | Persistence | As a developer, I store orders in PostgreSQL through an outbound port. | High | Done |
| PB-07 | Testing | As a developer, I validate API + persistence with Testcontainers integration tests. | High | Done |
| PB-08 | CI quality | As a maintainer, CI runs `mvn clean verify` with tests and quality gates. | High | Done |
| PB-09 | Packaging | As a user, I can run app + postgres with Docker Compose. | Medium | Done |
| PB-10 | Operability | As an operator, I can use Actuator health and OpenAPI docs. | Medium | Done |
| PB-11 | API evolution | As an API consumer, I can list orders with pagination and sorting query params. | Medium | Todo |
| PB-12 | Reliability | As an API consumer, create/cancel/fill endpoints support idempotency keys. | Medium | Todo |
| PB-13 | Security | As an operator, endpoints are protected with authentication and basic authorization. | Medium | Todo |
| PB-14 | Delivery | As a maintainer, image is published by CI with tag strategy. | Low | Todo |
| PB-15 | Frontend | As a trader, I can view orders in a web interface. | High | Todo |
| PB-16 | Frontend | As a trader, I can create MARKET and LIMIT orders from a web form. | High | Todo |
| PB-17 | Frontend | As a trader, I can cancel/fill NEW orders from the web interface. | High | Todo |
| PB-18 | Frontend | As a user, I get clear loading, success, empty, and error states in the UI. | Medium | Todo |
| PB-19 | Frontend quality | As a maintainer, frontend critical flows are tested in CI. | Medium | Todo |

## Definition

- `Done`: implemented, tested, and merged on `main`.
- `Todo`: not yet implemented or not committed as part of released scope.
