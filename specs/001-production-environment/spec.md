# Feature Specification: Escalabilidade, Monitoramento e Produção (Ambiente PROD)

**Feature Branch**: `feature/001-production-environment`

**Created**: 2026-08-03

**Status**: Draft

**Input**: User description: "Escalabilidade, Monitoramento e Produção (Ambiente PROD). Orquestração na GCP para escalabilidade horizontal, Rate Limiting por Tenant, Pipeline CI/CD com GitHub Actions, Observabilidade (latência, consumo de tokens, alertas)."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Local Docker Orchestration & DB Population (Priority: P1)

Como engenheiro de software, quero rodar toda a infraestrutura (Backend, Redis, MySQL, Weaviate) utilizando o Docker local (Docker Compose), para que eu possa validar a comunicação entre os componentes e popular os bancos de dados corretamente antes de provisionar recursos na nuvem.

**Why this priority**: É a fundação do ambiente. Não se deve ir para a nuvem sem garantir que a aplicação funciona perfeitamente de ponta a ponta e que a estrutura do banco de dados está pronta (migrations e seeds).

**Independent Test**: Pode ser testado rodando `docker-compose up -d` e acessando o backend via `localhost`, confirmando que as conexões ao MySQL e Weaviate estão ativas e as APIs de teste respondem.

**Acceptance Scenarios**:

1. **Given** um ambiente de desenvolvimento limpo, **When** executo `docker-compose up`, **Then** os containers do MySQL, Weaviate, Redis e Backend iniciam corretamente.
2. **Given** o backend em execução local, **When** chamo a rota de popular DB, **Then** as tabelas do MySQL recebem os dados do tenant e o Weaviate recebe os vetores.

---

### User Story 2 - Rate Limiting por Tenant (Priority: P1)

Como gestor do produto, quero que cada requisição seja contabilizada e limitada com base no `tenant_id`, para que um único cliente (tenant) não consuma abusivamente os recursos de infraestrutura e API LLM (tokens), protegendo a rentabilidade do modelo multi-tenant.

**Why this priority**: O Rate Limiting é a principal salvaguarda financeira, essencial para impedir contas milionárias por uso excessivo não previsto.

**Independent Test**: Pode ser testado disparando dezenas de requisições sob um mesmo `tenant_id` até atingir o limite estipulado, validando que a API devolve o status `429 Too Many Requests`.

**Acceptance Scenarios**:

1. **Given** um tenant com limite de 100 requests por minuto, **When** a aplicação envia a 101ª requisição no mesmo minuto, **Then** a API retorna um código `429` com o cabeçalho `Retry-After`.
2. **Given** requests simultâneos para diferentes tenants, **When** um atinge seu limite, **Then** os outros tenants continuam sendo atendidos normalmente.

---

### User Story 3 - Pipeline de CI/CD (Priority: P2)

Como engenheiro de software, quero que qualquer código integrado à branch principal dispare um fluxo automatizado de testes e deploy via GitHub Actions, para que o core do sistema permaneça estável e reduza o tempo entre o desenvolvimento e a disponibilidade em produção.

**Why this priority**: A automação do deploy previne falhas humanas na atualização de produção e garante que apenas versões testadas sejam publicadas.

**Independent Test**: Pode ser testado abrindo um Pull Request e verificando a execução dos testes automatizados. O deploy é verificado ao fazer merge na `main`.

**Acceptance Scenarios**:

1. **Given** um novo commit na branch `main`, **When** o GitHub Actions é disparado, **Then** os testes automatizados são executados, a imagem Docker é construída e implantada no GCP (Cloud Run) automaticamente.

---

### User Story 4 - Observabilidade e Alertas (Priority: P2)

Como operador do sistema, quero ter um dashboard central com as métricas de latência, número de erros e consumo de tokens, com envio de alertas para picos imprevistos, para que possamos detectar e atuar preventivamente frente a anomalias.

**Why this priority**: Permite detecção antecipada de problemas antes que eles impactem a percepção dos usuários finais ou resultem em alto custo.

**Independent Test**: Simular erros (ex: chaves inválidas) e picos de uso para verificar se os alertas (ex: via webhook/Slack) são devidamente disparados no tempo configurado.

**Acceptance Scenarios**:

1. **Given** que o serviço de LLM externo comece a apresentar lentidão (latência > 3s), **When** a condição persiste por mais de 5 minutos, **Then** um alerta é disparado automaticamente para os canais de engenharia.
2. **Given** o acompanhamento padrão, **When** um tenant excede 10.000 tokens diários (limite de anomalia), **Then** um alerta específico para uso intensivo de tenant é disparado.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O sistema MUST usar o `docker-compose.yml` para orquestrar MySQL, Weaviate, Redis e o container do Node.js Backend em rede local.
- **FR-002**: O sistema MUST interceptar cada request à API do backend, extrair o `tenant_id`, e validar com um rate limiter conectado ao Redis rodando no docker.
- **FR-003**: O sistema MUST rejeitar requests excedentes com o status `429 Too Many Requests`.
- **FR-004**: O sistema MUST logar explicitamente a quantidade de `prompt_tokens` e `completion_tokens` de cada request feito ao GPT-4 para auditoria de consumo local.
- **FR-005**: O sistema MUST ter rotas/scripts específicos (ex: migrations, seeders) para popular o MySQL e o Weaviate com o contexto inicial de teste de tenants.
- **FR-006**: O repositório MUST conter `.github/workflows/deploy.yml` planejado para a GCP, mas o deploy será pausado até a conclusão da fase local.

### Key Entities 

- **RateLimiter**: Serviço de controle de cotas baseado no Redis que mantém contadores por tenant e por janelas de tempo.
- **MetricEvent**: Entidade de log que armazena dados de execução como `tenant_id`, `tokens_prompt`, `tokens_completion` e `latency_ms`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: O tempo de deploy e disponibilidade da aplicação após o merge na `main` deve ser de até 5 minutos (Pipeline automatizado).
- **SC-002**: A API suporta até 10.000 requests/segundo globais graças à orquestração escalável.
- **SC-003**: 100% das requisições devem consumir uma cota válida do tenant, e requests excedentes devem ser barrados em menos de 10ms (Rate Limiting rápido com Redis).
- **SC-004**: Qualquer erro `5xx` ou aumento de latência superior a 2 segundos dispara um alerta para a equipe técnica em menos de 1 minuto.

## Assumptions

- Presume-se que teremos acesso a uma conta GCP com permissões de faturamento ativas e acesso a IAM para criar Service Accounts necessárias para o GitHub Actions.
- O sistema utilizará o Redis já provisionado (ou um novo cluster no GCP Memorystore) como backend de armazenamento do Rate Limiter e cache rápido.
- O serviço externo provedor de LLM fornece a quantidade de tokens consumidos em cada resposta da API, permitindo a ingestão dessas métricas pela observabilidade.
