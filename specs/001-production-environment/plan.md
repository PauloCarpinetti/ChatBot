# Implementation Plan: Escalabilidade, Monitoramento e Produção (Ambiente PROD)

**Branch**: `feature/001-production-environment` | **Date**: 2026-08-03 | **Spec**: `/specs/001-production-environment/spec.md`

## Summary

O foco principal é transformar o projeto ChatBot em um serviço escalável, altamente disponível e com seus custos blindados contra tráfego abusivo. A orquestração será feita na GCP (Cloud Run), garantindo escala e pagamento sob demanda. Além disso, implementaremos o CI/CD (GitHub Actions), a observabilidade (latência e tokens), e Rate Limiting essencial usando tenant_id.

## Technical Context

**Language/Version**: Node.js / TypeScript, Python (conforme stacks do backend existentes).

**Primary Dependencies**: 
- backend: Bibliotecas para Rate Limiting (como `redis`, `express-rate-limit` ou equivalentes). Ferramentas de observabilidade (ex: Google Cloud Logging/Monitoring, Prometheus).
- devops: GitHub Actions, gcloud CLI, Docker.

**Storage**: Redis (Memorystore no GCP) para gerenciamento de estado temporário e contadores de limite de requisições. O banco relacional será o Cloud SQL ou equivalente.

**Target Platform**: 
- **Produção (PROD)**: Google Cloud Platform (Cloud Run para Backend, Cloud SQL para DB, Cloud Storage/CDN para Frontend Widget).
- **Testes / Staging**: Servidor Ubuntu Gratuito (ex: GCP e2-micro free tier, AWS EC2 t2.micro, ou Oracle Cloud Always Free) rodando com Docker Compose.

**Project Type**: Web API backend e Frontend widget.

**Performance Goals**: Latência adicionada pelo Rate Limiter não superior a 5-10ms.

**Constraints**: Os tokens de acesso à API e Service Accounts devem ser injetados estritamente como secrets no GitHub Actions, garantindo que o deploy seja seguro e zero-trust.

## Cloud Architecture & Infra

> [!TIP]
> A estratégia foi ajustada para iniciar **100% no Docker local**. Validaremos a arquitetura, prepararemos o banco de dados e mediremos o consumo de tokens (GPT-4) antes de movermos qualquer recurso para a nuvem (seja VPS ou GCP).

### Fase 1: Ambiente Local (Docker Compose)
Para validar o sistema sem custos de infraestrutura e garantir o total preparo dos dados, utilizaremos o **Docker Compose** localmente.
- **Bancos de Dados**: MySQL e Weaviate já configurados no `docker-compose.yml`.
- **Cache & Rate Limit**: Adicionaremos o **Redis** ao Compose.
- **Backend (Node.js)**: Será conteinerizado e adicionado ao Compose, conectado às redes internas dos bancos.
- **Frontends**: Podem ser servidos pelo próprio docker (ex: Nginx) ou rodados via `npm run dev` localmente apontando para o backend no docker.

### Fase 2: Validação de Consumo GPT-4
- O backend interceptará e fará o log de **prompt_tokens** e **completion_tokens** de cada chamada à OpenAI.
- Realizaremos a população dos bancos (MySQL e Weaviate) usando a infra local.
- Mediremos na ponta do lápis os custos de Rate Limit e Tokens do GPT-4.

### Fase 3: Ambiente de Produção (GCP Serverless)
(Planejado para o futuro, após conclusão e sucesso da Fase 1 e 2).
1. **Frontend Widget**: Cloud Storage + Cloud CDN.
2. **Backend**: Imagens Docker no Artifact Registry, executadas no **Cloud Run** (auto-scaling).
3. **Bancos de Dados**: **Cloud SQL** (MySQL) e **Memorystore** (Redis).

## Project Structure

### Documentation (this feature)

```text
specs/001-production-environment/
├── plan.md              # This file
├── spec.md              # Requirement specifications
└── tasks.md             # Task breakdown
```

### Source Code modifications

```text
.github/
└── workflows/
    └── deploy.yml            # CI/CD pipelines rules

backend/
├── src/
│   ├── middlewares/
│   │   ├── rateLimiter.ts    # Rate limiting middleware por tenant
│   │   └── metricsLogger.ts  # Exportador de métricas e latência
│   └── services/
│       └── metrics.ts        # Serviço auxiliar para registrar tokens
└── Dockerfile                # Configurações do container para GCP

cloud-functions/ (se aplicável para processos assíncronos)
└── alerts/
```

**Structure Decision**: A principal adição estrutural será a pasta `.github/workflows` e os novos arquivos de middleware dentro do `backend/src/middlewares/`. Manteremos a arquitetura em monorepo com módulos independentes.

## Implementation Steps

1. **Setup do GCP**: Configurar o projeto no GCP, ativar a API do Cloud Run, Container Registry/Artifact Registry.
2. **GitHub Actions**: Configurar secrets e criar arquivo `.github/workflows/deploy.yml`.
3. **Containerização**: Finalizar/otimizar o `Dockerfile` do `backend` e validar sua execução na nuvem.
4. **Rate Limiting**: Desenvolver e testar o middleware no backend, validando com um Redis local/em-memória. 
5. **Observabilidade**: Injetar métricas nas chamadas LLM e configurar as métricas para o dashboard de logs e monitoramento.
