package br.com.paulo.chatbot.domain.ai;

import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${chatbot.weaviate.host}")
    private String weaviateHost;

    @Value("${chatbot.weaviate.port}")
    private Integer weaviatePort;

    @Value("${chatbot.weaviate.scheme}")
    private String weaviateScheme;

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return WeaviateEmbeddingStore.builder()
                .scheme(weaviateScheme)
                .host(weaviateHost + ":" + weaviatePort)
                .objectClass("DocumentChunk")
                .avoidDups(true)
                .consistencyLevel("ALL")
                .build();
    }
}
