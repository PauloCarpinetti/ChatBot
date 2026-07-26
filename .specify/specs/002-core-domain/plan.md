# Implementation Plan: Core Domain & Multi-Tenant Persistence

**Branch**: `002-core-domain` | **Date**: 2026-07-25 | **Spec**: [spec.md](file:///c:/Users/paulo/Desktop/projetosPortifolio/ChatBot/ChatBot/.specify/specs/002-core-domain/spec.md)

**Input**: Feature specification from `spec.md`

## Summary

This plan outlines the implementation of the relational data layer (MySQL) to support the multi-tenant architecture. This includes database schema creation via Flyway, JPA entity mapping using Lombok, and repository creation with strict isolation rules enforcing `tenant_id` on all queries.

## Technical Context

**Language/Version**: Java 17+

**Primary Dependencies**: 
- Spring Boot Starter Data JPA
- Flyway Core
- MySQL Driver
- Lombok

**Storage**: MySQL (Relational)

**Testing**: JUnit, `@SpringBootTest`

**Target Platform**: Backend API

**Project Type**: Web-service

**Constraints**: Strict `tenantId` filtering on all session and message queries. `@JsonIgnore` must be applied to the Tenant API Key to prevent accidental leakage in JSON serialization.

**Scale/Scope**: 3 core domain entities (`Tenant`, `ChatSession`, `ChatMessage`).

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Isolamento Multi-Tenant (Segurança Inegociável): Filtros por `tenantId` obrigatórios nos repositórios.
- [x] Otimizar para manutenibilidade: Uso padronizado de Flyway para versionamento de esquema e Spring Data JPA para abstração de persistência.

## Project Structure

### Documentation (this feature)

```text
.specify/specs/002-core-domain/
├── plan.md              # This file
├── spec.md              # Feature specification
└── tasks.md             # Execution tasks
```

### Source Code (repository root)

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/br/com/paulo/chatbot/domain/
│   │   │   ├── model/
│   │   │   │   ├── Tenant.java
│   │   │   │   ├── ChatSession.java
│   │   │   │   └── ChatMessage.java
│   │   │   └── repository/
│   │   │       ├── TenantRepository.java
│   │   │       ├── ChatSessionRepository.java
│   │   │       └── ChatMessageRepository.java
│   │   └── resources/
│   │       └── db/migration/
│   │           └── V1__init_schema.sql
│   └── test/
```

**Structure Decision**: Standard Spring Boot package structure within the existing monolithic backend. The domain logic is encapsulated inside `domain.model` and `domain.repository`.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| N/A       | N/A        | N/A                                 |
