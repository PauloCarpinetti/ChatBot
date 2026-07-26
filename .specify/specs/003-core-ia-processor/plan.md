# Plan: Core IA (Background Processor)

## 1. Pesquisa e Definições Arquiteturais
- **Provedor da Cloud Function**: Decidir se será AWS Lambda, Google Cloud Functions ou Azure Functions.
- **Linguagem**: Sugere-se Node.js ou Python pela facilidade com pacotes de scraping (ex: `cheerio` ou `BeautifulSoup`), mas Java (Spring Cloud Function) pode ser usado para manter consistência com o backend atual.
- **Cron Job**: Definir a periodicidade (ex: de hora em hora, diariamente às 3 da manhã).

## 2. Scraping e Sanitização
- Identificar as fontes (URLs) que o scraper vai consumir.
- Desenvolver a lógica de limpeza:
  1. Baixar o HTML bruto.
  2. Extrair apenas a tag `<body>` ou `<article>`.
  3. Remover `<script>`, `<style>`, `<nav>`, `<footer>`, etc.
  4. Extrair texto puro.
  5. Truncar caso ultrapasse ~8000 caracteres (ou um valor seguro para o token limit do `gpt-4o-mini`).

## 3. Retentativas (Resiliência)
- Armazenar o estado da extração/processamento no banco de dados (ex: tabela `scraping_jobs` com colunas `url`, `status`, `retry_count`, `last_error`).
- Ao falhar a requisição com a OpenAI (timeout, 429 Too Many Requests, 5xx):
  - Incrementar `retry_count`.
  - Se `retry_count` < MAX_RETRIES (ex: 3), colocar o job em status `pending_retry` com atraso exponencial (ex: 1min, 5min, 15min).
  - Se `retry_count` atingir o máximo, marcar como `failed`.

## 4. Orçamento e Limites
- Garantir logs do tamanho do texto antes de chamar a IA, para ter rastreabilidade se a conta exceder limites.

## 5. Estratégia de Testes (TDD/Integração)
- **Testes Unitários (Sanitization)**: Criar testes mockando diferentes cenários de HTML (com scripts maliciosos, com muito ruído, ou textos imensos) para garantir que a lógica de limpeza e truncamento funcione corretamente sem estourar o limite de tokens.
- **Testes de Resiliência (Retries)**: Usar *mocks* na chamada HTTP (ex: WireMock ou Mockito para a interface da OpenAI) para forçar erros `500` e `429` (Rate Limit) e validar se o job realmente entra em status de repetição com atraso progressivo (Exponential Backoff).
- **Testes de Fluxo Completo**: Testar localmente a trigger do Cron injetando uma URL controlada para verificar se o dado chega até o estágio final (salvo no banco/vector store) com status `completed`.
