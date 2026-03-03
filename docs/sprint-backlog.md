# Sprint Backlog

Last update: 2026-03-03

## Sprint 1 - Create + Get (Done)

Goal: deliver first vertical slice with rich domain rules and REST endpoints.

Committed items:

- `POST /orders` create order
- `GET /orders/{id}` get one order
- Domain invariants and transitions
- Unit tests for domain
- Web tests for create/get

Outcome:

- Goal reached
- Delivered in commit `3126bd0`

## Sprint 2 - List + Cancel + Persistence (Done)

Goal: move from in-memory to PostgreSQL and extend order lifecycle.

Committed items:

- PostgreSQL adapter (JPA + Spring Data)
- `GET /orders` list orders
- `POST /orders/{id}/cancel` cancel order
- `OrderNotFoundException` mapped to 404
- Integration tests with Testcontainers

Outcome:

- Goal reached
- Delivered on `main` in commit `5fa5bba`

## Sprint 3 - Fill + Quality Gates + Packaging (Done)

Goal: complete lifecycle and harden build and runtime packaging.

Committed items:

- `POST /orders/{id}/fill` fill order
- Unit, web, and integration tests for fill workflow
- CI on `mvn clean verify`
- Checkstyle + PMD + SpotBugs in Maven
- Dockerfile + compose service for app
- Docs updates and session logs

Outcome:

- Goal reached
- Delivered on `main` in commits `5fa5bba`, `1e1f8c4`, `4107b5b`, `6d05b47`

## Next Sprint Candidates (Not Started)

- Pagination/sorting for `GET /orders`
- Idempotency keys on command endpoints
- Authentication/authorization
- Container image publish in CI
- Frontend order desk MVP

## Sprint 4 - Frontend Order Desk MVP (Planned)

Goal: enable users to create and monitor orders via a web UI.

Epic and plan:

- Epic: `docs/frontend-epic-orders-ui.md`
- Sprint plan: `docs/sprint-4-frontend-plan.md`

Planned items:

- FE-01 view order list
- FE-02 create MARKET order
- FE-03 create LIMIT order
- FE-04 cancel `NEW` order
- FE-05 fill `NEW` order
- FE-06 loading/empty/error/success UX states

Entry criteria:

- Client questionnaire completed:
  - `docs/frontend-client-questionnaire.md`
- Story scope agreed:
  - `docs/frontend-backlog-initial.md`

Exit criteria:

- UI integrated with existing backend API
- MVP frontend acceptance criteria validated in review
- Committed scope from sprint plan completed
