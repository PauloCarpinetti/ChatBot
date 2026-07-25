package br.com.paulo.chatbot.domain.repository;

import br.com.paulo.chatbot.domain.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    // MANDATÓRIO: O isolamento continua aqui
    List<ChatMessage> findBySessionIdAndTenantIdOrderByCreatedAtAsc(UUID sessionId, UUID tenantId);
}
