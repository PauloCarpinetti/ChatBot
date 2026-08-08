package br.com.paulo.chatbot.domain.service;

import br.com.paulo.chatbot.application.dto.TenantCreateRequest;
import br.com.paulo.chatbot.application.dto.TenantResponse;
import br.com.paulo.chatbot.application.dto.TenantUpdateRequest;
import br.com.paulo.chatbot.domain.model.Tenant;
import br.com.paulo.chatbot.domain.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TenantAdminService {

    private final TenantRepository tenantRepository;

    public TenantAdminService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<TenantResponse> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(TenantResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TenantResponse createTenant(TenantCreateRequest request) {
        String generatedApiKey = "sk-" + UUID.randomUUID().toString();
        Tenant tenant = Tenant.builder()
                .name(request.name())
                .apiKey(generatedApiKey)
                .systemPrompt(request.systemPrompt())
                .build();
        
        tenant = tenantRepository.save(tenant);
        return TenantResponse.from(tenant);
    }

    @Transactional
    public TenantResponse updateTenant(UUID id, TenantUpdateRequest request) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
        
        tenant.setName(request.name());
        if (request.systemPrompt() != null) {
            tenant.setSystemPrompt(request.systemPrompt());
        }
        
        tenant = tenantRepository.save(tenant);
        return TenantResponse.from(tenant);
    }

    @Transactional
    public void deleteTenant(UUID id) {
        tenantRepository.deleteById(id);
    }
}
