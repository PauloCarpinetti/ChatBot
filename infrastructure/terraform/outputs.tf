output "cloud_run_url" {
  value       = google_cloud_run_v2_service.backend.uri
  description = "The URL on which the deployed service is available"
}

output "redis_host" {
  value       = google_redis_instance.cache.host
  description = "The IP address of the Redis instance"
}

output "db_connection_name" {
  value       = google_sql_database_instance.postgres.connection_name
  description = "The connection name of the Cloud SQL instance"
}
