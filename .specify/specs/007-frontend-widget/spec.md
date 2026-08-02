# Spec: 007 - Frontend Widget (Next.js & React)

## 1. Objetivo

Desenvolver a interface do usuário (UI) do chatbot. O sistema deve ser um componente isolado (widget) em React/Next.js, capaz de ser embutido em diferentes sistemas clientes. Ele deve gerenciar a comunicação assíncrona com a API REST (criada na Spec 005/006), o estado do histórico da conversa ativa e lidar adequadamente com os tokens JWT.

## 2. Estrutura e Isolamento (Regras Base)

* **Estilos Nativos**: Utilizar Tailwind CSS, mas exigir a configuração de prefixo (`prefix: 'tw-chat-'`) definida na Spec 001. Nenhum estilo genérico como `h1 { font-weight: bold }` pode ser aplicado globalmente.
* **Estado**: O estado da aplicação deve ser gerenciado via React Hooks (`useState`, `useRef`, `useEffect`). Evitar gerenciadores de estado globais complexos (como Redux) para manter o widget leve.

### 2.1 Herança de CSS e Integração (Vazamento Intencional)

Para que o widget assuma a identidade visual do site hospedeiro, adotaremos a estratégia de injeção direta no DOM (evitando `iframe` e `Shadow DOM`).
* **Injeção de Script**: O widget será compilado em um único arquivo Javascript (ex: `embed.js`) que, ao ser inserido no site cliente, injetará a div root no DOM e renderizará a aplicação React nela.
* **Variáveis CSS para Temas**: O `tailwind.config.ts` mapeará suas cores principais para Variáveis CSS (Custom Properties) com valores de fallback. Ex: `primary: 'var(--chat-primary-color, #2563eb)'`. Isso permite que o site cliente sobrescreva facilmente as cores do widget através do seu próprio CSS.
* **Tipografia Fluida**: Não definir propriedades rígidas de `font-family` nos containers principais, permitindo que o widget herde naturalmente a tipografia (família e cor base) do `<body>` do site cliente.

## 3. Componentes Principais

Estrutura de componentes na pasta `frontend-widget/src/components/chat/`:

### 3.1. ChatWidget.tsx (Contêiner Principal)
* **Responsabilidade**: Gerenciar a visibilidade do chat (aberto/fechado), armazenar o token JWT recebido pelo sistema hospedeiro e invocar o `ChatWindow`.
* **Propriedades (Props)**:
  * `token` (String, Obrigatório): O JWT contendo o `tenantId`.
  * `themeColor` (String, Opcional): Cor principal (hex) para personalizar o header do widget.

### 3.2. ChatWindow.tsx (A Janela da Conversa)
* **Responsabilidade**: Exibir a lista de mensagens e o input de texto.
* **Estado Interno**:
  * `messages` (Array de objetos `{ role: 'USER' | 'ASSISTANT', content: string }`).
  * `sessionId` (String | null): Guarda o ID da conversa atual.
  * `isLoading` (Boolean): Controla o estado de "digitando..." da IA.

### 3.3. ChatMessage.tsx (Componente de Apresentação)
* **Responsabilidade**: Renderizar o balão visual da mensagem.
* **Comportamento**: Balões do USER ficam alinhados à direita com cor primária. Balões do ASSISTANT ficam à esquerda em cinza neutro. Suportar renderização de Markdown básico na resposta da IA.

### 3.4. ChatInput.tsx (Campo de Texto)
* **Responsabilidade**: Receber a digitação do usuário. Capturar a tecla Enter (ou o clique no botão de enviar) para disparar a função de envio para o componente pai.

## 4. Integração com a API (Services)

Criar o arquivo `frontend-widget/src/services/api.ts`:

Função `sendMessage(message: string, sessionId: string | null, token: string)`:
* Fazer um fetch (POST) para a URL da API (ex: `http://localhost:8080/api/v1/chat/message`).
* Incluir o Header: `Authorization: Bearer <token>`.
* Incluir o Body: `JSON.stringify({ message, sessionId })`.
* Tratar as respostas 401 (Token Inválido) ou 504 (Timeout da IA) e disparar erros legíveis para a UI.
* Retornar os dados (Response JSON completo).

## 5. Lógica de Interação (User Flow)

1. O usuário digita uma mensagem e clica em enviar.
2. O `ChatWindow` adiciona a mensagem do usuário ao state `messages` imediatamente (para resposta visual rápida) e seta `isLoading = true`.
3. Dispara a chamada `sendMessage`.
4. Ao receber o retorno (HTTP 200), o componente extrai a response e adiciona ao state `messages` como `role: ASSISTANT`.
5. Se for a primeira mensagem, extrair também o `sessionId` retornado e atualizá-lo no state, garantindo que as próximas requisições o utilizem.
6. A tela rola automaticamente para o final da conversa.

## 6. Critérios de Aceite (Acceptance Criteria)

* O widget não deve quebrar ou alterar a estilização de um arquivo HTML limpo onde for inserido.
* O estado de "carregando/digitando" deve ser visível e deve desabilitar o input enquanto a API não responde, prevenindo duplo envio por parte do usuário.
* Ao recarregar a página (F5), se não implementarmos armazenamento local (localStorage), o widget inicia limpo sem apresentar erros sistêmicos.
* Falhas na API (ex: serviço backend indisponível) devem apresentar um balão de erro genérico no chat ("Ops, ocorreu um erro de conexão.").
