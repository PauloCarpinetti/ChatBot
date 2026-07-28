package br.com.paulo.chatbot.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TenantContextHolderTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentTenantId_WhenAuthenticated_ReturnsTenantId() {
        UUID tenantId = UUID.randomUUID();
        TenantAuthenticationToken token = new TenantAuthenticationToken(tenantId);
        SecurityContextHolder.getContext().setAuthentication(token);

        UUID result = TenantContextHolder.getCurrentTenantId();

        assertEquals(tenantId, result);
    }

    @Test
    void testGetCurrentTenantId_WhenNotAuthenticated_ThrowsUnauthorizedException() {
        SecurityContextHolder.clearContext();

        assertThrows(UnauthorizedException.class, () -> {
            TenantContextHolder.getCurrentTenantId();
        });
    }
}
