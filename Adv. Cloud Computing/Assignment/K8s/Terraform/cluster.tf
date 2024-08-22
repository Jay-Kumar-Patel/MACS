resource "google_container_cluster" "k8s-assignment" {
  name                     = "k8s-assignment"
  location                 = "us-central1-c"
  remove_default_node_pool = true
  initial_node_count       = 1
  logging_service          = "logging.googleapis.com/kubernetes"
  monitoring_service       = "monitoring.googleapis.com/kubernetes"
}