# Memorystore for Redis
resource "google_redis_instance" "cache" {
  name           = "chatbot-redis"
  tier           = "BASIC" # Basic tier for cost savings
  memory_size_gb = 1       # Minimum allowed size

  region                  = var.region
  location_id             = var.zone
  authorized_network      = google_compute_network.main_vpc.id
  connect_mode            = "PRIVATE_SERVICE_ACCESS"
  redis_version           = "REDIS_7_0"
  
  depends_on = [
    google_service_networking_connection.private_vpc_connection,
    google_project_service.redis
  ]
}
