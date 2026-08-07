# Artifact Registry Repository
resource "google_artifact_registry_repository" "repo" {
  location      = var.region
  repository_id = "chatbot-repo"
  description   = "Docker repository for ChatBot backend"
  format        = "DOCKER"
  
  depends_on = [
    google_project_service.artifactregistry
  ]
}

# Cloud Run Service (Backend)
resource "google_cloud_run_v2_service" "backend" {
  name     = var.cloud_run_service_name
  location = var.region
  ingress  = "INGRESS_TRAFFIC_ALL"

  depends_on = [
    google_project_service.cloudrun,
    google_sql_database_instance.postgres,
    google_redis_instance.cache
  ]

  template {
    scaling {
      min_instance_count = 0 # Scale to zero to save credits
      max_instance_count = 5
    }

    vpc_access {
      connector = google_vpc_access_connector.connector.id
      egress    = "PRIVATE_RANGES_ONLY"
    }

    containers {
      image = var.docker_image

      resources {
        limits = {
          cpu    = "1000m"
          memory = "512Mi"
        }
      }

      env {
        name  = "SPRING_DATASOURCE_URL"
        value = "jdbc:postgresql://${google_sql_database_instance.postgres.private_ip_address}:5432/${var.db_name}"
      }
      env {
        name  = "SPRING_DATASOURCE_USERNAME"
        value = var.db_user
      }
      env {
        name  = "SPRING_DATASOURCE_PASSWORD"
        value = var.db_password
      }
      env {
        name  = "SPRING_DATA_REDIS_HOST"
        value = google_redis_instance.cache.host
      }
      env {
        name  = "SPRING_DATA_REDIS_PORT"
        value = google_redis_instance.cache.port
      }
      # Required for Langchain4j Vertex AI integration 
      # By default, GCP application default credentials (ADC) are used.
      env {
        name  = "GCP_PROJECT_ID"
        value = var.project_id
      }
      env {
        name  = "GCP_LOCATION"
        value = var.region
      }
    }
  }
}

# Allow public access to Cloud Run (if desired, remove this for private only)
resource "google_cloud_run_v2_service_iam_member" "public_access" {
  project = google_cloud_run_v2_service.backend.project
  location = google_cloud_run_v2_service.backend.location
  name = google_cloud_run_v2_service.backend.name
  role = "roles/run.invoker"
  member = "allUsers"
}
