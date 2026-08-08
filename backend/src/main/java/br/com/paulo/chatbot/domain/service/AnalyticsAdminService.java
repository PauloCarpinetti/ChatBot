package br.com.paulo.chatbot.domain.service;

import br.com.paulo.chatbot.application.dto.ChatSessionResponse;
import br.com.paulo.chatbot.application.dto.DashboardMetricsResponse;
import br.com.paulo.chatbot.domain.repository.ChatSessionRepository;
import br.com.paulo.chatbot.domain.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsAdminService {

    private final ChatSessionRepository chatSessionRepository;
    private final TenantRepository tenantRepository;

    public AnalyticsAdminService(ChatSessionRepository chatSessionRepository, TenantRepository tenantRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.tenantRepository = tenantRepository;
    }

    public DashboardMetricsResponse getMetrics() {
        long totalTenants = tenantRepository.count();
        long activeChats = chatSessionRepository.count(); // Basic implementation
        long apiCallsToday = activeChats * 2; // Mock calculation
        long averageResponseTime = 450; // Mock calculation

        return new DashboardMetricsResponse(totalTenants, activeChats, apiCallsToday, averageResponseTime);
    }

    public List<ChatSessionResponse> getRecentChats() {
        // Here we ideally want to fetch the last 50 chats or so.
        return chatSessionRepository.findAll().stream()
                .limit(50)
                .map(ChatSessionResponse::from)
                .collect(Collectors.toList());
    }
}
