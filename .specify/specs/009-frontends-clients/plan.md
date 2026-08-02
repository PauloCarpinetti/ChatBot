# Plano de Implementação: Frontends de Clientes

1. **Separação Estrutural**: Organizar os clientes em subpastas isoladas dentro do diretório raiz `Clients/`.
2. **Setup BioHealth**: Reestruturar uma aplicação Laravel, instalando dependências via composer e gerando o SQLite, além de construir o layout principal no arquivo `welcome.blade.php`.
3. **Setup Initech**: Construir um arquivo `index.html` autônomo referenciando bibliotecas externas (Tailwind, FontAwesome) via CDN, simulando as classes e estrutura de um site em WordPress.
4. **Setup Acme Corp**: Gerar um projeto Next.js em modo App Router via comando `create-next-app` com Tailwind embutido, configurando o design de "Política de Trocas" na página principal (`page.tsx`).
5. **Revisão e Versionamento**: Commitar e encapsular todos esses hospedeiros de teste num ambiente seguro, prontos para a injeção do script do Chatbot Widget em etapas posteriores.
