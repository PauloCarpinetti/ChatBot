# Spec 009: Ambientes de Hospedagem (Frontends)

## Descrição
Criação e configuração de três ambientes distintos de frontend para simular clientes hospedando o Chatbot Widget. O objetivo é garantir que o widget do chatbot seja totalmente agnóstico e funcione em qualquer tecnologia web hospedeira, isolando CSS e dependências.

## Regras de Negócio e Clientes
1. **BioHealth** (Laravel/Blade): Manual do convênio. Cobertura nacional, sem carência para emergência, 180 dias para alta complexidade.
2. **Initech** (HTML Mock - WordPress): Guia de software. Licenças corporativas, renovação anual, cancelamento com aviso de 60 dias e suporte 24/7.
3. **Acme Corp** (Next.js/React): Política de trocas. Trocas em até 30 dias após compra, sem troca para saldo. Contato via sac@acmecorp.com.

## Arquitetura Tecnológica
- **Laravel (PHP/Blade):** Framework robusto tradicional MVC.
- **HTML Estático:** Simulação de CMS como WordPress, representando sites não-SPA sem build complexo.
- **React (Next.js):** SPA/SSR moderno com App Router.
Todos estilizados utilizando TailwindCSS.
