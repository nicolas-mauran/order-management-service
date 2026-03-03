# Repository Structure

This layout is optimized for tactical DDD and hexagonal architecture.

```text
src/main/java/com/example/ordermanagement
|-- domain
|   |-- model              # Order aggregate, enums, value objects
|   `-- exception          # Domain-specific exceptions
|-- application
|   |-- port
|   |   |-- in             # Input contracts (use case interfaces)
|   |   `-- out            # Output contracts (repositories, gateways)
|   `-- usecase            # Use case implementations
|-- adapters
|   |-- in
|   |   `-- web            # REST controllers, request/response DTOs
|   `-- out
|       `-- persistence    # JPA entities, Spring Data repos, mappers
`-- config                 # Spring beans and cross-cutting configuration
```

## Frontend Layout

```text
frontend/
|-- src/
|   |-- api/               # HTTP client calls to backend
|   |-- App.tsx            # Main screen and orchestration
|   |-- main.tsx           # Frontend entrypoint
|   |-- styles.css         # UI styling and responsive behavior
|   `-- types.ts           # Frontend domain types
|-- index.html
|-- package.json
|-- tsconfig.json
`-- vite.config.ts
```

## Test Layout

```text
src/test/java/com/example/ordermanagement
|-- domain          # Fast unit tests for invariants
|-- web             # Controller tests
|-- integration     # Testcontainers + full stack tests
`-- architecture    # Optional ArchUnit tests
```

## Documentation Layout

```text
docs/
|-- adr/                    # Architecture Decision Records
|-- dod.md                  # Definition of Done
|-- frontend-*.md           # Frontend discovery, epic, and sprint artifacts
|-- implementation-plan.md  # Sprint-based delivery plan
`-- repository-structure.md # This document
```

## Why this split?

- Keeps business logic independent from frameworks.
- Makes use cases explicit and testable.
- Limits accidental coupling between REST, persistence, and domain.
- Communicates senior-level code organization in a recruiter-friendly way.
