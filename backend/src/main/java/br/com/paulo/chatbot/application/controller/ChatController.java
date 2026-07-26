package br.com.paulo.chatbot.application.controller;

import br.com.paulo.chatbot.application.dto.ChatRequest;
import br.com.paulo.chatbot.application.dto.ChatResponse;
import br.com.paulo.chatbot.domain.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public ChatResponse ask(@RequestBody ChatRequest request) {
        // No futuro, tenantId e userIdentifier virão do Token JWT
        String answer = chatService.processMessage(
                request.tenantId(),
                request.sessionId(),
                request.userIdentifier(),
                request.message()
        );
        return new ChatResponse(answer);
    }
}
