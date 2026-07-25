# Constitution: Multi-Tenant Context-Aware RAG Chatbot

## 1. Princípios de Operação (Solo Developer)
- **Rationale:** Sendo um desenvolvedor único, complexidade arquitetural excessiva (ex: microserviços prematuros ou infraestrutura pesada) resulta em paralisia e débito técnico impossível de manter.
- **Rule:** Otimize para manutenibilidade, simplicidade e baixo atrito cognitivo.
- **How to apply:**
  - Adote o padrão Monolito Modular no backend antes de considerar microsserviços.
  - Prefira serviços gerenciados (PaaS como Vercel/Render, bancos de dados as a service como Pinecone/Supabase) em vez de hospedar e gerenciar infraestrutura própria, até que o custo force uma migração.
  - Ferramentas nativas do framework sempre têm prioridade sobre bibliotecas de terceiros.

## 2. Isolamento Multi-Tenant (Segurança Inegociável)
- **Rationale:** Como o chatbot atende diferentes segmentos/sistemas, um cliente NUNCA pode ter acesso aos dados (conversas ou banco vetorial) de outro. Um vazamento de dados inter-tenant anula a confiabilidade do produto.
- **Rule:** O isolamento de dados por `tenant_id` é o requisito não-funcional de maior prioridade em todo o ciclo de vida do código.
- **How to apply:**
  - Nenhuma rota de API que lide com dados do usuário ou contexto pode ser pública; todas exigem autenticação baseada em JWT contendo as claims do tenant.
  - Toda consulta ao banco de dados relacional (MySQL) DEVE incluir o filtro `WHERE tenant_id = ?`.
  - Toda busca de similaridade no Banco Vetorial DEVE usar os metadados de filtro (`filter: { tenantId: '...' }`). Não faça buscas globais no index vetorial.

## 3. Abstração do LLM e Pipeline RAG
- **Rationale:** LLMs são commodities que evoluem e mudam de preço rapidamente. O sistema não pode ser fortemente acoplado à API específica da OpenAI, Anthropic ou modelos locais.
- **Rule:** Mantenha a separação rígida entre a Lógica de Negócio, o Retrieval (Busca Vetorial) e a Generation (Chamada ao LLM).
- **How to apply:**
  - Encapsule as chamadas ao modelo fundacional em uma interface genérica (ex: `ChatProviderInterface`).
  - O prompt de sistema deve impor limites estritos (guardrails). Exemplo mandatório no RAG: *"Você é um assistente restrito. Baseie-se APENAS no contexto fornecido. Se a resposta não constar no contexto, responda que não tem a informação."*
  - Não armazene chaves de API estáticas no repositório.

## 4. Frontend Acoplável (Widget)
- **Rationale:** O frontend do chatbot operará como um componente embarcado (widget/iframe) nos sistemas de terceiros e não pode interferir no ecossistema deles.
- **Rule:** O cliente de UI deve ter "Zero Colisão" com a aplicação hospedeira.
- **How to apply:**
  - Se estiver usando TailwindCSS, configure um prefixo (ex: `tw-chat-`) para evitar que as classes do chat modifiquem as cores/estruturas do site do cliente.
  - Encapsule o estado global localmente dentro do componente React/Next.js; não polua o `window` ou o escopo global do navegador.

## 5. Testabilidade e Qualidade
- **Rationale:** Um desenvolvedor solo não tem um time de QA para validar regressões. Os testes são a única garantia de que uma alteração não quebrou o pipeline RAG.
- **Rule:** O fluxo crítico de montagem de contexto deve ser testável de forma isolada, rápida e barata.
- **How to apply:**
  - Crie testes unitários mockando o retorno do LLM. O teste não deve fazer requisições reais para a OpenAI (para evitar custos e lentidão no CI/CD).
  - Teste exaustivamente se a função que monta o Prompt está recebendo os *chunks* corretos do banco vetorial.

## 6. Rituais e Disciplina de Código
Mesmo sendo um único desenvolvedor, o repositório segue um fluxo de trabalho profissional de colaboração assíncrona:

*   **Git Flow / Branching:** 
    *   A branch `main` é sagrada e reflete apenas o código funcional (produção).
    *   Nenhum commit é feito diretamente na `main`. 
    *   Todo desenvolvimento ocorre em branches isoladas, nomeadas por contexto (`feature/nome-da-feature`, `fix/descricao-do-bug`, `refactor/o-que-mudou`).
*   **Pull Requests (PRs):** 
    *   Toda integração de código é feita via Pull Request.
    *   Eu realizo o "self-review" (auto-revisão) de cada PR, garantindo que o diff faz sentido, não há código comentado e os padrões de linting foram respeitados.
*   **Commits:**
    *   Mensagens de commit devem ser curtas, imperativas e explicarem "o que" foi feito e "por que", utilizando [Conventional Commits](https://www.conventionalcommits.org/).
