Perfil Corporativo e Base de Conhecimento Estruturada: Acme Corp

1. Visão Geral e Identidade Organizacional

A Acme Corp posiciona-se no mercado como uma organização orientada a dados que prioriza a transparência e a resolutividade em suas operações de atendimento. No ecossistema de Inteligência Artificial Generativa, a empresa reconhece que a documentação estruturada de suas diretrizes é o alicerce para uma estratégia de Retrieval-Augmented Generation (RAG) eficaz. Esta base de conhecimento foi projetada para mitigar ambiguidades, garantindo que o motor de busca vetorial recupere informações precisas e alinhe a automação às regras de negócio vigentes.

A missão da Acme Corp no relacionamento com o cliente é centralizar a comunicação para assegurar a integridade da informação em todos os pontos de contato. Para otimizar a captura de entidades e a indexação semântica, o canal oficial de suporte é definido de forma unificada:

Canal Oficial de Atendimento e Suporte: sac@acmecorp.com

Esta centralização facilita a governança de dados e estabelece um ponto de verdade único para o treinamento e operação de assistentes virtuais, conectando a identidade da marca à sua operacionalização logística e de vendas.

2. Ecossistema de Vendas e Serviços Relacionados

A eficiência logística e o suporte pós-venda são os pilares que sustentam a experiência do usuário e a retenção na Acme Corp. Para um Arquiteto de Bases de Conhecimento, é imperativo que os serviços disponíveis sejam mapeados com precisão técnica para alimentar o banco vetorial, evitando que o modelo de linguagem responda por serviços não oferecidos pela companhia.

O atendimento voltado para logística é especializado na resolução de dúvidas de frete, utilizando exclusivamente o e-mail sac@acmecorp.com como interface de comunicação. Para fins de ingestão de dados, os serviços autorizados pela política corporativa são:

* Suporte a Logística e Frete: Esclarecimento de dúvidas sobre prazos, modalidades de envio e rastreamento de mercadorias.
* Gestão de Trocas e Devoluções: Processamento de solicitações de substituição de produtos conforme as normas de elegibilidade da empresa.

A delimitação estrita desses serviços prepara o ambiente para a aplicação das diretrizes de manutenção do produto e governança de prazos.

3. Diretrizes de Pós-Venda e Política de Trocas

Uma política de trocas transparente é uma ferramenta estratégica para reduzir a fricção no suporte e eliminar as "alucinações" de modelos de linguagem (LLMs). Ao fornecer regras claras, permitimos que o sistema RAG realize o grounding (ancoragem) das respostas em fatos concretos, garantindo segurança jurídica e operacional.

Com base no documento normativo acme_corp_politica.pdf, o fluxo de atendimento deve seguir os seguintes critérios de ação:

Ação de Validação (Elegibilidade)	Critério de Rejeição (Restrições)
Validar se a solicitação ocorre em até 30 dias após a data da compra.	Rejeitar trocas de produtos adquiridos em regime de "saldo".
Processar a troca apenas para produtos dentro do prazo regulamentar.	Bloquear solicitações que excedam o limite de 30 dias corridos.

O cumprimento mandatório desses requisitos é essencial para a integridade da conta do cliente e para a automação eficiente da logística reversa.

4. Estruturação Técnica para Ingestão em Banco Vetorial

Para que as informações da Acme Corp sejam recuperadas com alta precisão em um ambiente multi-tenant, aplicamos uma lógica de fragmentação (chunking) atômica. O objetivo é converter o texto bruto em vetores matemáticos (embeddings) através de modelos como o text-embedding-3-small, permitindo que a busca por similaridade de cosseno identifique o fragmento mais relevante para a consulta do usuário.

Abaixo, os dados foram convertidos em Fragmentos de Conhecimento otimizados, incluindo tags de contexto para aumentar a pontuação de relevância no Retrieval:

* [Política de Trocas: Prazo]
  * Conteúdo: A Acme Corp permite a troca de produtos em até 30 dias após a compra.
  * Keywords: prazo de troca, 30 dias, validade.
* [Política de Trocas: Elegibilidade de Saldo]
  * Conteúdo: Produtos de saldo não são elegíveis para troca ou substituição.
  * Keywords: produtos de saldo, restrição de troca, itens promocionais.
* [Suporte Logístico: Canal de Contato]
  * Conteúdo: Dúvidas relacionadas a frete e logística de entrega devem ser enviadas para sac@acmecorp.com.
  * Keywords: contato frete, e-mail suporte, dúvidas logística.
* [Identidade: Canal SAC]
  * Conteúdo: O canal oficial e centralizado de atendimento da Acme Corp é o sac@acmecorp.com.
  * Keywords: SAC, atendimento oficial, canal primário.

Para garantir o isolamento total dos dados no Vector DB e evitar que informações da Acme Corp vazem para outros clientes, cada fragmento é associado obrigatoriamente a um tenant_id nos metadados. O acesso a esses dados é controlado via autenticação baseada em JWT, garantindo que o pipeline RAG filtre as buscas exclusivamente para o identificador da Acme Corp.
