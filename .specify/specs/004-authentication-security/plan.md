# Plan - Spec 004

## Arquitetura de Segurança

A implementação adotará um modelo API-first Stateless baseado em JSON Web Tokens (JWT). A segurança baseia-se num fluxo simples:

1. **Recepção**: Filtro (`JwtAuthenticationFilter`) escuta cada requisição HTTP.
2. **Avaliação**: O Token é validado pelo `java-jwt` garantindo assinatura, temporalidade e formatação.
3. **Extração de Contexto**: Se válido, extraímos a "Claim" de tenant do JWT e criamos o `TenantAuthenticationToken`.
4. **Isolamento**: Armazenamos na `SecurityContextHolder`. A classe auxiliar `TenantContextHolder` servirá o UUID globalmente.
5. **Decisão**: A cadeia de segurança (`SecurityFilterChain`) bloqueia requisições nas rotas sensíveis que não possuam autenticação.

## Implementação Backend

### 1. Pacotes e Estrutura
- `br.com.paulo.chatbot.security`: Centralizará as classes `JwtAuthenticationFilter`, `TenantAuthenticationToken` e `TenantContextHolder` (separação de responsabilidade para reutilização global pela API).
- `br.com.paulo.chatbot.application.config.SecurityConfig`: Será atualizada.

### 2. Tratamento de Exceção
O `TenantContextHolder` lançará uma exceção Runtime (ex: `UnauthorizedException` ou uma customizada `TenantNotFoundException`) caso um endpoint protegido falhe em ter o tenant_id. Esta exceção deve ser capturada no futuro por um `GlobalExceptionHandler` caso necessário.

### 3. Integração (Próximos Passos)
Na Spec 002 ou em rotas onde a API acessa os repositórios (ex: ChatMessageRepository), as instâncias poderão chamar diretamente `TenantContextHolder.getCurrentTenantId()` sem precisarem referenciar detalhes de requisição HTTP (HttpServletRequest) ou tokens, isolando as camadas adequadamente.
