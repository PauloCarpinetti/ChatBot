# Tasks: Project Foundation

**Input**: Design documents from `/specs/001-project-foundation/`

**Prerequisites**: plan.md (required), spec.md (required for user stories)

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Initialize `backend` directory with Java and Spring Boot.
- [x] T002 Initialize `frontend-widget` directory with React and NextJS.
- [x] T003 Configure Docker Compose infrastructure (MySQL, Weaviate).

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [ ] T004 Configurar roteamento básico e controllers no Spring Boot.
- [ ] T005 [P] Setup da configuração de properties/yml e variáveis de ambiente (`.env`).
- [ ] T006 Implementar controller advice global para tratamento de erros no backend.
- [ ] T007 Configurar build do frontend-widget no NextJS para uso isolado e embarcado.

---

## Phase 3: User Story 2 - Infraestrutura Multi-Tenant com JWT (Priority: P1)

**Goal**: Garantir isolamento de dados via JWT

### Implementation for User Story 2

- [ ] T008 [US2] Criar filtro de autenticação Spring Security (`JwtAuthenticationFilter`) que decodifica JWT.
- [ ] T009 [US2] Extrair `tenant_id` do token e injetar no contexto de segurança (SecurityContextHolder).
- [ ] T010 [US2] Retornar erro 401 Unauthorized se JWT estiver ausente ou inválido.
- [ ] T011 [US2] Adicionar testes unitários para o filtro de segurança com MockMvc.

---

## Phase 4: User Story 3 - Abstração de Chamadas de LLM (Priority: P2)

**Goal**: Abstrair chamadas de LLM do pipeline RAG.

### Implementation for User Story 3

- [ ] T012 [US3] Criar interface `ChatProviderInterface` (ex: `generate(prompt: String, context: List<String>): String`).
- [ ] T013 [US3] Criar classe `MockChatProvider` que implementa `ChatProviderInterface` para testes.
- [ ] T014 [US3] (Opcional) Criar implementação real que conecte com provedor LLM via WebClient ou biblioteca Spring AI.
- [ ] T015 [US3] Escrever testes unitários que comprovem a injeção correta do provider mockado.

---

## Phase 5: User Story 4 - Widget Frontend Acoplável (Priority: P2)

**Goal**: Widget React seguro contra vazamento de CSS.

### Implementation for User Story 4

- [ ] T016 [US4] Configurar o TailwindCSS no NextJS com prefixo customizado (ex: `prefix: 'tw-chat-'`).
- [ ] T017 [US4] Implementar um botão flutuante básico na UI usando os componentes do React.
- [ ] T018 [US4] Adicionar um componente base de janela de chat com encapsulamento de estado (useState, hooks).
- [ ] T019 [US4] Testar renderização do widget embarcado para validar que estilos externos não afetam o componente.
