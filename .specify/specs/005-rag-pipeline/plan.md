# Plano de Implementação - Spec 005

1. **Configuração de Beans (`AiConfig`)**:
   - Usar as dependências `langchain4j-open-ai-spring-boot-starter` e `langchain4j-weaviate`.
   - Adicionar as chaves (`openai.api-key`, `weaviate.url`) no `application.yml`.

2. **Criação do JpaChatMemoryStore**:
   - O `memoryId` passado pelas interfaces do LangChain4j representará o `sessionId` do nosso contexto.
   - Utilizar o `TenantContextHolder` para recuperar o `tenantId` e garantir isolamento rígido ao buscar e atualizar o chat history no banco MySQL (via `ChatMessageRepository`).
   - Mapear corretamente o tipo (ROLE) do LangChain4j para nossa string `role` (user/assistant) e vice-versa.

3. **Orquestração RAG (`ChatOrchestratorService`)**:
   - Utilizar a API programática (fluent API) do LangChain4j, que permite montar o RAG dinâmico (`RetrievalAugmentor`, `ConversationalRetrievalChain` ou a nova API `AiServices` com configuração programática).
   - O segredo é garantir que o Weaviate Filter aplique estritamente o `tenantId` da requisição atual.

4. **Testes Unitários/Integrados**:
   - Testar o mapeamento de entidades.
   - Testar que os filtros do Weaviate estão sendo injetados com o TenantID apropriado.
