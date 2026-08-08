package br.com.paulo.chatbot.application.controller.admin;

import br.com.paulo.chatbot.application.dto.TenantCreateRequest;
import br.com.paulo.chatbot.application.dto.TenantResponse;
import br.com.paulo.chatbot.application.dto.TenantUpdateRequest;
import br.com.paulo.chatbot.domain.service.TenantAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/tenants")
public class TenantController {

    private final TenantAdminService tenantAdminService;

    public TenantController(TenantAdminService tenantAdminService) {
        this.tenantAdminService = tenantAdminService;
    }

    @GetMapping
    public List<TenantResponse> getAllTenants() {
        return tenantAdminService.getAllTenants();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TenantResponse createTenant(@RequestBody TenantCreateRequest request) {
        return tenantAdminService.createTenant(request);
    }

    @PutMapping("/{id}")
    public TenantResponse updateTenant(@PathVariable UUID id, @RequestBody TenantUpdateRequest request) {
        return tenantAdminService.updateTenant(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTenant(@PathVariable UUID id) {
        tenantAdminService.deleteTenant(id);
    }
}
