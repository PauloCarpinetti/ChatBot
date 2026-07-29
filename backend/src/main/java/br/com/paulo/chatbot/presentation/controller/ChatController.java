package br.com.paulo.chatbot.presentation.controller;

import br.com.paulo.chatbot.application.ChatOrchestratorService;
import br.com.paulo.chatbot.domain.model.ChatMessage;
import br.com.paulo.chatbot.domain.model.ChatSession;
import br.com.paulo.chatbot.domain.repository.ChatMessageRepository;
import br.com.paulo.chatbot.domain.repository.ChatSessionRepository;
import br.com.paulo.chatbot.presentation.dto.ChatRequest;
import br.com.paulo.chatbot.presentation.dto.ChatResponse;
import br.com.paulo.chatbot.security.TenantContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatOrchestratorService chatOrchestratorService;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatController(ChatOrchestratorService chatOrchestratorService,
                          ChatSessionRepository chatSessionRepository,
                          ChatMessageRepository chatMessageRepository) {
        this.chatOrchestratorService = chatOrchestratorService;
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("A mensagem não pode estar vazia");
        }

        UUID tenantId = TenantContextHolder.getCurrentTenantId();
        UUID sessionId = request.getSessionId();

        if (sessionId == null) {
            sessionId = UUID.randomUUID();
            ChatSession newSession = new ChatSession();
            newSession.setId(sessionId);
            newSession.setTenantId(tenantId);
            newSession.setUserIdentifier("anonymous"); // Simulating frontend user identifier for now
            chatSessionRepository.save(newSession);
        } else {
            // Verify if the session belongs to this tenant, or just save if doesn't exist
            if (chatSessionRepository.findByIdAndTenantId(sessionId, tenantId).isEmpty()) {
                ChatSession newSession = new ChatSession();
                newSession.setId(sessionId);
                newSession.setTenantId(tenantId);
                newSession.setUserIdentifier("anonymous");
                chatSessionRepository.save(newSession);
            }
        }

        String responseText = chatOrchestratorService.processMessage(sessionId, request.getMessage());

        ChatResponse response = new ChatResponse(
                sessionId,
                responseText,
                Instant.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sessions/{sessionId}/history")
    public ResponseEntity<List<Map<String, String>>> getHistory(@PathVariable UUID sessionId) {
        UUID tenantId = TenantContextHolder.getCurrentTenantId();
        
        List<ChatMessage> history = chatMessageRepository.findBySessionIdAndTenantIdOrderByCreatedAtAsc(sessionId, tenantId);

        List<Map<String, String>> formattedHistory = history.stream().map(msg -> {
            Map<String, String> map = new HashMap<>();
            map.put("role", msg.getRole());
            map.put("content", msg.getContent());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(formattedHistory);
    }
}
