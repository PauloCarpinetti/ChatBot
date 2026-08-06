package br.com.paulo.chatbot.presentation.controller;

import br.com.paulo.chatbot.application.ChatOrchestratorService;
import br.com.paulo.chatbot.domain.model.ChatMessage;
import br.com.paulo.chatbot.domain.model.ChatSession;
import br.com.paulo.chatbot.domain.repository.ChatMessageRepository;
import br.com.paulo.chatbot.domain.repository.ChatSessionRepository;
import br.com.paulo.chatbot.presentation.dto.ChatRequest;
import br.com.paulo.chatbot.presentation.dto.ChatResponse;
import br.com.paulo.chatbot.security.TenantContextHolder;
import dev.langchain4j.service.TokenStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
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

    private UUID getOrCreateSession(UUID sessionId, UUID tenantId) {
        if (sessionId == null) {
            sessionId = UUID.randomUUID();
            ChatSession newSession = new ChatSession();
            newSession.setId(sessionId);
            newSession.setTenantId(tenantId);
            newSession.setUserIdentifier("anonymous");
            chatSessionRepository.save(newSession);
        } else {
            if (chatSessionRepository.findByIdAndTenantId(sessionId, tenantId).isEmpty()) {
                ChatSession newSession = new ChatSession();
                newSession.setId(sessionId);
                newSession.setTenantId(tenantId);
                newSession.setUserIdentifier("anonymous");
                chatSessionRepository.save(newSession);
            }
        }
        return sessionId;
    }

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("A mensagem não pode estar vazia");
        }

        UUID tenantId = TenantContextHolder.getCurrentTenantId();
        UUID sessionId = getOrCreateSession(request.getSessionId(), tenantId);

        String responseText = chatOrchestratorService.processMessage(sessionId, request.getMessage());

        ChatResponse response = new ChatResponse(
                sessionId,
                responseText,
                Instant.now()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/message/stream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamMessage(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("A mensagem não pode estar vazia");
        }

        UUID tenantId = TenantContextHolder.getCurrentTenantId();
        UUID finalSessionId = getOrCreateSession(request.getSessionId(), tenantId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Session-Id", finalSessionId.toString());

        ResponseBodyEmitter emitter = new ResponseBodyEmitter(120000L); // 2 minutes timeout
        TokenStream tokenStream = chatOrchestratorService.streamMessage(finalSessionId, request.getMessage());

        tokenStream.onNext(token -> {
            try {
                System.out.println("Token emitted: " + token);
                emitter.send(token, MediaType.APPLICATION_OCTET_STREAM);
            } catch (IOException e) {
                // Connection dropped by client
                emitter.completeWithError(e);
            }
        }).onComplete(response -> {
            emitter.complete();
        }).onError(error -> {
            emitter.completeWithError(error);
        }).start();

        return ResponseEntity.ok().headers(headers).body(emitter);
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
