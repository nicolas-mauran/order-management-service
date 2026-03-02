# Definition Of Done

A story is done only when all criteria below are met.

## Quality

- All tests are green in local and CI (`mvn verify`)
- Line coverage is at least `80%` (JaCoCo)
- No critical/high findings from static analysis tools
- No Spring dependency in `domain` package

## Architecture

- Business invariants are implemented in domain entities/value objects
- Use cases are explicit (`application/usecase`)
- Data access is behind outbound ports (`application/port/out`)
- Web layer uses DTOs, not persistence entities

## Delivery

- PR reviewed and approved
- Conventional Commit title used
- API contract updated (OpenAPI)
- Documentation updated when behavior changes

## Operability

- Actuator health endpoint works
- Service runs with local PostgreSQL (Docker Compose)
- Integration tests pass with Testcontainers

