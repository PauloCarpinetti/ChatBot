variable "project_id" {
  description = "GCP Project ID"
  type        = string
}

variable "region" {
  description = "GCP Region for resources"
  type        = string
  default     = "us-central1"
}

variable "zone" {
  description = "GCP Zone for resources"
  type        = string
  default     = "us-central1-a"
}

variable "db_password" {
  description = "Database password for Cloud SQL"
  type        = string
  sensitive   = true
}

variable "db_user" {
  description = "Database user for Cloud SQL"
  type        = string
  default     = "postgres"
}

variable "db_name" {
  description = "Database name for Cloud SQL"
  type        = string
  default     = "chatbot_db"
}

variable "cloud_run_service_name" {
  description = "Name for the Cloud Run service"
  type        = string
  default     = "backend"
}

variable "docker_image" {
  description = "Docker image URL for Cloud Run (e.g. us-central1-docker.pkg.dev/project/repo/backend:latest)"
  type        = string
  default     = "us-docker.pkg.dev/cloudrun/container/hello" # Default placeholder
}
