import React from 'react';
import { createRoot } from 'react-dom/client';
import { ChatWidget } from './components/chat/ChatWidget';
// Importa o CSS para que o bundler possa processar as classes Tailwind
import './app/globals.css';

function initWidget() {
  const rootId = 'chat-widget-root';
  let rootElement = document.getElementById(rootId);

  if (!rootElement) {
    rootElement = document.createElement('div');
    rootElement.id = rootId;
    document.body.appendChild(rootElement);
  }

  // O token e outras configs podem ser passados globalmente
  // pelo site hospedeiro na variável window.ChatBotConfig
  const config = (window as any).ChatBotConfig || {};
  const token = config.token || 'MISSING_TOKEN';

  const root = createRoot(rootElement);
  root.render(<ChatWidget token={token} />);
}

// Inicializa o widget assim que o script for carregado
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initWidget);
} else {
  initWidget();
}
