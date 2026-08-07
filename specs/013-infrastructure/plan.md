# Plano de Ação: Spec 013 (Infrastructure as Code)

## 1. Organização do Código
O código de infraestrutura ficará isolado no diretório `infrastructure/terraform` na raiz do projeto.

## 2. Componentes Terraform
Serão criados os seguintes arquivos principais:
*   `provider.tf`: Configuração do provider e definição de credenciais/região (recomendado: `us-central1`).
*   `variables.tf`: Variáveis para parametrização do projeto (ID do projeto GCP, senha do DB, etc.).
*   `network.tf`: Criação da VPC e configuração de acesso privado aos serviços gerenciados.
*   `database.tf`: Configuração da instância Cloud SQL PostgreSQL.
*   `cache.tf`: Configuração do Memorystore for Redis.
*   `cloudrun.tf`: Definição do serviço Cloud Run, injetando as variáveis de ambiente necessárias e o Serverless VPC Access.
*   `apis.tf`: Habilitação da Vertex AI API, Artifact Registry API, Cloud Run API, etc.

## 3. Segurança e Economia
- As credenciais de banco não ficarão expostas no código, sendo injetadas pelo Secret Manager ou variáveis no apply.
- Cloud SQL usará instâncias econômicas (`db-f1-micro`) para poupar os créditos GCP.
- A comunicação entre o Cloud Run, Redis e PostgreSQL será toda interna (sem IP público para os bancos), garantindo total isolamento via VPC.

## 4. Integração LangChain4j e Vertex AI
Como definido na Spec 012, após a infraestrutura estar provisionada, o código Spring Boot já existente se conectará à Vertex AI usando o token de serviço embutido no Cloud Run via Application Default Credentials (ADC).
