# Feature Specification: Spec 015: Backend Admin API & Integration

**Feature Branch**: `feature/spec-015`

**Created**: 2026-08-07

**Status**: Completed (Planning)

**Input**: Derived from Spec 014 frontend requirements.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Tenant API (Priority: P1)

As an Administrator, I want to manage tenants through backend endpoints so that I can persist client data in the database.

**Why this priority**: Core requirement for the platform.

**Independent Test**: Use cURL or Postman to test `GET /api/v1/admin/tenants` and `POST /api/v1/admin/tenants`.

**Acceptance Scenarios**:

1. **Given** valid Basic Auth credentials, **When** calling `GET /api/v1/admin/tenants`, **Then** the system returns a list of tenants from the database.

---

### User Story 2 - Analytics API (Priority: P2)

As an Administrator, I want to fetch metrics and chat history from the backend so that I can visualize system health in the dashboard.

**Why this priority**: Required for Dashboard and Analytics pages.

**Independent Test**: Use cURL to test `GET /api/v1/admin/analytics/metrics`.

**Acceptance Scenarios**:

1. **Given** valid credentials, **When** calling the metrics endpoint, **Then** it returns the aggregated total of tenants and active chats.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide REST endpoints for Tenant CRUD under `/api/v1/admin/tenants`.
- **FR-002**: System MUST provide REST endpoints for Analytics under `/api/v1/admin/analytics`.
- **FR-003**: System MUST protect `/api/v1/admin/**` using Basic Authentication (Spring Security).
- **FR-004**: System MUST connect the Vite Frontend to these endpoints via a local proxy, replacing mock data.

### Key Entities *(include if feature involves data)*

- **Tenant**: Table `tenants`.
- **ChatSession**: Table `chat_sessions`.
- **DashboardMetrics**: DTO for returning aggregated statistics.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Frontend dashboard loads data successfully from the backend database (status 200 OK).
- **SC-002**: Backend tests for `TenantController` pass with >90% coverage.

## Assumptions

- Admin authentication uses Basic Auth with pre-configured static credentials for MVP simplicity.
