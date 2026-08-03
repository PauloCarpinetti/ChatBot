package br.com.paulo.chatbot.security;

import br.com.paulo.chatbot.application.ChatOrchestratorService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class TenantLeakageTest {

    @Autowired
    private ChatOrchestratorService chatOrchestratorService;

    @Autowired
    private br.com.paulo.chatbot.domain.repository.ChatSessionRepository chatSessionRepository;

    @Autowired
    private br.com.paulo.chatbot.domain.repository.TenantRepository tenantRepository;

    @MockitoBean
    private ChatLanguageModel chatLanguageModel;

    @MockitoBean
    private EmbeddingStore<TextSegment> embeddingStore;

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testTenantCannotAccessOtherTenantContext() {
        // Arrange
        UUID tenantA = UUID.randomUUID();
        TenantAuthenticationToken token = new TenantAuthenticationToken(tenantA);
        SecurityContextHolder.getContext().setAuthentication(token);

        dev.langchain4j.model.output.Response<dev.langchain4j.data.message.AiMessage> mockedResponse = 
            dev.langchain4j.model.output.Response.from(new dev.langchain4j.data.message.AiMessage("Resposta do Mock"), new dev.langchain4j.model.output.TokenUsage(10, 10), dev.langchain4j.model.output.FinishReason.STOP);
        when(chatLanguageModel.generate(org.mockito.ArgumentMatchers.anyList())).thenReturn(mockedResponse);
        
        // Simular retorno vazio do banco vetorial
        when(embeddingStore.search(org.mockito.ArgumentMatchers.any(EmbeddingSearchRequest.class)))
                .thenReturn(new EmbeddingSearchResult<>(Collections.emptyList()));

        // Act
        br.com.paulo.chatbot.domain.model.Tenant tenant = new br.com.paulo.chatbot.domain.model.Tenant();
        tenant.setId(tenantA);
        tenant.setName("Tenant A");
        tenant.setApiKey("test-api-key");
        tenantRepository.save(tenant);

        UUID sessionId = UUID.randomUUID();
        
        br.com.paulo.chatbot.domain.model.ChatSession session = new br.com.paulo.chatbot.domain.model.ChatSession();
        session.setId(sessionId);
        session.setTenantId(tenantA);
        session.setUserIdentifier("test-user");
        chatSessionRepository.save(session);
        
        chatOrchestratorService.processMessage(sessionId, "Olá");

        // Assert
        ArgumentCaptor<EmbeddingSearchRequest> requestCaptor = ArgumentCaptor.forClass(EmbeddingSearchRequest.class);
        verify(embeddingStore).search(requestCaptor.capture());
        
        EmbeddingSearchRequest capturedRequest = requestCaptor.getValue();
        
        // Verifica se o filtro do langchain4j garante a condição do TenantA
        String filterString = capturedRequest.filter().toString();
        assertTrue(filterString.contains(tenantA.toString()), "O filtro da query vetorial deve conter estritamente o Tenant ID atual, prevenindo vazamento de dados.");
    }
}
