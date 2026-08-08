package br.com.paulo.chatbot.application.dto;

import br.com.paulo.chatbot.domain.model.ChatSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ChatSessionResponse(
    String id,
    String tenantId,
    String userId,
    String status,
    LocalDateTime startedAt,
    List<ChatMessageResponse> messages
) {
    public static ChatSessionResponse from(ChatSession session) {
        return new ChatSessionResponse(
            session.getId().toString(),
            session.getTenantId().toString(),
            session.getUserIdentifier(),
            "completed", // MVP static status
            session.getCreatedAt(),
            List.of()
        );
    }
}
