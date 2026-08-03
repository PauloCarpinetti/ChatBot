# Spec 010: Refatoração do Backend para Validação de IA

## Descrição
Refatoração estrutural da arquitetura do backend em Java/Spring Boot para suportar integralmente as regras e os testes estabelecidos na Spec 008. O código de produção atual precisa ser reestruturado e desacoplado para permitir injeção de mocks, viabilizando testes de LLM econômicos, validação de IA por juízes e garantindo o isolamento absoluto de dados entre clientes.

## Objetivos (Regras de Negócio Técnicas)
1. **Desacoplamento do Serviço de IA**: Refatorar os serviços (ex: `ChatOrchestratorService`) que chamam a API da LLM para dependerem de abstrações (Interfaces), permitindo a injeção do mock durante a execução dos testes.
2. **Isolamento de Dados (Tenants)**: Refatorar a camada de acesso a dados e a camada de requisição HTTP para que o `Tenant ID` seja estritamente injetado no contexto de cada operação, assegurando que o `TenantLeakageTest` passe.
3. **Avaliador LLM (LLM as a Judge)**: Fornecer a fundação no código de produção para acionar um modelo superior focado em avaliar as respostas do chatbot.

## Tecnologias e Padrões
- Java / Spring Boot
- Injeção de Dependências Dinâmica (Profiles de Teste)
- Clean Architecture / Separação de Preocupações
- Padrão ThreadLocal para gestão de Sessão/Tenant
