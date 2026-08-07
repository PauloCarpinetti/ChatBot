# ChatBot Project

A robust AI ChatBot orchestrator with multi-tenancy, rate limiting, and conversational memory, built with Java 21, Spring Boot, and LangChain4j.

## Infrastructure

The infrastructure for this project is managed using Terraform and runs on Google Cloud Platform (GCP).

### Prerequisites
- [Terraform](https://developer.hashicorp.com/terraform/install) >= 1.5.0
- [Google Cloud CLI (gcloud)](https://cloud.google.com/sdk/docs/install)
- A GCP project with billing enabled.

### Deployment Instructions

1. Navigate to the terraform directory:
   ```bash
   cd infrastructure/terraform
   ```

2. Initialize Terraform (downloads providers):
   ```bash
   terraform init
   ```

3. Configure your GCP project and credentials. You can set them up using `gcloud`:
   ```bash
   gcloud auth application-default login
   gcloud config set project YOUR_PROJECT_ID
   ```

4. Create a `terraform.tfvars` file or pass variables via command line:
   ```hcl
   project_id  = "YOUR_PROJECT_ID"
   db_password = "YOUR_SECURE_PASSWORD"
   # other variables as defined in variables.tf
   ```

5. Review the plan:
   ```bash
   terraform plan -var="project_id=YOUR_PROJECT_ID" -var="db_password=YOUR_SECURE_PASSWORD"
   ```

6. Apply the configuration:
   ```bash
   terraform apply -var="project_id=YOUR_PROJECT_ID" -var="db_password=YOUR_SECURE_PASSWORD"
   ```

### Architecture
- **Cloud Run:** Serverless compute for the backend API, scaling to zero when inactive.
- **Cloud SQL (PostgreSQL):** Managed relational database with private IP for tenant and chat memory storage.
- **Memorystore (Redis):** Managed Redis instance with private IP for Rate Limiting and Caching.
- **Vertex AI:** Access to Gemini 1.5 for the AI Chat capabilities.
- **Artifact Registry:** Docker container registry for deploying backend images.
- **VPC & Serverless VPC Access:** Secures communication between Cloud Run and the databases without exposing them to the internet.
