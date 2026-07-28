# Spec 004 - Autenticação, Segurança e Isolamento de Tenant (Spring Security)

## 1. Objetivo

Proteger todas as rotas da API REST do chatbot utilizando JWT (JSON Web Tokens). O Spring Security deve ser configurado para interceptar requisições, validar o token e, de forma transparente, injetar o `tenant_id` no contexto de segurança da aplicação. Isso garante que a camada de controle (Controllers) e os repositórios (da Spec 002) sempre saibam de quem é a requisição, impondo a **Regra 2** da nossa `constitution.md`.

## 2. Configuração do Spring Security (`com.agentechat.backend.application.config.security`)

### 2.1. Desativações Iniciais

Como este é um microsserviço API-first que servirá um widget (Next.js), precisamos adequar o Spring Security para um cenário Stateless:

- **Desativar CSRF** (`csrf().disable()`), pois tokens JWT não são suscetíveis nativamente a CSRF como os cookies de sessão.
- **Desativar gerenciamento de sessão** (`sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)`).
- **Desativar o formulário de login padrão** (`formLogin().disable()`).

### 2.2. CORS (Cross-Origin Resource Sharing)

O widget frontend será embutido em domínios de terceiros. Configurar o `CorsConfigurationSource` para:

- Permitir origens específicas definidas via variável de ambiente (`CORS_ALLOWED_ORIGINS`). Para desenvolvimento, permitir `http://localhost:3000`.
- Permitir métodos: `GET`, `POST`, `OPTIONS`.
- Permitir headers: `Authorization`, `Content-Type`.

## 3. Filtro de Autenticação JWT (`JwtAuthenticationFilter`)

Criar um filtro que estenda `OncePerRequestFilter`. A lógica de execução deve ser:

- **Extração**: Ler o header `Authorization: Bearer <token>`.
- **Pulo (Bypass)**: Se o header não existir, continuar a cadeia de filtros (o Spring Security barrará o acesso nas rotas protegidas).
- **Validação**: Usar a biblioteca `java-jwt` para verificar a assinatura e a validade temporal do token, usando uma chave secreta injetada via `@Value("${jwt.secret}")`.
- **Extração de Claims**: Se o token for válido, extrair o `tenantId` (UUID) de dentro do payload do token.
- **Construção do Contexto**: Criar um objeto de autenticação customizado (ex: `TenantAuthenticationToken`) armazenando o `tenantId`.
- **Injeção**: Setar este objeto no `SecurityContextHolder`.

## 4. Gestão do Contexto do Tenant (`com.agentechat.backend.security`)

Para facilitar o uso nas outras camadas (Services/Repositories) sem acoplar fortemente ao Spring Security, crie um utilitário:

**Classe `TenantContextHolder`**:
- Método `static UUID getCurrentTenantId()`: Esse método deve ler o `SecurityContextHolder`, extrair o `tenantId` da autenticação atual e retorná-lo. Deve lançar uma `UnauthorizedException` se nenhum tenant for encontrado no contexto.

## 5. Rotas e Permissões

Configurar o `SecurityFilterChain` para definir que:

- As rotas `/api/v1/chat/**` exigem autenticação obrigatória (usuário portando JWT válido com um `tenantId`).
- Rotas de health check (`/actuator/health` ou `/api/v1/public/health`) devem ser públicas (permitidas sem token).

## 6. Critérios de Aceite (Acceptance Criteria)

1. [ ] Uma requisição `POST /api/v1/chat/message` sem o header `Authorization` retorna HTTP `401 Unauthorized`.
2. [ ] Uma requisição com um JWT corrompido, expirado ou assinado com uma chave secreta diferente retorna HTTP `401 Unauthorized`.
3. [ ] Uma requisição com um JWT válido processa a requisição e o método `TenantContextHolder.getCurrentTenantId()` consegue extrair com sucesso o UUID do cliente.
4. [ ] O console não apresenta erros de CORS ao fazer requisições a partir de um cliente no `localhost:3000`.
