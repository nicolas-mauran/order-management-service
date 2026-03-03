# Session Log - 2026-03-02

## Context

- Projet: `order-management-service`
- Objectif: construire une API Spring Boot Java 21, DDD tactique, architecture hexagonale, avec trajectoire Sprint 1 -> Sprint 3.
- Contrainte explicite utilisateur: ne jamais pousser vers GitHub sans accord.

## Decisions prises

1. Repo bootstrap complet cree:
- README pro
- docs DOD / plan implementation / ADR / structure repo
- CI GitHub Actions
- `pom.xml`, `application.yml`, `docker-compose.yml`

2. Stack validee:
- Java 21
- Spring Boot 3.2.x
- Maven
- PostgreSQL
- Testcontainers

3. Docker compose retenu pour le projet:
- Oui pour dev local (Postgres)
- Pas obligatoire pour debuter le Sprint 1, mais recommande pour coherence.

4. Compat Fedora/Podman:
- `docker` absent au depart
- Podman present
- Correction du `docker-compose.yml` pour image fully-qualified:
  - `docker.io/library/postgres:16`

5. Maven/Java mismatch corrige:
- `mvn -v` utilisait Java 25 a cause de `/etc/java/maven.conf`
- Solution: exporter `JAVA_HOME=/usr/lib/jvm/java-21-temurin-jdk`

## Sprint 1 (termine)

Implementations:
- Domaine `Order` riche + invariants + transitions
- Ports/use cases `CreateOrder` + `GetOrder`
- Adapter REST:
  - `POST /orders`
  - `GET /orders/{id}`
- Gestion d erreurs API
- Tests:
  - unit domain
  - web (MockMvc)

Validation utilisateur:
- `mvn clean test` OK
- 10 tests verts

Commit:
- `3126bd0`
- message: `feat: bootstrap project and implement sprint 1 create/get vertical slice`

## Sprint 2 (en cours, code implemente non committe)

Implementations faites:
- Remplacement adapter memoire par adapter PostgreSQL JPA
- Ajout use cases:
  - `ListOrdersUseCase`
  - `CancelOrderUseCase`
- Endpoints ajoutes:
  - `GET /orders`
  - `POST /orders/{id}/cancel`
- Exception metier:
  - `OrderNotFoundException` -> 404
- Tests:
  - web tests etendus
  - integration Testcontainers ajoutee

Validation locale agent:
- `mvn test` = BUILD SUCCESS
- tests integration Testcontainers skips si environnement Docker non detecte.

## Commandes importantes relevees

Installation Maven Fedora:
```bash
sudo dnf install -y maven
```

Forcer Maven sur Java 21:
```bash
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-temurin-jdk' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
mvn -v
```

Demarrage local:
```bash
docker compose up -d postgres
JAVA_HOME=/usr/lib/jvm/java-21-temurin-jdk mvn clean verify
JAVA_HOME=/usr/lib/jvm/java-21-temurin-jdk mvn spring-boot:run
```

Healthcheck:
```bash
curl http://localhost:8080/actuator/health
```

## Notes techniques

- Message Hibernate `No JTA platform available` explique comme informatif/non bloquant.
- Warnings ByteBuddy/JDK en test identifies comme non bloquants.
- Rappel conserve: aucun push GitHub sans accord explicite utilisateur.

