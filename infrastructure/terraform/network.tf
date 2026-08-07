# VPC Network
resource "google_compute_network" "main_vpc" {
  name                    = "chatbot-vpc"
  auto_create_subnetworks = false

  depends_on = [
    google_project_service.compute
  ]
}

# Subnet for Serverless VPC Access
resource "google_compute_subnetwork" "serverless_subnet" {
  name          = "serverless-subnet"
  ip_cidr_range = "10.8.0.0/28"
  region        = var.region
  network       = google_compute_network.main_vpc.id
}

# Serverless VPC Access Connector
resource "google_vpc_access_connector" "connector" {
  name          = "chatbot-vpc-conn"
  region        = var.region
  
  subnet {
    name = google_compute_subnetwork.serverless_subnet.name
  }

  depends_on = [
    google_project_service.vpcaccess
  ]
}

# Global IP address for private services access (Cloud SQL & Redis)
resource "google_compute_global_address" "private_ip_address" {
  name          = "private-ip-address"
  purpose       = "VPC_PEERING"
  address_type  = "INTERNAL"
  prefix_length = 16
  network       = google_compute_network.main_vpc.id
}

# Private Connection to Service Networking (Google APIs)
resource "google_service_networking_connection" "private_vpc_connection" {
  network                 = google_compute_network.main_vpc.id
  service                 = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [google_compute_global_address.private_ip_address.name]

  depends_on = [
    google_project_service.servicenetworking
  ]
}
