# Implementation Plan: Spec 013: Infrastructure as Code (GCP)

**Branch**: `feature/spec-013` | **Date**: 2026-08-07 | **Spec**: [specs/013-infrastructure/spec.md](file:///c:/Users/paulo/Desktop/projetosPortifolio/ChatBot/ChatBot/specs/013-infrastructure/spec.md)

**Input**: Feature specification from `/specs/013-infrastructure/spec.md`

## Summary

Provision GCP infrastructure (Cloud Run, Cloud SQL, Memorystore, Vertex AI, Artifact Registry, VPC) using Terraform. All data stores will be securely placed inside a Virtual Private Cloud (VPC) with internal IPs only, while the Cloud Run backend accesses them via a Serverless VPC Access Connector.

## Technical Context

**Language/Version**: Terraform (>= 1.5.0), HCL

**Primary Dependencies**: `hashicorp/google` Terraform Provider

**Storage**: Cloud SQL (PostgreSQL db-f1-micro) and Memorystore (Redis BASIC tier)

**Testing**: `terraform validate`, `terraform plan`

**Target Platform**: Google Cloud Platform (GCP)

**Project Type**: Infrastructure as Code (IaC)

**Performance Goals**: N/A (Infrastructure provisioning)

**Constraints**: Minimize cost by using free-tier or cheapest available managed resources (Cloud Run scaling to 0, db-f1-micro, Redis 1GB). Secure all databases behind a VPC.

**Scale/Scope**: Cloud Run will scale horizontally from 0 to 5 instances max based on traffic. 

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

No violations. The chosen architecture respects the existing Spring Boot multi-tenant backend constraints.

## Project Structure

### Documentation (this feature)

```text
specs/013-infrastructure/
├── plan.md              # This file
├── spec.md              # Feature specification
└── tasks.md             # Execution task list
```

### Source Code (repository root)

```text
infrastructure/
└── terraform/
    ├── apis.tf          # API enablement
    ├── cache.tf         # Memorystore (Redis)
    ├── cloudrun.tf      # Cloud Run and Artifact Registry
    ├── database.tf      # Cloud SQL
    ├── network.tf       # VPC and Private IP
    ├── outputs.tf       # Provisioned IPs and URLs
    ├── provider.tf      # GCP Provider config
    └── variables.tf     # Configurable inputs
```

**Structure Decision**: A dedicated `infrastructure/terraform` directory cleanly separates IaC code from application source code (`backend/`). Modular `.tf` files ensure readability and maintainability compared to a single monolithic `main.tf`.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| N/A       | N/A        | N/A                                 |
