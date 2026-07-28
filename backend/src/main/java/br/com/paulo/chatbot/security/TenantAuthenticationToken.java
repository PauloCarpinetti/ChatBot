package br.com.paulo.chatbot.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import java.util.UUID;
import java.util.Collections;

public class TenantAuthenticationToken extends AbstractAuthenticationToken {

    private final UUID tenantId;

    public TenantAuthenticationToken(UUID tenantId) {
        super(Collections.emptyList());
        this.tenantId = tenantId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return tenantId;
    }

    public UUID getTenantId() {
        return tenantId;
    }
}
