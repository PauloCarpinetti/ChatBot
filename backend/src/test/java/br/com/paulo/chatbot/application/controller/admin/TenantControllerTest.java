package br.com.paulo.chatbot.application.controller.admin;

import br.com.paulo.chatbot.application.dto.TenantCreateRequest;
import br.com.paulo.chatbot.application.dto.TenantResponse;
import br.com.paulo.chatbot.domain.service.TenantAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantControllerTest {

    @Mock
    private TenantAdminService tenantAdminService;

    @InjectMocks
    private TenantController tenantController;

    @Test
    void testCreateTenant() {
        TenantCreateRequest request = new TenantCreateRequest("Test Corp", "Prompt");
        TenantResponse expected = new TenantResponse("id", "Test Corp", "active", "key", "Prompt", LocalDateTime.now());

        when(tenantAdminService.createTenant(any())).thenReturn(expected);

        TenantResponse response = tenantController.createTenant(request);
        assertEquals("Test Corp", response.name());
    }

    @Test
    void testGetAllTenants() {
        TenantResponse expected = new TenantResponse("id", "Test Corp", "active", "key", "Prompt", LocalDateTime.now());

        when(tenantAdminService.getAllTenants()).thenReturn(List.of(expected));

        List<TenantResponse> responses = tenantController.getAllTenants();
        assertEquals(1, responses.size());
        assertEquals("Test Corp", responses.get(0).name());
    }
}
