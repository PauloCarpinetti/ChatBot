package br.com.paulo.chatbot.domain.ai;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.store.embedding.filter.Filter;
import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeaviateService {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;

    public WeaviateService(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
    }

    public List<String> searchRelevantChunks(String tenantId, String query, int maxResults) {
        // 1. Embed the query
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        // 2. Filter by tenant_id
        Filter tenantFilter = metadataKey("tenant_id").isEqualTo(tenantId);

        // 3. Search
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .filter(tenantFilter)
                .maxResults(maxResults)
                .build();

        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);

        return result.matches().stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.toList());
    }
}
