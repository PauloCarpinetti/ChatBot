# Plano de Implementação: Refatoração Backend (Spec 010)

1. **Mapeamento da Arquitetura**: 
   - Analisar o código atual no pacote `backend/src/main/java/br/com/paulo/chatbot`.
   - Identificar dependências rígidas (hardcoded) de chamadas externas de LLM.
2. **Abstração do LLM (Mocking)**: 
   - Extrair uma interface `LlmClient` ou similar.
   - Criar uma implementação real para uso em produção e garantir que a infraestrutura do Spring Boot consiga injetar o mock correto nos perfis de teste (`@Profile("test")`).
3. **Mecanismo Multi-Tenant Seguro**:
   - Implementar a classe `TenantContextHolder` (usando ThreadLocal) para armazenar o ID do cliente durante o ciclo de vida da requisição HTTP.
   - Adicionar validações na camada de persistência para assegurar que queries não "vazem" para outros tenants.
4. **Infraestrutura do LLM Judge**:
   - Desenvolver o serviço `LlmEvaluationService` responsável por orquestrar o envio de métricas e validar as respostas via um modelo julgador.
5. **Teste e Homologação**: 
   - Executar a suíte de testes (TDD) desenvolvida na Spec 008.
   - Refatorar o código até que todos os testes (LLM Mock, Tenant Leakage e LLM Judge) fiquem verdes.
