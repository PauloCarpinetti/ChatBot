package br.com.paulo.chatbot.application.dto;

import java.time.LocalDateTime;

public record ChatMessageResponse(
    String id,
    String role,
    String content,
    LocalDateTime timestamp
) {}
