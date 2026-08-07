# Lista de Tarefas: Scalability, Rate Limiting & Observability

- [ ] `Backend`: Incluir dependências do **Bucket4j**, **Spring Data Redis** e **Actuator/Micrometer** no `pom.xml`.
- [ ] `Backend`: Configurar Beans de Redis no Spring Boot (`RedisConfig.java`) apontando para as variáveis de ambiente `REDIS_HOST` e `REDIS_PORT`.
- [ ] `Backend`: Criar a lógica do Rate Limiter:
    - Implementar a classe `RateLimitingService.java` que consulta o bucket do Tenant.
    - Criar o filtro de servlet ou o interceptor MVC `RateLimitInterceptor.java` aplicando-o nas chamadas autenticadas `/api/v1/chat/**`.
- [ ] `Backend`: Incluir tags de métricas em `ChatOrchestratorService.java` usando `@Timed`.
- [ ] `Backend`: Liberar acesso no Spring Security para a URL do Actuator `/actuator/prometheus`.
- [ ] `Infraestrutura`: Adicionar a action de CI/CD para GCP (`.github/workflows/deploy.yml`).
- [ ] `Testes Manuais`: Fazer chamadas concorrentes com curl local para validar o recebimento de `429 Too Many Requests`.
- [ ] `Testes Manuais`: Verificar métricas abertas acessando http://localhost:8081/actuator/prometheus.
