# Plano Arquitetural - Spec 006

1. **DTOs**: Criação de objetos simples de contrato estrito para JSON binding via Jackson.
2. **GlobalExceptionHandler**: Captura global de exceções para impedir vazamento de erros do Tomcat e responder com status REST corretos.
3. **ChatController**: Receber requisições com UUID opcional, injetando ChatOrchestratorService e mapeando fluxos de criação de resposta via LLM e recuperação de histórico.
