# Tasks: Spec 015: Backend Admin API & Integration

**Input**: Design documents from `/specs/015-backend-admin/`

**Prerequisites**: plan.md (required), spec.md (required for user stories)

## Phase 1: Setup Backend

**Purpose**: Create controllers and protect routes.

- [ ] T001 Modify `SecurityConfig.java` to protect `/api/v1/admin/**` using Basic Auth.
- [ ] T002 Create `TenantService` to handle business logic for tenants.
- [ ] T003 Create `TenantController` with standard CRUD mappings.
- [ ] T004 Create `AnalyticsController` for fetching metrics.

## Phase 2: Setup Frontend

**Purpose**: Connect frontend to backend.

- [ ] T005 Update `vite.config.ts` to proxy `/api` to `http://localhost:8080`.
- [ ] T006 Update `src/services/api.ts` to use `fetch` with Basic Auth headers.
- [ ] T007 Remove mock data logic.

## Phase 3: Validation

**Purpose**: Test integration.

- [ ] T008 Run backend tests.
- [ ] T009 Manual E2E test via UI.
