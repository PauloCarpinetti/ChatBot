package br.com.paulo.chatbot.application;

import br.com.paulo.chatbot.domain.ai.JpaChatMemoryStore;
import br.com.paulo.chatbot.security.TenantContextHolder;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import br.com.paulo.chatbot.domain.repository.TenantRepository;
import br.com.paulo.chatbot.domain.model.Tenant;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import br.com.paulo.chatbot.security.TenantAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatOrchestratorServiceTest {

    @Mock
    private ChatLanguageModel chatLanguageModel;
    @Mock
    private EmbeddingModel embeddingModel;
    @Mock
    private EmbeddingStore<TextSegment> embeddingStore;
    @Mock
    private JpaChatMemoryStore chatMemoryStore;
    @Mock
    private TenantRepository tenantRepository;
    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private Counter counter;

    @InjectMocks
    private ChatOrchestratorService chatOrchestratorService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID sessionId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new TenantAuthenticationToken(tenantId));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldProcessMessageSuccessfully() {
        // Apenas um teste estrutural mockado para confirmar que o Assistant não quebra
        // e que o tenantId é lido corretamente para formar o filtro.
        when(chatLanguageModel.generate(any(List.class)))
                .thenReturn(new dev.langchain4j.model.output.Response<>(new dev.langchain4j.data.message.AiMessage("Resposta AI")));
        
        when(meterRegistry.counter(any(String.class), any(String.class), any(String.class))).thenReturn(counter);
        
        Tenant t = new Tenant();
        t.setId(tenantId);
        t.setSystemPrompt("Prompt");
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(t));
        
        // Simular retriever devolvendo nada
        when(embeddingModel.embed(any(String.class)))
                .thenReturn(new dev.langchain4j.model.output.Response<>(dev.langchain4j.data.embedding.Embedding.from(new float[]{0.1f})));
        
        when(embeddingStore.search(any()))
                .thenReturn(new dev.langchain4j.store.embedding.EmbeddingSearchResult<>(java.util.List.of()));

        String response = chatOrchestratorService.processMessage(sessionId, "Olá");
        assertNotNull(response);
    }
}
