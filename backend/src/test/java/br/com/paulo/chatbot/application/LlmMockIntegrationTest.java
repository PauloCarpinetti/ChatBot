package br.com.paulo.chatbot.application;

import br.com.paulo.chatbot.security.TenantAuthenticationToken;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.paulo.chatbot.application.service.RateLimitingService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LlmMockIntegrationTest {

    @MockitoBean
    private RateLimitingService rateLimitingService;

    @Autowired
    private ChatOrchestratorService chatOrchestratorService;

    @Autowired
    private br.com.paulo.chatbot.domain.repository.ChatSessionRepository chatSessionRepository;

    @Autowired
    private br.com.paulo.chatbot.domain.repository.TenantRepository tenantRepository;

    @MockitoBean
    private ChatLanguageModel chatLanguageModel;

    @MockitoBean
    private dev.langchain4j.store.embedding.EmbeddingStore<dev.langchain4j.data.segment.TextSegment> embeddingStore;

    @BeforeEach
    public void setup() {
        TenantAuthenticationToken token = new TenantAuthenticationToken(UUID.randomUUID());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testMockLlmResponse() {
        // Arrange
        String expectedMockResponse = "Olá! Esta é uma resposta simulada para não gastar tokens da OpenAI.";
        dev.langchain4j.model.output.Response<dev.langchain4j.data.message.AiMessage> mockedResponse = 
            dev.langchain4j.model.output.Response.from(new dev.langchain4j.data.message.AiMessage(expectedMockResponse), new dev.langchain4j.model.output.TokenUsage(10, 10), dev.langchain4j.model.output.FinishReason.STOP);
        when(chatLanguageModel.generate(org.mockito.ArgumentMatchers.anyList())).thenReturn(mockedResponse);
        
        when(embeddingStore.search(org.mockito.ArgumentMatchers.any(dev.langchain4j.store.embedding.EmbeddingSearchRequest.class)))
                .thenReturn(new dev.langchain4j.store.embedding.EmbeddingSearchResult<>(java.util.Collections.emptyList()));

        // Act
        UUID tenantId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        br.com.paulo.chatbot.domain.model.Tenant tenant = new br.com.paulo.chatbot.domain.model.Tenant();
        tenant.setId(tenantId);
        tenant.setName("Mock Tenant");
        tenant.setApiKey("test-api-key");
        tenantRepository.save(tenant);

        UUID sessionId = UUID.randomUUID();
        br.com.paulo.chatbot.domain.model.ChatSession session = new br.com.paulo.chatbot.domain.model.ChatSession();
        session.setId(sessionId);
        session.setTenantId(tenantId);
        session.setUserIdentifier("test-user");
        chatSessionRepository.save(session);
        
        String response = chatOrchestratorService.processMessage(sessionId, "Qualquer mensagem do usuário");

        // Assert
        assertEquals(expectedMockResponse, response, "A resposta do serviço deve vir do mock e não da API real.");
    }
}
