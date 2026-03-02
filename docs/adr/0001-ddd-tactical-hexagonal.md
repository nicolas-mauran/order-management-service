# ADR-0001: Tactical DDD + Hexagonal Architecture

- Status: Accepted
- Date: 2026-03-02

## Context

The project must be:

- Small enough to remain readable
- Strong enough to demonstrate senior engineering practices
- Structured to prevent business rules from drifting into framework code

## Decision

Adopt tactical DDD with a hexagonal architecture:

- Rich domain entity (`Order`) owning invariants and state transitions
- Explicit use cases in `application` layer
- Inbound and outbound ports separating policy from technology
- Spring Boot used in adapters/config only, not in domain

## Consequences

Positive:

- High testability of business logic without Spring context
- Clear boundaries between API, use cases, and persistence
- Better maintainability and easier onboarding

Tradeoffs:

- More files than a CRUD-only layered app
- Requires discipline in mapping between layers

## Alternatives Considered

1. Classic layered Spring architecture (`controller -> service -> repository`)
- Rejected: too easy to create anemic domain and framework-coupled business logic.

2. Full strategic DDD with multiple contexts and domain events
- Rejected: overkill for pedagogical scope.

## Notes

This project intentionally applies DDD where it adds value and avoids ceremonial complexity.

