# Implementation Plan: Scalability, Rate Limiting & Observability

## Visão Geral
Nesta fase (Ambiente de Produção e Escalabilidade), vamos construir uma arquitetura "Production Ready". A meta é configurar um pipeline CI/CD robusto, orquestrar os serviços no Google Cloud Platform (GCP) e garantir resiliência financeira implementando limites de requisições por locatário.

## Componentes a Modificar

### 1. Spring Boot Backend (Rate Limiting)
- Incluir a dependência do **Resilience4j** (ou Bucket4j) e do **Spring Data Redis**.
- Configurar `RedisTemplate` para suportar o cache.
- Criar um interceptor de requisição HTTP (`RateLimitInterceptor.java`) que intercepte a rota `/api/v1/chat/message`.
- O interceptor lerá o `tenant_id` do JWT e verificará a chave no Redis (ex: `rate_limit:tenant:<uuid>`).
- Se houver tokens no bucket, a requisição segue; se não, retorna código 429 e headers `X-RateLimit-Limit`, `X-RateLimit-Remaining`.

### 2. Spring Boot Backend (Observabilidade)
- Incluir o **Spring Boot Actuator** e o **Micrometer Registry Prometheus**.
- Adicionar anotações `@Timed` e `@Counted` nas funções essenciais (ex: envio de mensagens pro LLM e geração de RAG).
- Expor os endpoints no `SecurityConfig.java` (`/actuator/prometheus`).

### 3. Pipeline CI/CD (.github/workflows)
- Criar o arquivo `deploy-backend.yml` no GitHub Actions.
- O pipeline será disparado por um *push* na branch `main`.
- Passos:
  1. Compilar Java via Maven.
  2. Construir imagem Docker usando BuildKit.
  3. Fazer login no GCP via Workload Identity Federation (preferível) ou Service Account.
  4. Enviar a imagem para o Artifact Registry.
  5. Acionar `gcloud run deploy` apontando para a nova imagem.

### 4. Setup e Configuração de Contêiner (Docker)
- Ajustar variáveis de ambiente no Dockerfile e nas Action Secrets para permitir flexibilidade e injeção do REDIS_URL e OPENAI_API_KEY.

## Critérios de Aceite
- Ao rodar scripts de teste de carga (ex. K6, Apache Benchmark) contra a API passando um JWT válido de um Tenant, a API deve rejeitar requisições excedentes com status `429 Too Many Requests`.
- O endpoint `/actuator/prometheus` deve responder com o status de latência das respostas do GPT-4.
- Uma simulação de push na branch (Pull Request) deve realizar o *build* no GitHub Actions (mesmo sem permissões totais de infraestrutura configuradas ainda).
