# Spec 011 - Integração dos Clientes Frontend

## 1. Objetivo
Integrar o Widget de Chat desenvolvido na Spec 007 aos três clientes de testes localizados na pasta `Clients/` do repositório. O objetivo é demonstrar e validar a interoperabilidade e o isolamento do widget em diferentes ecossistemas (Laravel, WordPress e Next.js) e garantir que o chatbot mantenha as regras e identidades visuais de cada tenant.

## 2. Escopo de Integração

O widget (`embed.js`), que é o build exportado pelo projeto `frontend-widget`, deverá ser incorporado nas seguintes aplicações clientes:

1. **BioHealth (Laravel)**
   - **Localização:** `Clients/laravel-biohealth/`
   - **Método de injeção:** Incorporar a chamada do script no arquivo `welcome.blade.php`.
   - **Configuração do Tema:** Utilizar cores voltadas à identidade da BioHealth e configurar o `tenantId` correspondente ao ambiente.

2. **Initech (WordPress)**
   - **Localização:** `Clients/wordpress-initech/`
   - **Método de injeção:** Incorporar a chamada do script inserindo na tag `<head>` ou diretamente no `index.php`.
   - **Configuração do Tema:** Utilizar identidade corporativa da Initech e seu respectivo `tenantId`.

3. **Acme Corp (Next.js)**
   - **Localização:** `Clients/nextjs-acmecorp/`
   - **Método de injeção:** Utilizar a tag `Script` do Next.js no `app/layout.tsx` ou `app/page.tsx`.
   - **Configuração do Tema:** Utilizar a configuração visual e tenant da Acme Corp.

## 3. Lógica de Deploy e Sincronização
O build exporta `embed.js` na pasta `public/` do `frontend-widget`. Para simular um uso real, podemos criar um script que copia o `embed.js` para as pastas `public` de cada cliente ou servir o arquivo estático através do `frontend-widget` e injetá-lo por URL.

## 4. Critérios de Aceite (Acceptance Criteria)
- O widget deverá carregar com sucesso nos 3 ambientes.
- O chatbot deve apresentar coloração temática específica para cada client.
- Inicializar a configuração do chatbot e validar a comunicação com o backend sem erros de CORS.
- Não devem haver vazamentos ou conflitos de estilo CSS com as aplicações hospedeiras.
