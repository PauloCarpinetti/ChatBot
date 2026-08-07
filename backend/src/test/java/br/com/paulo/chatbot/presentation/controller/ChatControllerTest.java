package br.com.paulo.chatbot.presentation.controller;

import br.com.paulo.chatbot.application.ChatOrchestratorService;
import br.com.paulo.chatbot.domain.model.ChatMessage;
import br.com.paulo.chatbot.domain.repository.ChatMessageRepository;
import br.com.paulo.chatbot.domain.repository.ChatSessionRepository;
import br.com.paulo.chatbot.presentation.exception.GlobalExceptionHandler;
import br.com.paulo.chatbot.security.TenantAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ChatOrchestratorService chatOrchestratorService;

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatController chatController;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        SecurityContextHolder.getContext().setAuthentication(new TenantAuthenticationToken(tenantId));
        
        mockMvc = MockMvcBuilders.standaloneSetup(chatController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenPostMessageWithoutSessionId_thenReturnsNewSessionId() throws Exception {
        when(chatOrchestratorService.processMessage(any(UUID.class), eq("Olá IA!")))
                .thenReturn("Olá humano!");

        Map<String, String> request = new HashMap<>();
        request.put("message", "Olá IA!");

        mockMvc.perform(post("/api/v1/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").exists())
                .andExpect(jsonPath("$.reply").value("Olá humano!"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenPostMessageWithSessionId_thenReusesSessionId() throws Exception {
        UUID existingSessionId = UUID.randomUUID();

        when(chatOrchestratorService.processMessage(eq(existingSessionId), eq("Qual meu nome?")))
                .thenReturn("Eu não sei.");

        Map<String, String> request = new HashMap<>();
        request.put("sessionId", existingSessionId.toString());
        request.put("message", "Qual meu nome?");

        mockMvc.perform(post("/api/v1/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(existingSessionId.toString()))
                .andExpect(jsonPath("$.reply").value("Eu não sei."));
    }

    @Test
    void whenMessageIsEmpty_thenReturnsBadRequest() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("message", "   ");

        mockMvc.perform(post("/api/v1/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("A mensagem não pode estar vazia"));
    }

    @Test
    void whenTimeoutException_thenReturnsGatewayTimeout() throws Exception {
        when(chatOrchestratorService.processMessage(any(UUID.class), eq("Dorme?")))
                .thenThrow(new RuntimeException("Database down"));

        Map<String, String> request = new HashMap<>();
        request.put("message", "Dorme?");

        mockMvc.perform(post("/api/v1/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Ocorreu um erro interno no servidor."));
    }

    @Test
    void whenGetHistory_thenReturnsList() throws Exception {
        UUID sessionId = UUID.randomUUID();

        ChatMessage msg1 = new ChatMessage();
        msg1.setRole("USER");
        msg1.setContent("Oi");

        ChatMessage msg2 = new ChatMessage();
        msg2.setRole("ASSISTANT");
        msg2.setContent("Olá, tudo bem?");

        when(chatMessageRepository.findBySessionIdAndTenantIdOrderByCreatedAtAsc(sessionId, tenantId))
                .thenReturn(List.of(msg1, msg2));

        mockMvc.perform(get("/api/v1/chat/sessions/" + sessionId + "/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[0].content").value("Oi"))
                .andExpect(jsonPath("$[1].role").value("ASSISTANT"))
                .andExpect(jsonPath("$[1].content").value("Olá, tudo bem?"));
    }
}
