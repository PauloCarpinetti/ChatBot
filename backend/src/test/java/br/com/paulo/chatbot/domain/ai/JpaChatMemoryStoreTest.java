package br.com.paulo.chatbot.domain.ai;

import br.com.paulo.chatbot.domain.model.ChatMessage;
import br.com.paulo.chatbot.domain.repository.ChatMessageRepository;
import br.com.paulo.chatbot.security.TenantContextHolder;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import br.com.paulo.chatbot.security.TenantAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaChatMemoryStoreTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private JpaChatMemoryStore jpaChatMemoryStore;

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
    void shouldGetMessagesCorrectlyMapped() {
        ChatMessage entity1 = ChatMessage.builder().role("user").content("Hello").build();
        ChatMessage entity2 = ChatMessage.builder().role("assistant").content("Hi").build();

        when(chatMessageRepository.findBySessionIdAndTenantIdOrderByCreatedAtAsc(sessionId, tenantId))
                .thenReturn(Arrays.asList(entity1, entity2));

        List<dev.langchain4j.data.message.ChatMessage> messages = jpaChatMemoryStore.getMessages(sessionId);

        assertEquals(2, messages.size());
        assertTrue(messages.get(0) instanceof UserMessage);
        assertEquals("Hello", messages.get(0).text());
        assertTrue(messages.get(1) instanceof AiMessage);
        assertEquals("Hi", messages.get(1).text());
    }

    @Test
    void shouldUpdateMessagesCorrectly() {
        dev.langchain4j.data.message.ChatMessage lcMsg1 = new UserMessage("Test user");
        dev.langchain4j.data.message.ChatMessage lcMsg2 = new AiMessage("Test AI");

        when(chatMessageRepository.findBySessionIdAndTenantIdOrderByCreatedAtAsc(sessionId, tenantId))
                .thenReturn(List.of());

        jpaChatMemoryStore.updateMessages(sessionId, Arrays.asList(lcMsg1, lcMsg2));

        ArgumentCaptor<List<ChatMessage>> captor = ArgumentCaptor.forClass(List.class);
        verify(chatMessageRepository).saveAll(captor.capture());

        List<ChatMessage> saved = captor.getValue();
        assertEquals(2, saved.size());
        
        assertEquals(sessionId, saved.get(0).getSessionId());
        assertEquals(tenantId, saved.get(0).getTenantId());
        assertEquals("user", saved.get(0).getRole());
        assertEquals("Test user", saved.get(0).getContent());
        
        assertEquals("assistant", saved.get(1).getRole());
    }
}
