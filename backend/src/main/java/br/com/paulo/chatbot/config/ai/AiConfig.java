package br.com.paulo.chatbot.config.ai;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AiConfig {

    @Value("${langchain4j.open-ai.chat-model.api-key:demo}")
    private String openAiApiKey;

    @Value("${chatbot.weaviate.host:localhost}")
    private String weaviateHost;

    @Value("${chatbot.weaviate.port:8090}")
    private String weaviatePort;

    @Value("${chatbot.weaviate.scheme:http}")
    private String weaviateScheme;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName("gpt-4o-mini")
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(openAiApiKey)
                .modelName("text-embedding-3-small")
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return WeaviateEmbeddingStore.builder()
                .host(weaviateHost + ":" + weaviatePort)
                .scheme(weaviateScheme)
                .objectClass("KnowledgeBase")
                .build();
    }
}
