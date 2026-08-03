# Plan - Spec 011

## 1. Pesquisa e Preparação
- Analisar a estrutura atual de cada client (`Clients/laravel-biohealth`, `Clients/wordpress-initech`, `Clients/nextjs-acmecorp`).
- Rodar o build do `frontend-widget` para gerar o artefato `public/embed.js`.

## 2. Abordagem de Distribuição do Widget
Para evitar problemas de CORS e links quebrados localmente, criaremos um script simples em node na raiz ou utilizaremos o próprio backend Spring Boot como provedor do `embed.js` na rota estática `/static/embed.js`.
Porém, como a integração é focada no frontend, uma forma simples é **copiar o arquivo `embed.js` para as pastas de assets públicos de cada um dos 3 clients**.

### Script Automatizado
Vamos criar um `scripts/deploy-widget.mjs` no `frontend-widget` que, logo após o build, copie automaticamente o `public/embed.js` para:
- `../Clients/laravel-biohealth/public/widget/embed.js`
- `../Clients/wordpress-initech/wp-content/themes/twentytwentyfour/assets/embed.js` (ou o tema correspondente)
- `../Clients/nextjs-acmecorp/public/widget/embed.js`

## 3. Implementação nos Clientes

### BioHealth (Laravel)
- Modificar `welcome.blade.php`.
- Inserir a tag `<script src="/widget/embed.js" data-tenant="BIOHEALTH-123" data-theme="#10b981"></script>`.
- Adicionar uma div raiz `<div id="chat-widget-root"></div>`.

### Initech (WordPress)
- Modificar o tema default (`index.php` ou `functions.php`).
- Inserir a tag script ou `wp_enqueue_script`.
- Configurar as props do script para o tenant INITECH e uma cor de tema padrão.

### Acme Corp (Next.js)
- No `app/layout.tsx`, adicionar o componente `Script` do next apontando para `/widget/embed.js`.
- Configurar a tag com `data-tenant` e cor da Acme Corp.

## 4. Validação
- Verificar em cada ambiente local (NextJS dev, Laravel artisan serve e WP Docker) se o widget inicializa sem sobrepor os estilos do site.
- Garantir que o prefixo Tailwind do widget evita vazamentos de CSS para o site base.
