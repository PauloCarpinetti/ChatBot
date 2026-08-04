package br.com.paulo.chatbot.domain.service;

import br.com.paulo.chatbot.domain.ai.DocumentIngestionService;
import br.com.paulo.chatbot.domain.model.Tenant;
import br.com.paulo.chatbot.domain.repository.TenantRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class DatabaseSeederService {

    private final TenantRepository tenantRepository;
    private final DocumentIngestionService ingestionService;

    public DatabaseSeederService(TenantRepository tenantRepository, DocumentIngestionService ingestionService) {
        this.tenantRepository = tenantRepository;
        this.ingestionService = ingestionService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedDatabase() {
        System.out.println("Checking if database seeding is required...");
        
        seedTenant("Acme Corp", "acme-api-key-123", "acme", "Você é o assistente virtual da Acme Corp. Responda apenas sobre logística e prazos conforme os documentos.");
        seedTenant("BioHealth", "biohealth-api-key-123", "biohealth", "Você é o assistente virtual da BioHealth. Priorize regras de carência e contatos de emergência (0800-BIO-1234).");
        seedTenant("Initech", "initech-api-key-123", "initech", "Você é o assistente virtual da Initech. Foco em licenciamento de software corporativo e suporte 24/7.");
        
        System.out.println("Seeding process finished.");
    }

    private void seedTenant(String name, String apiKey, String filePrefix, String systemPrompt) {
        Tenant tenant = tenantRepository.findByName(name).orElseGet(() -> {
            System.out.println("Creating Tenant: " + name);
            Tenant newTenant = Tenant.builder()
                    .id(UUID.randomUUID())
                    .name(name)
                    .apiKey(apiKey)
                    .systemPrompt(systemPrompt)
                    .build();
            return tenantRepository.save(newTenant);
        });

        // Tenta ingerir os arquivos markdown se eles existirem
        try {
            // Assume que estamos rodando da raiz ou que docs/samples está disponível
            Path mdPath = Paths.get("../docs/samples/" + filePrefix + ".md");
            Path jsonPath = Paths.get("../docs/samples/" + filePrefix + ".json");
            
            // Fallback para caso rode de outro working dir
            if (!Files.exists(mdPath)) {
                mdPath = Paths.get("docs/samples/" + filePrefix + ".md");
                jsonPath = Paths.get("docs/samples/" + filePrefix + ".json");
            }

            if (Files.exists(mdPath) && Files.exists(jsonPath)) {
                System.out.println("Found documents for " + name + ". Ingesting...");
                String markdownContent = Files.readString(mdPath);
                
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> metadata = objectMapper.readValue(
                        Files.readString(jsonPath), 
                        new TypeReference<Map<String, String>>() {}
                );
                
                ingestionService.ingestMarkdown(markdownContent, filePrefix + ".md", metadata, tenant.getId());
            } else {
                System.out.println("Documents for " + name + " not found at " + mdPath.toAbsolutePath());
            }
            
        } catch (Exception e) {
            System.err.println("Error seeding documents for " + name + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
