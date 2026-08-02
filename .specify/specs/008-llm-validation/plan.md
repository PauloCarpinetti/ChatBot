# Plano de Ação: Spec 008

## Estratégia de Arquitetura
1. **Limpeza do Repositório:** A pasta `Clients` deve ser restrita apenas a clientes front-end em HTML/JS (ex: testes manuais do widget). Qualquer resíduo de backend PHP/Laravel será removido.
2. **Setup do Spring Boot Testing:** Criaremos a infraestrutura de testes no diretório oficial do backend (`backend/src/test`). Isso inclui a criação do perfil `test` no Spring.
3. **Mocks e Avaliação:** Usaremos a anotação `@MockBean` (ou similar) para isolar a infraestrutura de comunicação com APIs externas. Criaremos interfaces ou serviços de abstração (ex: `LlmEvaluationService`) e escreveremos os testes (TDD).

## Fases
1. **Fase 1: Preparação do Ambiente**
   - Limpar a pasta `Clients/`.
   - Adicionar o arquivo `application-test.yml` no projeto Spring.
2. **Fase 2: Testes Base e Mock**
   - Escrever `LlmMockIntegrationTest.java` focado em assertivas básicas usando mock.
3. **Fase 3: Tenant Leakage**
   - Escrever `TenantLeakageTest.java` focado em assertivas de segurança e contexto de multitenancy.
4. **Fase 4: LLM as a Judge**
   - Escrever `LlmJudgeEvaluationTest.java` com prompts estruturados para o modelo avaliador testar a resposta do ChatBot contra uma "Ground Truth" esperada.
