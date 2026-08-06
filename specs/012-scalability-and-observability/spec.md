# Specification: Scalability, Rate Limiting & Observability (Prod Env)

## 1. Introdução
Esta especificação descreve a arquitetura final para implantação em produção (GCP) do ChatBot B2B (Multi-Tenant).
O objetivo principal é garantir que a aplicação seja altamente escalável (sem estado), tolerante a falhas e, principalmente, protegida contra abuso de consumo da API da OpenAI, por meio da implementação de um mecanismo de **Rate Limiting**. Além disso, o sistema deve ser totalmente monitorável e atualizável por meio de uma esteira CI/CD automatizada.

## 2. Requisitos Principais

### 2.1 Rate Limiting por Locatário (Tenant)
- A chave de autenticação primária é o JWT que carrega o `tenant_id`.
- Devemos restringir o número de requisições de Chat (ex: limite de tokens ou chamadas por minuto) por `tenant_id`.
- Em caso de violação do limite, a API deve retornar `HTTP 429 Too Many Requests`.
- A contagem e controle de janelas de tempo serão realizados usando **Redis** para garantir atomicidade em um ambiente balanceado.

### 2.2 Observabilidade e Monitoramento
- Expor métricas da JVM e métricas de aplicação (número de chats, requisições HTTP, contagem de falhas) através do **Micrometer** (endpoint Actuator `/actuator/prometheus`).
- Gerar logs estruturados indicando: ID da conversa, `tenant_id`, latência da requisição LLM e tokens consumidos.
- Acompanhar as despesas de forma a identificar *tenants* mais ativos.

### 2.3 Deploy e Infraestrutura em Nuvem (GCP)
- O **Backend Spring Boot** será encapsulado em uma imagem Docker (já configurado) e publicado no **Google Cloud Run**, aproveitando a escalabilidade Serverless (scale to zero).
- **Weaviate**: Devido às exigências de vetorização e custo operacional, utilizaremos uma instância Compute Engine ou Weaviate Cloud (WCD).
- **Bancos de Dados**: O MySQL será provisionado via **Cloud SQL**.
- **Redis**: Provisionado via **Memorystore for Redis**.
- O GitHub Actions (CI/CD) será configurado para construir imagens e fazer push para o **Artifact Registry**, e em seguida, acionar uma nova revisão no Cloud Run.

## 3. Considerações de Segurança e Performance
- O banco Cloud SQL e a instância do Memorystore devem ser protegidos em uma VPC privada e o Cloud Run deve acessá-los através de conectores Serverless VPC Access (se aplicável).
- O Rate Limiting usa o algoritmo Token Bucket para garantir limites rígidos mas permitir pequenos picos instantâneos.
