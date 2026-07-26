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

    @jakarta.annotation.PostConstruct
    public void initWeaviateSchema() {
        try {
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            
            // Check if schema exists
            java.net.http.HttpRequest checkRequest = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(weaviateScheme + "://" + weaviateHost + ":" + weaviatePort + "/v1/schema/DocumentChunk"))
                    .GET()
                    .build();
            
            java.net.http.HttpResponse<String> checkResponse = client.send(checkRequest, java.net.http.HttpResponse.BodyHandlers.ofString());
            
            if (checkResponse.statusCode() == 404) {
                // Create schema
                String schemaJson = """
                    {
                      "class": "DocumentChunk",
                      "description": "A chunk of text from a document",
                      "vectorizer": "text2vec-openai",
                      "moduleConfig": {
                        "text2vec-openai": {
                          "model": "text-embedding-3-small"
                        }
                      },
                      "properties": [
                        {
                          "name": "text",
                          "dataType": ["text"],
                          "description": "The text content"
                        },
                        {
                          "name": "tenant_id",
                          "dataType": ["text"],
                          "description": "The tenant ID this chunk belongs to",
                          "indexFilterable": true,
                          "indexSearchable": true
                        },
                        {
                          "name": "file_name",
                          "dataType": ["text"],
                          "description": "The original file name"
                        }
                      ]
                    }
                    """;
                
                java.net.http.HttpRequest createRequest = java.net.http.HttpRequest.newBuilder()
                        .uri(java.net.URI.create(weaviateScheme + "://" + weaviateHost + ":" + weaviatePort + "/v1/schema"))
                        .header("Content-Type", "application/json")
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(schemaJson))
                        .build();
                
                java.net.http.HttpResponse<String> createResponse = client.send(createRequest, java.net.http.HttpResponse.BodyHandlers.ofString());
                System.out.println("Weaviate Schema creation status: " + createResponse.statusCode());
                System.out.println("Weaviate Schema creation response: " + createResponse.body());
            } else {
                System.out.println("Weaviate Schema already exists.");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize Weaviate schema: " + e.getMessage());
        }
    }
}
