# Tasks: Core Domain & Multi-Tenant Persistence

**Input**: Design documents from `/specs/002-core-domain/`

**Prerequisites**: plan.md (required), spec.md (required for user stories)

## Phase 1: Flyway Migration (US1)

**Goal**: Setup database schema with Flyway.

- [ ] T001 [US1] Adicionar dependências do `flyway-core` e `mysql-connector-j` no `backend/pom.xml` (se ainda não existirem).
- [ ] T002 [US1] Criar diretório e arquivo de migração `backend/src/main/resources/db/migration/V1__init_schema.sql`.
- [ ] T003 [US1] Adicionar DDL para a tabela `tenants` (`id`, `name`, `api_key`, `system_prompt`, `created_at`).
- [ ] T004 [US1] Adicionar DDL para a tabela `chat_sessions` (`id`, `tenant_id`, `user_identifier`, `created_at`) com índice `idx_tenant_user`.
- [ ] T005 [US1] Adicionar DDL para a tabela `chat_messages` (`id`, `session_id`, `tenant_id`, `role`, `content`, `created_at`).

---

## Phase 2: JPA Entities (US2)

**Goal**: Criar o mapeamento objeto-relacional (JPA) com Lombok.

- [ ] T006 [P] [US2] Adicionar dependência do `spring-boot-starter-data-jpa` no `backend/pom.xml`.
- [ ] T007 [P] [US2] Criar entidade `Tenant` em `br.com.paulo.chatbot.domain.model.Tenant` com anotações `@Entity` e `@JsonIgnore` em `api_key`.
- [ ] T008 [P] [US2] Criar entidade `ChatSession` em `br.com.paulo.chatbot.domain.model.ChatSession`.
- [ ] T009 [P] [US2] Criar entidade `ChatMessage` em `br.com.paulo.chatbot.domain.model.ChatMessage`.

---

## Phase 3: Spring Data Repositories (US3)

**Goal**: Implementar camada de acesso aos dados garantindo isolamento estrito por tenant.

- [ ] T010 [P] [US3] Criar `TenantRepository` estendendo `JpaRepository` com o método `findByApiKey(String apiKey)`.
- [ ] T011 [P] [US3] Criar `ChatSessionRepository` com os métodos `findByIdAndTenantId` e `findByTenantIdAndUserIdentifier`.
- [ ] T012 [P] [US3] Criar `ChatMessageRepository` com o método `findBySessionIdAndTenantIdOrderByCreatedAtAsc`.
- [ ] T013 [US3] Revisão estática para garantir que nenhum repositório tenha um método padrão que pule o `tenantId` para buscas de sessões ou mensagens.

---

## Phase 4: Validation & Testing

**Goal**: Confirmar que a infraestrutura sobe corretamente e o Flyway atua sobre o MySQL local.

- [ ] T014 [P] Rodar `@SpringBootTest` para confirmar o carregamento do contexto do JPA/Hibernate sem falhas de mapeamento.
- [ ] T015 [P] Inicializar a aplicação conectada ao MySQL local (`docker-compose up`) e verificar se o Flyway processa a versão V1 sem erros.
