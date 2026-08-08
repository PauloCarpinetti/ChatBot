package br.com.paulo.chatbot.application.dto;

public record DashboardMetricsResponse(
    long totalTenants,
    long activeChats,
    long apiCallsToday,
    long averageResponseTime
) {}
