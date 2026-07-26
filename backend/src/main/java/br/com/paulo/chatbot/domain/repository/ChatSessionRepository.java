package br.com.paulo.chatbot.domain.repository;

import br.com.paulo.chatbot.domain.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {
    // MANDATÓRIO: Toda busca por sessão deve incluir o tenantId
    Optional<ChatSession> findByIdAndTenantId(UUID id, UUID tenantId);
    List<ChatSession> findByTenantIdAndUserIdentifier(UUID tenantId, String userIdentifier);
}
