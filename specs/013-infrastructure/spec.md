# Spec 013: Infrastructure as Code (GCP)

## Descrição
Configuração da infraestrutura como código (IaC) utilizando Terraform para provisionar os recursos necessários na Google Cloud Platform (GCP). O objetivo é criar os serviços gerenciados para rodar o backend do ChatBot de forma segura, escalável e otimizada (Cloud Run, Cloud SQL, Memorystore e Vertex AI).

## Objetivos
1.  **Cloud Run**: Provisionar o serviço do Cloud Run para rodar o contêiner Docker do backend.
2.  **Cloud SQL**: Provisionar uma instância PostgreSQL para armazenamento de tenants e memórias de chat.
3.  **Memorystore**: Provisionar uma instância Redis para rate limiting e cache.
4.  **Vertex AI**: Habilitar a API da Vertex AI para utilização do LLM (Gemini 1.5).
5.  **Artifact Registry**: Configurar um repositório Docker para as imagens da aplicação.
6.  **Rede & Segurança**: Criar uma VPC, configurar IPs privados e conectar os serviços de banco de dados/cache de forma segura sem expô-los publicamente.

## Escopo
*   Criação de scripts Terraform (`main.tf`, `variables.tf`, `outputs.tf`).
*   Configuração do provedor GCP.
*   Documentação de execução do Terraform.
*   *Não inclui:* Migração de dados de produção existentes.

## Critérios de Aceite
- O comando `terraform plan` deve executar com sucesso validando os recursos.
- A infraestrutura deve garantir comunicação interna segura (VPC Connector) entre o Cloud Run, o Cloud SQL e o Memorystore.
- O Cloud SQL deve estar configurado na camada de testes/MVP (`db-f1-micro` ou similar) para economia de créditos.
- O Redis deve ser provisionado na camada Básica (1GB).
