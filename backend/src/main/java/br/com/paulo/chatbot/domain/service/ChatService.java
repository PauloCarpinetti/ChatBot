package br.com.paulo.chatbot.domain.service;

import br.com.paulo.chatbot.domain.model.ChatMessage;
import br.com.paulo.chatbot.domain.model.ChatSession;
import br.com.paulo.chatbot.domain.model.Tenant;
import br.com.paulo.chatbot.domain.repository.ChatMessageRepository;
import br.com.paulo.chatbot.domain.repository.ChatSessionRepository;
import br.com.paulo.chatbot.domain.repository.TenantRepository;
import br.com.paulo.chatbot.domain.ai.WeaviateService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final TenantRepository tenantRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final WeaviateService weaviateService;
    private final ChatLanguageModel chatLanguageModel;

    public ChatService(TenantRepository tenantRepository,
                       ChatSessionRepository chatSessionRepository,
                       ChatMessageRepository chatMessageRepository,
                       WeaviateService weaviateService,
                       ChatLanguageModel chatLanguageModel) {
        this.tenantRepository = tenantRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.weaviateService = weaviateService;
        this.chatLanguageModel = chatLanguageModel;
    }

    @Transactional
    public String processMessage(UUID tenantId, UUID sessionId, String userIdentifier, String userText) {
        // 1. Recebimento e Validação
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant não encontrado"));
        
        ChatSession session = chatSessionRepository.findByIdAndTenantId(sessionId, tenantId)
                .orElseGet(() -> {
                    ChatSession newSession = new ChatSession();
                    newSession.setId(sessionId);
                    newSession.setTenantId(tenantId);
                    newSession.setUserIdentifier(userIdentifier);
                    return chatSessionRepository.save(newSession);
                });

        // 2. Persistência Inicial (MySQL)
        ChatMessage userMessage = new ChatMessage();
        userMessage.setSessionId(sessionId);
        userMessage.setTenantId(tenantId);
        userMessage.setRole("USER");
        userMessage.setContent(userText);
        chatMessageRepository.save(userMessage);

        // 3 & 4. Vetorização e Busca de Contexto (Weaviate)
        List<String> relevantChunks = weaviateService.searchRelevantChunks(tenantId.toString(), userText, 3);
        String contextStr = String.join("\n\n", relevantChunks);

        // 5. Busca de Histórico (MySQL)
        List<ChatMessage> history = chatMessageRepository.findBySessionIdAndTenantIdOrderByCreatedAtAsc(sessionId, tenantId);

        // 6. Orquestração do Prompt (LangChain4j)
        List<dev.langchain4j.data.message.ChatMessage> langChainMessages = new ArrayList<>();
        
        // System Prompt com Contexto Injetado
        String systemPrompt = "Você é um assistente útil e educado.\n" +
                              "Responda à pergunta do usuário baseando-se ESTRITAMENTE no contexto abaixo:\n" +
                              "CONTEXTO DO SISTEMA:\n" + contextStr;
        langChainMessages.add(SystemMessage.from(systemPrompt));

        // Histórico (já inclui a userMessage salva no passo 2)
        for (ChatMessage msg : history) {
            if ("USER".equalsIgnoreCase(msg.getRole())) {
                langChainMessages.add(UserMessage.from(msg.getContent()));
            } else if ("ASSISTANT".equalsIgnoreCase(msg.getRole())) {
                langChainMessages.add(AiMessage.from(msg.getContent()));
            }
        }
        
        // 7. Geração (LLM)
        AiMessage aiResponse = chatLanguageModel.generate(langChainMessages).content();
        String assistantText = aiResponse.text();

        // 8. Persistência Final e Retorno (MySQL)
        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setSessionId(sessionId);
        assistantMessage.setTenantId(tenantId);
        assistantMessage.setRole("ASSISTANT");
        assistantMessage.setContent(assistantText);
        chatMessageRepository.save(assistantMessage);

        return assistantText;
    }
}
