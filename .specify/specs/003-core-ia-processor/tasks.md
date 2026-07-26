# Tasks: Core IA (Background Processor)

- `[ ]` 1. Configurar infraestrutura base da Cloud Function (Cron trigger).
- `[ ]` 2. Desenvolver módulo de `Scraping` para obter o HTML das fontes alvo.
- `[ ]` 3. Desenvolver módulo de `Sanitization`:
  - `[ ]` 3.1. Limpeza de tags desnecessárias (CSS, JS, Navs).
  - `[ ]` 3.2. Extração do texto principal.
  - `[ ]` 3.3. Truncamento com base em regras de tamanho máximo (limite da context window).
- `[ ]` 4. Implementar pipeline de chamada à OpenAI (`gpt-4o-mini`).
- `[ ]` 5. Implementar mecanismo de controle de falhas (Retries):
  - `[ ]` 5.1. Logar chamadas falhas (status `failed`).
  - `[ ]` 5.2. Aplicar backoff exponencial e limite de tentativas (ex: max 3x).
- `[ ]` 6. Efetuar testes integrados (Unit Tests + End-to-End).
- `[ ]` 7. Deploy e monitoramento da function no ambiente de homologação.
