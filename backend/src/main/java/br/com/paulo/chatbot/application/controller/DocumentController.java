package br.com.paulo.chatbot.application.controller;

import br.com.paulo.chatbot.domain.ai.DocumentIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentIngestionService ingestionService;

    public DocumentController(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tenantId") UUID tenantId) {
        
        // No cenário real, o tenantId costuma vir de um Token JWT logado.
        ingestionService.ingestPdf(file, tenantId);
        
        return ResponseEntity.ok("Documento processado e indexado com sucesso para o tenant: " + tenantId);
    }

    public static class TextUploadRequest {
        public String tenantId;
        public String text;
        public String sourceUrl;
    }

    @PostMapping("/text")
    public ResponseEntity<String> uploadText(@RequestBody TextUploadRequest request) {
        ingestionService.ingestText(
                request.text, 
                request.sourceUrl, 
                UUID.fromString(request.tenantId)
        );
        return ResponseEntity.ok("Texto processado e indexado com sucesso para o tenant: " + request.tenantId);
    }
}
