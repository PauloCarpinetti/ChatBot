package br.com.paulo.chatbot.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "chat_sessions", indexes = {
    @Index(name = "idx_tenant_user", columnList = "tenant_id, user_identifier")
})
public class ChatSession {
    @Id
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "tenant_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID tenantId;

    @Column(name = "user_identifier", nullable = false)
    private String userIdentifier;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    private Tenant tenant;

    public ChatSession() {
    }

    public ChatSession(UUID id, UUID tenantId, String userIdentifier, LocalDateTime createdAt, Tenant tenant) {
        this.id = id;
        this.tenantId = tenantId;
        this.userIdentifier = userIdentifier;
        this.createdAt = createdAt;
        this.tenant = tenant;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public static ChatSessionBuilder builder() {
        return new ChatSessionBuilder();
    }

    public static class ChatSessionBuilder {
        private UUID id;
        private UUID tenantId;
        private String userIdentifier;
        private LocalDateTime createdAt;
        private Tenant tenant;

        public ChatSessionBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ChatSessionBuilder tenantId(UUID tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public ChatSessionBuilder userIdentifier(String userIdentifier) {
            this.userIdentifier = userIdentifier;
            return this;
        }

        public ChatSessionBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ChatSessionBuilder tenant(Tenant tenant) {
            this.tenant = tenant;
            return this;
        }

        public ChatSession build() {
            return new ChatSession(id, tenantId, userIdentifier, createdAt, tenant);
        }
    }
}
