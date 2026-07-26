package br.com.paulo.chatbot.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_sessions", indexes = {
    @Index(name = "idx_tenant_user", columnList = "tenant_id, user_identifier")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSession {
    @Id
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "tenant_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID tenantId;

    @Column(name = "user_identifier", nullable = false)
    private String userIdentifier;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    private Tenant tenant;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
