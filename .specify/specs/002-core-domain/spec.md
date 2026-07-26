# Feature Specification: Core Domain & Multi-Tenant Persistence

**Feature Branch**: `002-core-domain`

**Created**: 2026-07-25

**Status**: Draft

**Input**: User description: "Spec: 002 - Core Domain & Multi-Tenant Persistence from PDF"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Esquema de Banco de Dados via Flyway (Priority: P1)

Como desenvolvedor, eu quero usar Flyway para criar o esquema do banco de dados relacional MySQL (tabelas `tenants`, `chat_sessions`, `chat_messages`) garantindo suporte à arquitetura multi-tenant desde a base.

**Why this priority**: É a fundação do armazenamento de dados do projeto (P1).

**Independent Test**: Executar o Spring Boot e verificar se o Flyway roda o script sem erros.

**Acceptance Scenarios**:

1. **Given** um banco de dados MySQL vazio na infraestrutura local, **When** a aplicação inicializa, **Then** o Flyway executa o script de inicialização e cria as 3 tabelas sem erros.

---

### User Story 2 - Mapeamento de Entidades com JPA/Lombok (Priority: P1)

Como desenvolvedor, eu quero criar as entidades JPA correspondentes às tabelas (usando anotações do Lombok para redução de boilerplate), para permitir a interação orientada a objetos com os dados.

**Why this priority**: Dependência direta para a manipulação de dados na aplicação Spring Boot.

**Independent Test**: Subir o contexto do Spring Boot (via `@SpringBootTest`) para garantir que as anotações do Hibernate/JPA estão válidas.

**Acceptance Scenarios**:

1. **Given** as entidades JPA implementadas, **When** o `@SpringBootTest` roda, **Then** o contexto de inicialização sobe com sucesso.

---

### User Story 3 - Camada de Repositórios e Isolamento Multi-Tenant (Priority: P1)

Como sistema, eu devo garantir que todas as operações nos repositórios para `ChatSession` e `ChatMessage` incluam obrigatoriamente o `tenantId`, garantindo o isolamento de dados entre clientes.

**Why this priority**: Segurança inegociável descrita na Regra 2 da Constituição.

**Independent Test**: Verificar estaticamente se os repositórios não possuem métodos que consultem dados apenas pelo ID primário sem o ID do tenant.

**Acceptance Scenarios**:

1. **Given** os repositórios Spring Data JPA, **When** qualquer busca for declarada para sessões ou mensagens, **Then** ela exige obrigatoriamente o parâmetro `tenantId`.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O sistema DEVE usar Flyway com o script `backend/src/main/resources/db/migration/V1__init_schema.sql` para criar `tenants`, `chat_sessions` e `chat_messages`.
- **FR-002**: O sistema DEVE mapear as entidades JPA no pacote `br.com.paulo.chatbot.domain.model` usando as anotações `@Entity`, `@Table`, `@Data`, `@Builder`.
- **FR-003**: O campo `api_key` na entidade `Tenant` NUNCA deve ser retornado em serializações JSON, devendo utilizar `@JsonIgnore`.
- **FR-004**: O sistema DEVE criar Spring Data Repositories no pacote `br.com.paulo.chatbot.domain.repository`.
- **FR-005**: Repositórios de `ChatSession` e `ChatMessage` DEVEM exigir `tenantId` em todas as suas assinaturas de busca (`findByIdAndTenantId`, etc.).

### Key Entities

- **Tenant**: `id` (UUID), `name` (VARCHAR), `api_key` (VARCHAR), `system_prompt` (TEXT), `created_at` (TIMESTAMP).
- **ChatSession**: `id` (UUID), `tenant_id` (UUID FK), `user_identifier` (VARCHAR), `created_at` (TIMESTAMP). Possui índice composto `idx_tenant_user`.
- **ChatMessage**: `id` (UUID), `session_id` (UUID FK), `tenant_id` (UUID FK), `role` (VARCHAR), `content` (TEXT), `created_at` (TIMESTAMP).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: O script `V1__init_schema.sql` executa sem erros, criando as 3 tabelas no MySQL local.
- **SC-002**: Nenhuma query gerada ou escrita explicitamente nos repositórios de `ChatSession` ou `ChatMessage` permite a busca apenas pelo `id` da entidade (o `tenantId` deve ser um parâmetro obrigatório).
- **SC-003**: O contexto de inicialização do Spring Boot (`@SpringBootTest`) sobe com sucesso (indicando que as anotações do Hibernate/JPA estão corretas).

## Assumptions

- O banco de dados MySQL local já está disponível via Docker Compose (fase 001).
- O backend utilizará UUID como chave primária para todas as entidades principais.
