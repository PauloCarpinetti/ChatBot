# Relatório de Atividades - 26/07/2026

## 1. Conclusão da Spec 004 (RAG com Weaviate)
- O fluxo de ponta a ponta do RAG (Retrieve and Generate) foi finalizado.
- Foi resolvido o problema de incompatibilidade (`JpaSystemException`) do `UUID` gerado pelo Hibernate 6 no MySQL usando a anotação `@JdbcTypeCode(java.sql.Types.VARCHAR)` nas entidades.
- A branch `004-rag-integration` foi finalizada e mesclada na `main`.

## 2. Inicialização da Spec 003 (Core IA - Background Processor)
- **Branch e Speckit:** Criada a branch `003-core-ia-processor`. Os arquivos de especificação (Spec, Plan, Tasks) foram gerados e posteriormente alocados corretamente no diretório oficial `.specify/specs/003-core-ia-processor/`.
- **Definição de Arquitetura:**
  - Foi definido que a "Cloud Function" para scraping não usaria o `@Scheduled` do Java, mas sim o **Google Cloud Functions**.
  - A linguagem escolhida foi **Node.js** com a biblioteca **Cheerio** para alta performance na limpeza do HTML e manipulação do DOM.

## 3. Implementações Realizadas (Spec 003)
- **Sanitização de HTML:** Lógica criada para remover metadados, scripts, navegações e rodapés (`<script>`, `<style>`, `<nav>`, `<footer>`).
- **Otimização de Custos (Token Limit):** O texto extraído é sempre truncado de forma segura em 8.000 caracteres antes de ser enviado para os modelos, economizando Context Window e custos da OpenAI.
- **Resiliência e Exponential Backoff:**
  - Desenvolvida lógica nativa de repetições em caso de falha da API da OpenAI ou Timeout.
  - A repetição respeita intervalos progressivos (1min, 5min, 15min) atualizando a tabela `scraping_jobs` com status `PENDING_RETRY`.
  - Max Retries configurado para 3 tentativas antes de marcar a URL como `FAILED`.
- **Testes Automatizados (Jest):** Implementados testes unitários validando a sanitização e integração com _mocks_ validando perfeitamente a lógica de Retries e resiliência (Evitando a necessidade de instanciar a rede completa no Docker em tempo de dev).
- **Backend (Spring Boot):** 
  - Adicionado script Flyway (`V3__scraping_jobs_schema.sql`) gerando a tabela de fila para leitura e persistência.
  - Criado o Endpoint `POST /api/documents/text` no Java (e respectivo serviço `ingestText`) para o Cloud Function cuspir os dados brutos a serem transformados em vetores no Weaviate.

## 4. Próximos Passos (Para a Próxima Sessão)
- Ligar a infraestrutura (Docker, MySQL, Weaviate).
- Testar E2E local: inserir um registro na tabela `scraping_jobs`, rodar a function e acompanhar a injeção vetorial.
- Iniciar homologação do fluxo completo, ou partir para a revisão e merge da Spec 003.
