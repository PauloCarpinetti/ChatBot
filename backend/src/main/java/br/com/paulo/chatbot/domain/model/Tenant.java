package br.com.paulo.chatbot.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "tenants")
public class Tenant {
    @Id
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "api_key", nullable = false, unique = true)
    @JsonIgnore
    private String apiKey;

    @Column(name = "system_prompt", columnDefinition = "TEXT")
    private String systemPrompt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Tenant() {
    }

    public Tenant(UUID id, String name, String apiKey, String systemPrompt, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.apiKey = apiKey;
        this.systemPrompt = systemPrompt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public static TenantBuilder builder() {
        return new TenantBuilder();
    }

    public static class TenantBuilder {
        private UUID id;
        private String name;
        private String apiKey;
        private String systemPrompt;
        private LocalDateTime createdAt;

        public TenantBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public TenantBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TenantBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public TenantBuilder systemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
            return this;
        }

        public TenantBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Tenant build() {
            return new Tenant(id, name, apiKey, systemPrompt, createdAt);
        }
    }
}
