package br.com.paulo.chatbot.application;

import br.com.paulo.chatbot.domain.ai.JpaChatMemoryStore;
import br.com.paulo.chatbot.security.TenantContextHolder;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Service
public class ChatOrchestratorService {

    private final ChatLanguageModel chatLanguageModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final JpaChatMemoryStore chatMemoryStore;

    public ChatOrchestratorService(ChatLanguageModel chatLanguageModel, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore, JpaChatMemoryStore chatMemoryStore) {
        this.chatLanguageModel = chatLanguageModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.chatMemoryStore = chatMemoryStore;
    }

    // Interface interna para o AiServices do LangChain4j
    interface Assistant {
        String chat(String userMessage);
    }

    public String processMessage(UUID sessionId, String userText) {
        UUID tenantId = TenantContextHolder.getCurrentTenantId();

        // Filtro Vetorial Dinâmico para garantir isolamento por tenant
        Filter tenantFilter = metadataKey("tenantId").isEqualTo(tenantId.toString());

        // Configuração do Retriever com o filtro
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .filter(tenantFilter)
                .maxResults(3)
                .minScore(0.7)
                .build();

        // Configuração do Histórico de Conversa (Chat Memory)
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryStore(chatMemoryStore)
                .id(sessionId)
                .maxMessages(10)
                .build();

        // Montagem do RAG programaticamente
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(contentRetriever)
                .chatMemory(chatMemory)
                .build();

        // Executar a chamada para o LLM
        return assistant.chat(userText);
    }
}
