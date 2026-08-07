# Cloud SQL Database Instance
resource "google_sql_database_instance" "postgres" {
  name             = "chatbot-db-instance"
  database_version = "POSTGRES_15"
  region           = var.region
  
  depends_on = [
    google_service_networking_connection.private_vpc_connection,
    google_project_service.sqladmin
  ]

  settings {
    tier = "db-f1-micro" # Smallest tier for credits saving
    
    ip_configuration {
      ipv4_enabled    = false # Disable public IP
      private_network = google_compute_network.main_vpc.id
    }
    
    # Optional: Enable backups and maintenance windows if needed.
    # Leaving out for minimum cost/config.
  }
}

# The actual database within the instance
resource "google_sql_database" "database" {
  name     = var.db_name
  instance = google_sql_database_instance.postgres.name
}

# The database user
resource "google_sql_user" "user" {
  name     = var.db_user
  instance = google_sql_database_instance.postgres.name
  password = var.db_password
}
