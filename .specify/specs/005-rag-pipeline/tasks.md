# Tasks para Spec 005

- [ ] Criar `AiConfig` em `br.com.paulo.chatbot.config.ai`.
- [ ] Definir os beans de `ChatLanguageModel`, `EmbeddingModel`, `EmbeddingStore` (Weaviate).
- [ ] Criar classe `JpaChatMemoryStore` e implementar a lógica de `getMessages` e `updateMessages`.
- [ ] Criar `ChatOrchestratorService` em `br.com.paulo.chatbot.application` para processar a lógica do RAG.
- [ ] Implementar os testes (`JpaChatMemoryStoreTest`, `ChatOrchestratorServiceTest`).
