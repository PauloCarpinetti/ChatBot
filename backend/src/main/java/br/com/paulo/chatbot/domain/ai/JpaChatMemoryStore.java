package br.com.paulo.chatbot.domain.ai;

import br.com.paulo.chatbot.domain.model.ChatMessage;
import br.com.paulo.chatbot.domain.repository.ChatMessageRepository;
import br.com.paulo.chatbot.security.TenantContextHolder;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class JpaChatMemoryStore implements ChatMemoryStore {

    private final ChatMessageRepository chatMessageRepository;

    public JpaChatMemoryStore(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public List<dev.langchain4j.data.message.ChatMessage> getMessages(Object memoryId) {
        UUID sessionId = UUID.fromString(memoryId.toString());
        UUID tenantId = TenantContextHolder.getCurrentTenantId();

        List<ChatMessage> entities = chatMessageRepository.findBySessionIdAndTenantIdOrderByCreatedAtAsc(sessionId, tenantId);

        List<dev.langchain4j.data.message.ChatMessage> lcMessages = new ArrayList<>();
        for (ChatMessage entity : entities) {
            if ("user".equalsIgnoreCase(entity.getRole())) {
                lcMessages.add(new UserMessage(entity.getContent()));
            } else if ("ai".equalsIgnoreCase(entity.getRole()) || "assistant".equalsIgnoreCase(entity.getRole())) {
                lcMessages.add(new AiMessage(entity.getContent()));
            } else if ("system".equalsIgnoreCase(entity.getRole())) {
                lcMessages.add(new SystemMessage(entity.getContent()));
            }
        }
        return lcMessages;
    }

    @Override
    public void updateMessages(Object memoryId, List<dev.langchain4j.data.message.ChatMessage> messages) {
        UUID sessionId = UUID.fromString(memoryId.toString());
        UUID tenantId = TenantContextHolder.getCurrentTenantId();

        // Para evitar duplicação em ambientes sem session caching complexo,
        // o mais seguro numa memory store persistente customizada é comparar ou apagar/re-inserir,
        // porém o repositório ChatMessage do Langchain nos envia a lista completa (janela de contexto).
        // Vamos buscar as mensagens já existentes (pelo ID e texto) para não duplicar, ou limpar a sessão atual.
        // Como o chat memory store re-envia a lista toda vez que ela muda, a abordagem comum do JPA store
        // é deletar e re-salvar, ou inserir apenas a(s) nova(s) (o que é mais difícil sem IDs explícitos nas mensagens LC).
        
        // Uma abordagem simples: apagar o histórico atual da sessão e recriar.
        // Ou melhor: se assumirmos que a tabela só cresce e nós controlamos a inserção na ponta,
        // isso poderia ser perigoso se o Langchain reenviar tudo.
        // Na verdade, o langchain4j envia a "window" inteira no updateMessages.
        
        // Vamos apagar as existentes desta sessão e recriar para simplificar e garantir sincronia exata com a "memory window" do langchain.
        // (Atenção: em produção com histórico longo, deletar/recriar não é o ideal, mas satisfaz o escopo da especificação)
        
        List<ChatMessage> existing = chatMessageRepository.findBySessionIdAndTenantIdOrderByCreatedAtAsc(sessionId, tenantId);
        chatMessageRepository.deleteAll(existing);

        List<ChatMessage> newEntities = new ArrayList<>();
        for (dev.langchain4j.data.message.ChatMessage lcMsg : messages) {
            String role = "user";
            if (lcMsg instanceof AiMessage) {
                role = "assistant";
            } else if (lcMsg instanceof SystemMessage) {
                role = "system";
            }
            
            ChatMessage entity = ChatMessage.builder()
                    .sessionId(sessionId)
                    .tenantId(tenantId)
                    .role(role)
                    .content(lcMsg.text())
                    .build();
            newEntities.add(entity);
        }
        chatMessageRepository.saveAll(newEntities);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        UUID sessionId = UUID.fromString(memoryId.toString());
        UUID tenantId = TenantContextHolder.getCurrentTenantId();
        List<ChatMessage> existing = chatMessageRepository.findBySessionIdAndTenantIdOrderByCreatedAtAsc(sessionId, tenantId);
        chatMessageRepository.deleteAll(existing);
    }
}
