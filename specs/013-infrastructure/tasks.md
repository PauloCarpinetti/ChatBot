# Tasks: Spec 013: Infrastructure as Code (GCP)

**Input**: Design documents from `/specs/013-infrastructure/`

**Prerequisites**: plan.md (required), spec.md (required for user stories)

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Initialize Terraform files: `provider.tf`, `variables.tf`, `outputs.tf` in `infrastructure/terraform/`
- [x] T002 Update `README.md` with instructions on how to use Terraform for the project

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T003 [P] Configure GCP APIs in `apis.tf`
- [x] T004 Create VPC and Serverless VPC Access connector in `network.tf`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Secure Database & Cache Provisioning (Priority: P1) 🎯 MVP

**Goal**: Provision PostgreSQL and Redis without public IPs

- [x] T005 [P] [US1] Create Cloud SQL instance (`database.tf`) with private IP 
- [x] T006 [P] [US1] Create Memorystore Redis instance (`cache.tf`) with private IP

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently (databases exist).

---

## Phase 4: User Story 2 - Serverless Backend Deployment (Priority: P2)

**Goal**: Deploy backend on Cloud Run connected to VPC

- [x] T007 [US2] Implement Cloud Run service in `cloudrun.tf` pointing to the VPC Connector (depends on T004, T005, T006)
- [x] T008 [US2] Configure environment variables in `cloudrun.tf` for DB and Redis connections

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently.

---

## Phase 5: User Story 3 - API & Artifact Management (Priority: P3)

**Goal**: Seamless CI/CD requirements (Artifact Registry)

- [x] T009 [P] [US3] Create Artifact Registry repository in `cloudrun.tf`

**Checkpoint**: All user stories should now be independently functional.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [x] T010 Validate the Terraform code formatting and syntax using `terraform fmt` and `terraform validate`
- [x] T011 Commit and push changes to branch `feature/spec-013`
