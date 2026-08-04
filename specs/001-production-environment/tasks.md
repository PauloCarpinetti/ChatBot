# Feature Tasks: Escalabilidade, Monitoramento e Produção (Ambiente PROD)

**Branch**: `feature/001-production-environment` | **Date**: 2026-08-03

## Phase 1: Rate Limiting & Middleware

- `[ ]` 1. Instalar dependências para rate limiting (ex: `redis`, `express-rate-limit`) no backend.
- `[ ]` 2. Configurar a conexão com o Redis.
- `[ ]` 3. Implementar o middleware de Rate Limiting interceptando requisições com base no `tenant_id`.
- `[ ]` 4. Escrever testes unitários e de integração validando os cenários onde o tenant atinge o limite (`429 Too Many Requests`).

## Phase 2: Observabilidade

- `[ ]` 5. Implementar o middleware para exportação de métricas (logs de acesso contendo latência e tamanho dos tokens do payload).
- `[ ]` 6. Ajustar a camada de serviço que interage com as APIs de LLM para contabilizar o número de tokens no log.
- `[ ]` 7. Validar o log estruturado nos testes de integração.

## Phase 3: Ambiente Local (Docker Compose) & População do Banco

- `[ ]` 8. Atualizar o arquivo `docker-compose.yml` para incluir os serviços `redis` e `backend` (Node.js).
- `[ ]` 9. Definir variáveis de ambiente no arquivo `.env` conectando os serviços via rede interna do docker.
- `[ ]` 10. Implementar scripts de seed/migration para inicializar o MySQL com tenants de teste.
- `[ ]` 11. Implementar script de carga inicial de vetores no Weaviate.
- `[ ]` 12. Rodar a stack completa no Docker e disparar requisições pelo Frontend local para testar a integração.

## Phase 4: Validação de Consumo & CI/CD (Futuro)

- `[ ]` 13. Testar dezenas de mensagens e extrair relatório de custo de tokens GPT-4 gerado nos logs.
- `[ ]` 14. Criar o diretório `.github/workflows` e o `deploy.yml`.
- `[ ]` 15. Integrar testes no CI, paralisando deploy à GCP até os custos e a estabilidade local estarem 100% validados.
