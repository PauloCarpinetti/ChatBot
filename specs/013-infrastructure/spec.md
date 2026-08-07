# Feature Specification: Spec 013: Infrastructure as Code (GCP)

**Feature Branch**: `feature/spec-013`

**Created**: 2026-08-07

**Status**: Completed

**Input**: User description: "Provision GCP infrastructure (Cloud Run, Cloud SQL, Memorystore, Vertex AI) using Terraform scripts"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Secure Database & Cache Provisioning (Priority: P1)

As a DevOps engineer, I want the PostgreSQL database (Cloud SQL) and Redis cache (Memorystore) to be provisioned without public IP addresses, so that sensitive data is kept secure and inaccessible from the public internet.

**Why this priority**: Security is the highest priority. Exposing databases publicly is a critical vulnerability.

**Independent Test**: Can be fully tested by applying the Terraform scripts and verifying in the GCP Console that the instances have no public IPs assigned.

**Acceptance Scenarios**:

1. **Given** the Terraform configuration, **When** `terraform apply` is executed, **Then** a Cloud SQL instance and Memorystore instance are created.
2. **Given** the provisioned instances, **When** their networking configuration is inspected, **Then** neither instance has a public IP address enabled.

---

### User Story 2 - Serverless Backend Deployment (Priority: P2)

As a Developer, I want to deploy the application backend on Cloud Run within a VPC Connector, so that the backend can securely communicate with the private databases while automatically scaling to zero to save costs.

**Why this priority**: Cloud Run is the compute layer required to run the application, and the VPC connector is strictly required to fulfill the security constraints of User Story 1.

**Independent Test**: Can be tested by running `terraform apply` and verifying the Cloud Run service is created, attached to the VPC connector, and has scaling min_instance_count set to 0.

**Acceptance Scenarios**:

1. **Given** the VPC connector configuration, **When** the Cloud Run service is deployed, **Then** it is attached to the Serverless VPC Access connector.
2. **Given** the Cloud Run service, **When** inspected for scaling rules, **Then** the minimum instances must be exactly 0.

---

### User Story 3 - API & Artifact Management (Priority: P3)

As a CI/CD pipeline, I need an Artifact Registry repository and the necessary GCP APIs enabled automatically, so that I can seamlessly push Docker images and deploy without manual configuration steps.

**Why this priority**: Automating API enablement and Artifact Registry creation reduces operational overhead and allows the CI/CD pipeline to work correctly.

**Independent Test**: Can be verified by running `terraform apply` and confirming the Docker repository is available and APIs (Run, SQL, Redis, Vertex AI) are active.

**Acceptance Scenarios**:

1. **Given** the Terraform configuration, **When** applied, **Then** all required APIs (Vertex AI, Cloud Run, SQL Admin, etc.) are enabled.
2. **Given** the Artifact Registry configuration, **When** applied, **Then** a Docker format repository is successfully created.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST configure a GCP Virtual Private Cloud (VPC) network.
- **FR-002**: System MUST provision a Serverless VPC Access Connector.
- **FR-003**: System MUST provision a Cloud SQL PostgreSQL instance (tier: db-f1-micro) with private IP only.
- **FR-004**: System MUST provision a Memorystore Redis instance (tier: BASIC) with private IP only.
- **FR-005**: System MUST provision a Cloud Run service configured to route egress traffic through the VPC Connector.
- **FR-006**: System MUST automatically enable required GCP APIs (Compute, Run, SQL, Redis, Vertex AI, Artifact Registry).
- **FR-007**: System MUST output connection strings and IPs needed for deployment.

### Key Entities *(include if feature involves data)*

- **Cloud SQL Instance**: Stores tenant configurations and chat history.
- **Memorystore Redis**: Caches rate limit buckets and frequently accessed queries.
- **Cloud Run Service**: Executes the Spring Boot backend container.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Terraform configuration passes `terraform validate` without syntax errors.
- **SC-002**: Running `terraform plan` produces a predictable plan to create all 10+ resources without failure.
- **SC-003**: All infrastructure can be torn down safely with `terraform destroy` (excluding protected databases, if any).

## Assumptions

- Assumes the user has the GCP CLI installed and authenticated with an active billing account.
- Assumes the region for deployment defaults to `us-central1` if not overridden.
- Assumes the database password and sensitive variables will be supplied by the user via environment variables or a `.tfvars` file.
