package br.com.paulo.chatbot.domain.service;

import br.com.paulo.chatbot.domain.model.ChatMessage;
import br.com.paulo.chatbot.domain.model.ChatSession;
import br.com.paulo.chatbot.domain.model.Tenant;
import br.com.paulo.chatbot.domain.repository.ChatMessageRepository;
import br.com.paulo.chatbot.domain.repository.ChatSessionRepository;
import br.com.paulo.chatbot.domain.repository.TenantRepository;
import br.com.paulo.chatbot.domain.ai.WeaviateService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private TenantRepository tenantRepository;
    @Mock
    private ChatSessionRepository chatSessionRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private WeaviateService weaviateService;
    @Mock
    private ChatLanguageModel chatLanguageModel;

    @InjectMocks
    private ChatService chatService;

    private UUID tenantId;
    private UUID sessionId;
    private Tenant tenant;
    private ChatSession session;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        sessionId = UUID.randomUUID();
        tenant = new Tenant();
        tenant.setId(tenantId);
        
        session = new ChatSession();
        session.setId(sessionId);
        session.setTenantId(tenantId);
    }

    @Test
    void processMessage_Success() {
        String userText = "Hello AI";
        String aiResponseText = "Hello Human";

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));
        when(chatSessionRepository.findByIdAndTenantId(sessionId, tenantId)).thenReturn(Optional.of(session));
        
        // Mock DB save for user message
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock Weaviate context
        when(weaviateService.searchRelevantChunks(tenantId.toString(), userText, 3)).thenReturn(List.of("Context 1"));

        // Mock History
        ChatMessage oldMsg = new ChatMessage();
        oldMsg.setRole("USER");
        oldMsg.setContent("old question");
        when(chatMessageRepository.findBySessionIdAndTenantIdOrderByCreatedAtAsc(sessionId, tenantId))
                .thenReturn(List.of(oldMsg));

        // Mock LLM Response
        when(chatLanguageModel.generate(anyList())).thenReturn(new Response<>(new AiMessage(aiResponseText)));

        // Execute
        String result = chatService.processMessage(tenantId, sessionId, "user123", userText);

        // Verify
        assertEquals(aiResponseText, result);
        
        // Verify user msg and ai msg were saved (1 for user, 1 for assistant)
        verify(chatMessageRepository, times(2)).save(any(ChatMessage.class));
        verify(chatLanguageModel).generate(anyList());
    }
}
