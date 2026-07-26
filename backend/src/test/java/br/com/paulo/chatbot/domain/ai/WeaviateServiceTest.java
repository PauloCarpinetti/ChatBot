package br.com.paulo.chatbot.domain.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeaviateServiceTest {

    @Mock
    private EmbeddingStore<TextSegment> embeddingStore;

    @Mock
    private EmbeddingModel embeddingModel;

    @InjectMocks
    private WeaviateService weaviateService;

    @Test
    void searchRelevantChunks_Success() {
        String tenantId = "tenant-123";
        String query = "test query";

        Embedding embedding = new Embedding(new float[]{0.1f, 0.2f});
        when(embeddingModel.embed(query)).thenReturn(new Response<>(embedding));

        TextSegment segment = TextSegment.from("Document 1");
        EmbeddingMatch<TextSegment> match = new EmbeddingMatch<>(1.0, "doc1", embedding, segment);
        EmbeddingSearchResult<TextSegment> searchResult = new EmbeddingSearchResult<>(List.of(match));
        
        when(embeddingStore.search(any(EmbeddingSearchRequest.class))).thenReturn(searchResult);

        List<String> results = weaviateService.searchRelevantChunks(tenantId, query, 3);

        assertEquals(1, results.size());
        assertEquals("Document 1", results.get(0));

        ArgumentCaptor<EmbeddingSearchRequest> captor = ArgumentCaptor.forClass(EmbeddingSearchRequest.class);
        verify(embeddingStore).search(captor.capture());
        
        EmbeddingSearchRequest request = captor.getValue();
        assertEquals(3, request.maxResults());
    }
}
