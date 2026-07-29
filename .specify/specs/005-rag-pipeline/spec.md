# Spec: 005 - Pipeline RAG & Integração LLM (LangChain4j)

## 1. Objetivo
Implementar a camada de orquestração de Inteligência Artificial usando langchain4j. Esta spec unifica os modelos de linguagem (OpenAI), o banco de dados vetorial (Weaviate) e o banco relacional (MySQL via ChatMessageRepository da Spec 002) para formar o fluxo de RAG detalhado em rag-data-flow.md. A regra primária é garantir que o contexto vetorial injetado no prompt pertença exclusivamente ao tenant autenticado.

## 2. Configuração de Beans (Modelos e Vetores)
Criar o pacote `br.com.paulo.chatbot.config.ai` e a classe `AiConfig`.
Definir os seguintes Beans Spring (injetando as propriedades do application.yml via @Value):
* **ChatLanguageModel**: Instanciar o OpenAiChatModel (ex: modelo gpt-4o-mini), configurando a API Key.
* **EmbeddingModel**: Instanciar o OpenAiEmbeddingModel (ex: text-embedding-3-small).
* **EmbeddingStore<TextSegment>**: Instanciar o WeaviateEmbeddingStore.
* **Nota de Configuração**: Definir a URL do Weaviate local (Docker) e a chave da API (se configurada). Configurar para o schema/index padrão (ex: KnowledgeBase).

## 3. Integração de Histórico (Custom Chat Memory)
Para que o LangChain4j saiba o que foi dito antes e salve as conversas no nosso MySQL, precisamos criar uma ponte entre a interface dele e o nosso repositório.
* Criar a classe `JpaChatMemoryStore` implementando `ChatMemoryStore` (do LangChain4j).
* **Método getMessages(Object memoryId)**: O memoryId será o nosso sessionId. Deve buscar no ChatMessageRepository (usando também o tenantId extraído do TenantContextHolder) e converter a entidade ChatMessage para objetos ChatMessage do LangChain4j (UserMessage, AiMessage, etc).
* **Método updateMessages(Object memoryId, List<ChatMessage> messages)**: Deve salvar as mensagens novas no MySQL associadas ao sessionId e ao tenantId atual.

## 4. Orquestração do RAG (Camada de Serviço)
Criar a classe principal `ChatOrchestratorService` no pacote `br.com.paulo.chatbot.application`.
Esta classe não deve usar a anotação declarativa @AiService do LangChain4j de forma rígida, pois precisamos de controle dinâmico de multi-tenancy.
Fluxo do método `String processMessage(UUID sessionId, String userText)`:
* **Contexto de Segurança**: Resgatar o tenantId chamando TenantContextHolder.getCurrentTenantId() (da Spec 003).
* **Filtro Vetorial Dinâmico**: Construir um Filter do LangChain4j exigindo que a chave de metadados tenantId seja igual ao UUID retornado acima. Exemplo: `metadataKey("tenantId").isEqualTo(tenantId.toString())`
* **Content Retriever**: Instanciar um EmbeddingStoreContentRetriever passando o EmbeddingStore, o EmbeddingModel, e aplicando o Filtro criado.
* **Chat Memory**: Instanciar um MessageWindowChatMemory (ex: limite de 10 mensagens), delegando o armazenamento para o nosso JpaChatMemoryStore.
* **Montagem e Chamada**: Utilizar a classe ConversationalRetrievalChain ou RetrievalAugmentor do LangChain4j para amarrar o ChatLanguageModel, a memória e o retriever.
* Executar a cadeia com a mensagem do usuário e retornar a string gerada pelo LLM.

## 5. Critérios de Aceite (Acceptance Criteria)
- [ ] O sistema compila sem erros com as dependências do langchain4j.
- [ ] Isolamento de Tenant no RAG: Uma requisição autenticada como Tenant A nunca deve retornar no log de context (retrieval) o documento do Tenant B do Weaviate.
- [ ] As mensagens enviadas pelo usuário e as respostas do LLM devem aparecer no MySQL, tabela chat_messages, devidamente preenchidas com o tenant_id e o session_id.
- [ ] A ausência de documentos relevantes no Weaviate não deve quebrar o fluxo; o LLM deve receber um contexto vazio e responder naturalmente.
