package br.com.paulo.chatbot.application;

import br.com.paulo.chatbot.domain.ai.JpaChatMemoryStore;
import br.com.paulo.chatbot.domain.model.Tenant;
import br.com.paulo.chatbot.domain.repository.TenantRepository;
import br.com.paulo.chatbot.security.TenantContextHolder;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Service
public class ChatOrchestratorService {

    private final ChatLanguageModel chatLanguageModel;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final JpaChatMemoryStore chatMemoryStore;
    private final TenantRepository tenantRepository;
    private final MeterRegistry meterRegistry;

    public ChatOrchestratorService(ChatLanguageModel chatLanguageModel, 
                                   StreamingChatLanguageModel streamingChatLanguageModel,
                                   EmbeddingModel embeddingModel, 
                                   EmbeddingStore<TextSegment> embeddingStore, 
                                   JpaChatMemoryStore chatMemoryStore,
                                   TenantRepository tenantRepository,
                                   MeterRegistry meterRegistry) {
        this.chatLanguageModel = chatLanguageModel;
        this.streamingChatLanguageModel = streamingChatLanguageModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.chatMemoryStore = chatMemoryStore;
        this.tenantRepository = tenantRepository;
        this.meterRegistry = meterRegistry;
    }

    interface Assistant {
        String chat(String userMessage);
    }
    
    interface StreamingAssistant {
        TokenStream chat(String userMessage);
    }

    @Timed(value = "chat.response.time", description = "Time taken to generate chat response")
    public String processMessage(UUID sessionId, String userText) {
        meterRegistry.counter("chat.requests.total", "type", "sync").increment();
        return buildAssistant(sessionId, Assistant.class).chat(userText);
    }
    
    @Timed(value = "chat.stream.response.time", description = "Time taken to generate streaming chat response")
    public TokenStream streamMessage(UUID sessionId, String userText) {
        meterRegistry.counter("chat.requests.total", "type", "stream").increment();
        return buildStreamingAssistant(sessionId).chat(userText);
    }
    
    private Assistant buildAssistant(UUID sessionId, Class<Assistant> clazz) {
        UUID tenantId = TenantContextHolder.getCurrentTenantId();
        String systemPrompt = getSystemPrompt(tenantId);
        return AiServices.builder(clazz)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(buildRetriever(tenantId))
                .chatMemory(buildChatMemory(sessionId, tenantId))
                .systemMessageProvider(chatMemoryId -> systemPrompt)
                .build();
    }
    
    private StreamingAssistant buildStreamingAssistant(UUID sessionId) {
        UUID tenantId = TenantContextHolder.getCurrentTenantId();
        String systemPrompt = getSystemPrompt(tenantId);
        return AiServices.builder(StreamingAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .contentRetriever(buildRetriever(tenantId))
                .chatMemory(buildChatMemory(sessionId, tenantId))
                .systemMessageProvider(chatMemoryId -> systemPrompt)
                .build();
    }
    
    private String getSystemPrompt(UUID tenantId) {
        return tenantRepository.findById(tenantId)
                .map(Tenant::getSystemPrompt)
                .orElse("Você é um assistente virtual prestativo.");
    }
    
    private EmbeddingStoreContentRetriever buildRetriever(UUID tenantId) {
        Filter tenantFilter = metadataKey("tenant_id").isEqualTo(tenantId.toString());
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .filter(tenantFilter)
                .maxResults(3)
                .minScore(0.6)
                .build();
    }
    
    private MessageWindowChatMemory buildChatMemory(UUID sessionId, UUID tenantId) {
        return MessageWindowChatMemory.builder()
                .chatMemoryStore(chatMemoryStore)
                .id(sessionId.toString() + "_" + tenantId.toString())
                .maxMessages(10)
                .build();
    }
}
