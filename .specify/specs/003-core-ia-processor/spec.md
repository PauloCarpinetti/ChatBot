# Spec 003: Core IA (Background Processor)

## 1. Visão Geral
Esta especificação define a **Fase 3: Core IA (Background Processor)**. O objetivo principal é desenvolver um processador em background rodando como uma **Cloud Function agendada (Cron)**. 

Este processador será responsável por buscar informações (Scraping), sanitizar e otimizar os dados, e integrá-los ao fluxo de Inteligência Artificial usando o modelo GPT-4o-mini da OpenAI. O foco central é controlar custos, garantir que a *context window* do modelo não estoure, e criar resiliência em caso de falhas.

## 2. Requisitos Principais

### 2.1 Cloud Function Agendada (Cron)
- Criar uma Cloud Function (ou equivalente *serverless* dependendo do provedor cloud).
- Configurar uma trigger via Cron job para ser executada periodicamente conforme a necessidade de atualização de conteúdo.

### 2.2 Scraping & Sanitization
- **Extração**: Efetuar o *scraping* (coleta) do conteúdo bruto alvo.
- **Limpeza (Sanitization)**:
  - Remover tags HTML inúteis, scripts, metadados irrelevantes ou ruídos da extração.
  - Truncar o texto caso exceda o limite de tokens planejado, mantendo apenas a parte mais relevante (ex: sumário, início do texto) para preservar a relevância sem estourar a *context window*.
- **Otimização de Custos**: Limitar o tamanho do input enviado à OpenAI para minimizar as cobranças (já que o GPT-4o-mini cobra por tokens).

### 2.3 Resiliência e Tratamento de Erros
- Implementar política de **Retentativas (Retry)**:
  - Caso o processamento ou a chamada à OpenAI resulte em falha (`status = failed` ou *Timeouts*/*Rate Limits*), a função deve agendar ou executar novas tentativas com *Exponential Backoff*.
  - Definir um limite máximo de retentativas (ex: 3 vezes) para evitar *loops* infinitos.
  - Registrar erros persistentes em logs (ex: Cloud Logging/Sentry) e/ou alterar o status do processamento no banco de dados para alerta da equipe.

## 3. Critérios de Aceite
1. [ ] A Cloud Function está configurada e rodando no Cron corretamente.
2. [ ] O *Scraping* retorna dados brutos com sucesso.
3. [ ] A Sanitização limpa eficientemente o texto e trunca grandes volumes, respeitando os limites da *context window* (max tokens) configurados para o GPT-4o-mini.
4. [ ] O consumo na OpenAI não apresenta picos inesperados por *over-fetching* de dados.
5. [ ] Em simulação de erro de rede ou indisponibilidade da OpenAI, a Cloud Function retenta o processamento corretamente, marcando como `failed` permanente após esgotar tentativas.

## 4. Considerações Técnicas
- **Linguagem**: A mesma stack que a Cloud Function (Node.js/Python/Java dependendo da decisão arquitetural).
- **Dependências**: Bibliotecas de parsing HTML (como `jsoup` em Java, `BeautifulSoup` em Python, ou `cheerio` em Node.js) e SDK da LangChain4j/OpenAI.
- **Deploy**: O deploy deve seguir as mesmas diretrizes de CI/CD estabelecidas no repositório.
