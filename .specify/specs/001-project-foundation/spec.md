# Feature Specification: Project Foundation

**Feature Branch**: `001-project-foundation`

**Created**: 2026-07-25

**Status**: Done

**Input**: User description: "spec 1 project foundation based on constitution.md"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - System initialization and Mono-repo structure (Priority: P1)

Como desenvolvedor, eu quero inicializar o projeto com um frontend (React/NextJS) e backend (Java/Spring Boot) integrados via Docker Compose, para facilitar o desenvolvimento, testes e implantação da fundação do ChatBot.

**Why this priority**: É a infraestrutura base (P1) necessária para qualquer outra funcionalidade existir, alinhado com o Princípio 1 de simplicidade.

**Independent Test**: Pode ser testado executando o `docker-compose up` e confirmando que o backend (Java) e infraestrutura (MySQL, Weaviate) inicializam corretamente.

**Acceptance Scenarios**:

1. **Given** um ambiente limpo, **When** eu inicio o projeto via docker-compose, **Then** o backend, frontend e banco de dados rodam corretamente.

---

### User Story 2 - Infraestrutura Multi-Tenant com JWT (Priority: P1)

Como sistema, eu devo extrair o `tenant_id` de um JWT autenticado e repassá-lo para a camada de persistência e vector store, garantindo que os dados não vazem.

**Why this priority**: Segurança inegociável (Princípio 2).

**Independent Test**: Testes unitários do filtro JWT no Spring Security.

**Acceptance Scenarios**:

1. **Given** uma requisição válida com JWT, **When** ela chega na rota da API, **Then** o filtro extrai o `tenant_id` e o injeta no contexto.
2. **Given** uma requisição sem JWT, **When** ela chega, **Then** ela é rejeitada com código HTTP 401.

---

### User Story 3 - Abstração de Chamadas de LLM (Priority: P2)

Como desenvolvedor, eu quero abstrair a interface do provedor de LLM para poder trocar de provedor (OpenAI, Anthropic) sem mudar a lógica de negócios e facilitar o mock em testes.

**Why this priority**: Evita acoplamento e permite testes locais (Princípios 3 e 5).

**Independent Test**: Criar mock da interface de chat que retorna uma string fixa.

**Acceptance Scenarios**:

1. **Given** o pipeline RAG em execução, **When** ele solicita uma geração de texto, **Then** ele usa a abstração.

---

### User Story 4 - Widget Frontend Acoplável (Priority: P2)

Como usuário, eu quero embarcar o chat na minha aplicação sem que o CSS quebre o layout da página.

**Why this priority**: Zero colisão é essencial (Princípio 4).

**Independent Test**: Validar que os estilos CSS estão isolados no build do NextJS.

**Acceptance Scenarios**:

1. **Given** o widget carregado em uma div, **When** o CSS host tem estilos para botões, **Then** o botão do widget não é afetado.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Monolito Modular no backend utilizando Java e Spring Boot.
- **FR-002**: Autenticação via JWT contendo a claim do tenant.
- **FR-003**: Buscas de DB DEVEM incluir filtro `tenant_id`.
- **FR-004**: Buscas no Vector DB DEVEM usar filtro `tenantId`.
- **FR-005**: Chamadas ao LLM DEVEM ser abstraídas via interfaces no Spring.
- **FR-006**: Frontend (React/NextJS) DEVE usar encapsulamento de CSS.
- **FR-007**: A infraestrutura local DEVE ser provida via Docker Compose.

### Key Entities 

- **Tenant**: Identificado pelo `tenant_id`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Testes unitários do RAG rodam rápido com mock de LLM no backend Java.
- **SC-002**: 100% isolamento de CSS do frontend React.

## Assumptions

- Banco de dados relacional é MySQL.
- Banco vetorial é Weaviate.
- Ambos são providos localmente pelo Docker Compose.
