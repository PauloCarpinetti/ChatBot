import React from 'react';
import { createRoot } from 'react-dom/client';
import { ChatWidget } from './components/chat/ChatWidget';
// Import removed because Tailwind CSS is compiled separately and injected by build-widget.mjs
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
  const themeColor = config.themeColor || '#2563EB';

  // Inject CSS variables for dynamic colors
  // Set both the original names and the Tailwind v4 prefixed names to ensure compatibility
  rootElement.style.setProperty('--chat-primary-color', themeColor);
  rootElement.style.setProperty('--chat-primary-hover', themeColor);
  
  rootElement.style.setProperty('--twchat-color-chat-primary', themeColor);
  rootElement.style.setProperty('--twchat-color-chat-primary-hover', themeColor);

  const root = createRoot(rootElement);
  root.render(<ChatWidget token={token} />);
}

// Inicializa o widget assim que o script for carregado
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initWidget);
} else {
  initWidget();
}
