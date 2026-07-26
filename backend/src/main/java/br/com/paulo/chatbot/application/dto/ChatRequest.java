package br.com.paulo.chatbot.application.dto;

import java.util.UUID;

public record ChatRequest(
        UUID tenantId,
        UUID sessionId,
        String userIdentifier,
        String message
) {}
