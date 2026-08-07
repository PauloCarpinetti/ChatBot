## 1. Setup do Terraform
- [ ] Inicializar o diretório `infrastructure/terraform` no repositório.
- [ ] Criar o arquivo `provider.tf` configurando o provider do Google Cloud.

## 2. Configuração de Rede (VPC e Acesso Privado)
- [ ] Criar recursos de VPC (`google_compute_network`, `google_compute_subnetwork`).
- [ ] Configurar acesso a serviços privados (`google_service_networking_connection`) para o Cloud SQL e Redis.
- [ ] Criar Serverless VPC Access Connector para que o Cloud Run acesse a VPC privada.

## 3. Serviços Gerenciados
- [ ] Terraform para Cloud SQL (PostgreSQL), instanciando a base `db-f1-micro`.
- [ ] Terraform para Memorystore for Redis (Camada Básica 1GB).
- [ ] Habilitar as APIs do Vertex AI e Artifact Registry (`google_project_service`).

## 4. Deploy do Cloud Run
- [ ] Configurar o Artifact Registry via Terraform.
- [ ] Configurar o recurso do Cloud Run integrando com a VPC e variáveis de ambiente (banco, redis, vertex ai).

## 5. Documentação
- [ ] Atualizar o README com as instruções para rodar o `terraform apply`.
