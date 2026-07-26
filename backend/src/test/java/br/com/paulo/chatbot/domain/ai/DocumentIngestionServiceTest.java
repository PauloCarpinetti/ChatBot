package br.com.paulo.chatbot.domain.ai;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DocumentIngestionServiceTest {

    @Mock
    private EmbeddingStore<TextSegment> embeddingStore;

    @Mock
    private EmbeddingModel embeddingModel;

    @InjectMocks
    private DocumentIngestionService ingestionService;

    @Test
    void ingestPdf_ThrowsException_WhenInvalidPdf() {
        UUID tenantId = UUID.randomUUID();
        // A byte array with invalid PDF data should throw an exception from PDFBox
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "conteúdo pdf inválido".getBytes());

        assertThrows(RuntimeException.class, () -> ingestionService.ingestPdf(file, tenantId));
    }
}
