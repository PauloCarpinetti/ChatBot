Spec: 006 - REST API & Web Layer (Controllers)

1. Objetivo

Criar a camada de controle (Controllers) para expor as funcionalidades do chatbot através de uma API RESTful. Esta camada deve receber as requisições HTTP do widget frontend, processar os DTOs (Data Transfer Objects), delegar a execução para a camada de serviço (ChatOrchestratorService da Spec 004) e retornar respostas padronizadas, além de lidar de forma elegante com possíveis erros.

2. Data Transfer Objects (DTOs)

Criar o pacote com.yourdomain.chatbot.presentation.dto. Estes objetos definem o contrato estrito de entrada e saída.

ChatRequest (Entrada):
sessionId (UUID, Opcional): Identificador da conversa atual. Se for nulo, indica que é uma nova conversa.
message (String, Obrigatório): O texto enviado pelo usuário.

ChatResponse (Saída):
sessionId (UUID): O ID da sessão. Se não foi enviado na requisição, o backend devolve o novo gerado.
response (String): A resposta gerada pelo LLM.
timestamp (Instant): A hora exata da resposta.

3. O Controller Principal (ChatController)

Criar a classe ChatController no pacote com.yourdomain.chatbot.presentation.controller, anotada com @RestController e mapeada para @RequestMapping("/api/v1/chat").

Dependências Injetadas:
ChatOrchestratorService (Da Spec 004).

Nota: Não precisamos injetar nada de validação de Tenant aqui, pois o JwtAuthenticationFilter (Spec 003) já garantiu que a requisição é segura antes mesmo de chegar no Controller.

Endpoints Necessários:

POST /message
Corpo: JSON de ChatRequest.
Regra de Negócio (Sessão): Se request.getSessionId() for nulo, gerar um novo UUID.randomUUID() antes de passar para o serviço. Salvar/criar a sessão no banco de dados (relacionada ao tenant da request) se for o primeiro contato.
Ação: Chamar chatOrchestratorService.processMessage(sessionId, message).
Retorno: Construir e retornar o ChatResponse com status HTTP 200 (OK).

GET /sessions/{sessionId}/history (Opcional, mas recomendado)
Ação: Buscar no MySQL (via Repository) o histórico de mensagens dessa sessão específica para repopular a tela caso o usuário recarregue a página no frontend.
Retorno: Lista de objetos contendo role (USER/ASSISTANT) e content.

4. Tratamento Global de Erros (GlobalExceptionHandler)

Para evitar que o Tomcat vaze stack traces de erros de Java (o que é uma falha de segurança), crie um @RestControllerAdvice.

Tratadores (@ExceptionHandler):
IllegalArgumentException / Validações: Retornar HTTP 400 (Bad Request) com mensagem amigável (Ex: "A mensagem não pode estar vazia").
UnauthorizedException (Da Spec 003): Retornar HTTP 401 (Unauthorized).
Exceções de Timeout do LangChain4j (LLM demorou a responder): Retornar HTTP 504 (Gateway Timeout) com mensagem informando instabilidade na IA.
Exception (Genérica): Retornar HTTP 500 com mensagem padrão genérica.

5. Critérios de Aceite (Acceptance Criteria)

Ao enviar um POST /api/v1/chat/message sem sessionId, a API deve retornar sucesso e incluir um sessionId válido na resposta, demonstrando que uma nova conversa foi iniciada.
Na segunda requisição, enviando o mesmo sessionId recebido na requisição anterior, a API não deve criar uma nova sessão, mas sim dar continuidade à conversa (testável validando se o LLM "lembra" do contexto da mensagem anterior).
Qualquer erro interno (ex: banco de dados fora do ar) deve ser capturado pelo GlobalExceptionHandler e retornar um JSON com chave de error no formato apropriado, nunca expondo detalhes do código fonte.
