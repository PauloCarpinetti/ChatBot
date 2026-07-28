package br.com.paulo.chatbot.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class TenantContextHolder {

    public static UUID getCurrentTenantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof TenantAuthenticationToken) {
            return ((TenantAuthenticationToken) authentication).getTenantId();
        }
        throw new UnauthorizedException("Tenant ID not found in security context");
    }
}
