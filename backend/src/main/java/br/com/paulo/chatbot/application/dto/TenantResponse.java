package br.com.paulo.chatbot.application.dto;

import br.com.paulo.chatbot.domain.model.Tenant;
import java.time.LocalDateTime;

public record TenantResponse(
    String id,
    String name,
    String status,
    String apiKey,
    String systemPrompt,
    LocalDateTime createdAt
) {
    public static TenantResponse from(Tenant tenant) {
        return new TenantResponse(
            tenant.getId().toString(),
            tenant.getName(),
            "active", // MVP static status
            tenant.getApiKey(),
            tenant.getSystemPrompt(),
            tenant.getCreatedAt()
        );
    }
}
