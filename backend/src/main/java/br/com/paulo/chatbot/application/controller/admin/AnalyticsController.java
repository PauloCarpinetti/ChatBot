package br.com.paulo.chatbot.application.controller.admin;

import br.com.paulo.chatbot.application.dto.ChatSessionResponse;
import br.com.paulo.chatbot.application.dto.DashboardMetricsResponse;
import br.com.paulo.chatbot.domain.service.AnalyticsAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/analytics")
public class AnalyticsController {

    private final AnalyticsAdminService analyticsAdminService;

    public AnalyticsController(AnalyticsAdminService analyticsAdminService) {
        this.analyticsAdminService = analyticsAdminService;
    }

    @GetMapping("/metrics")
    public DashboardMetricsResponse getMetrics() {
        return analyticsAdminService.getMetrics();
    }

    @GetMapping("/chats")
    public List<ChatSessionResponse> getRecentChats() {
        return analyticsAdminService.getRecentChats();
    }
}
