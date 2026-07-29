package br.com.paulo.chatbot.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "session_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID sessionId;

    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "tenant_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID tenantId;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", insertable = false, updatable = false)
    private ChatSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    private Tenant tenant;

    public ChatMessage() {
    }

    public ChatMessage(UUID id, UUID sessionId, UUID tenantId, String role, String content, LocalDateTime createdAt, ChatSession session, Tenant tenant) {
        this.id = id;
        this.sessionId = sessionId;
        this.tenantId = tenantId;
        this.role = role;
        this.content = content;
        this.createdAt = createdAt;
        this.session = session;
        this.tenant = tenant;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ChatSession getSession() {
        return session;
    }

    public void setSession(ChatSession session) {
        this.session = session;
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

    public static ChatMessageBuilder builder() {
        return new ChatMessageBuilder();
    }

    public static class ChatMessageBuilder {
        private UUID id;
        private UUID sessionId;
        private UUID tenantId;
        private String role;
        private String content;
        private LocalDateTime createdAt;
        private ChatSession session;
        private Tenant tenant;

        public ChatMessageBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ChatMessageBuilder sessionId(UUID sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public ChatMessageBuilder tenantId(UUID tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public ChatMessageBuilder role(String role) {
            this.role = role;
            return this;
        }

        public ChatMessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public ChatMessageBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ChatMessageBuilder session(ChatSession session) {
            this.session = session;
            return this;
        }

        public ChatMessageBuilder tenant(Tenant tenant) {
            this.tenant = tenant;
            return this;
        }

        public ChatMessage build() {
            return new ChatMessage(id, sessionId, tenantId, role, content, createdAt, session, tenant);
        }
    }
}
