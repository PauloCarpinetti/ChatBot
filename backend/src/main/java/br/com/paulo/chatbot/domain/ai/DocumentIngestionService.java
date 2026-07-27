package br.com.paulo.chatbot.domain.ai;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class DocumentIngestionService {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;

    public DocumentIngestionService(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
    }

    public void ingestPdf(MultipartFile file, UUID tenantId) {
        try (InputStream inputStream = file.getInputStream()) {
            DocumentParser parser = new ApachePdfBoxDocumentParser();
            Document document = parser.parse(inputStream);
            
            // Injeta a chave de isolamento no Metadata do documento.
            // Os TextSegments criados pelo splitter vão herdar essa metadata,
            // garantindo que o Weaviate filtre adequadamente depois.
            document.metadata().put("tenant_id", tenantId.toString());
            document.metadata().put("file_name", file.getOriginalFilename());

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(DocumentSplitters.recursive(1000, 200))
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();

            System.out.println("Document text length: " + document.text().length());
            System.out.println("Starting ingestion into Weaviate...");
            ingestor.ingest(document);
            System.out.println("Ingestion completed successfully!");
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o PDF: " + e.getMessage(), e);
        }
    }

    public void ingestText(String text, String sourceUrl, UUID tenantId) {
        try {
            Document document = new Document(text);
            document.metadata().put("tenant_id", tenantId.toString());
            document.metadata().put("source_url", sourceUrl);

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(DocumentSplitters.recursive(1000, 200))
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();

            System.out.println("Ingesting raw text from: " + sourceUrl + " (length: " + text.length() + ")");
            ingestor.ingest(document);
            System.out.println("Text ingestion completed successfully!");
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar texto web: " + e.getMessage(), e);
        }
    }
}
