# Tasks - Spec 004

- [ ] Implementar as chaves de ambiente `jwt.secret` e `cors.allowed-origins` em `application.yml`
- [ ] Criar classe `TenantAuthenticationToken` estendendo `AbstractAuthenticationToken`
- [ ] Criar classe `TenantContextHolder` com `getCurrentTenantId()` e custom exception para caso não exista tenant
- [ ] Criar `JwtAuthenticationFilter` implementando parse JWT via `java-jwt` e injeção do contexto
- [ ] Refatorar a classe existente `SecurityConfig` para:
  - [ ] Stateless, desativar CSRF, desativar sessionManagement, formLogin disable.
  - [ ] Configurar Beans de CORS.
  - [ ] Bloquear endpoints do chat e permitir health.
  - [ ] Adicionar o `JwtAuthenticationFilter` no filter chain.
- [ ] Implementar testes unitários e integrados.
  - [ ] `JwtAuthenticationFilterTest`
  - [ ] `TenantContextHolderTest`
  - [ ] `SecurityIntegrationTest` (mockmvc + controller)
