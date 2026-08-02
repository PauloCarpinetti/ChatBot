import React, { useState, useRef, useEffect } from 'react';
import { ChatMessage } from './ChatMessage';
import { ChatInput } from './ChatInput';
import { sendMessage } from '../../services/api';
import { X } from 'lucide-react';

interface ChatWindowProps {
  token: string;
  onClose: () => void;
}

interface MessageData {
  role: 'USER' | 'ASSISTANT';
  content: string;
}

export function ChatWindow({ token, onClose }: ChatWindowProps) {
  const [messages, setMessages] = useState<MessageData[]>([]);
  const [sessionId, setSessionId] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages, isLoading]);

  const handleSend = async (text: string) => {
    // Adiciona msg do usuário
    setMessages((prev) => [...prev, { role: 'USER', content: text }]);
    setIsLoading(true);

    try {
      const response = await sendMessage(text, sessionId, token);
      
      // Atualiza session se for a 1a vez
      if (!sessionId && response.sessionId) {
        setSessionId(response.sessionId);
      }

      setMessages((prev) => [
        ...prev,
        { role: 'ASSISTANT', content: response.message || response.reply || response.answer || "Resposta recebida." }
      ]);
    } catch (err: any) {
      setMessages((prev) => [
        ...prev,
        { role: 'ASSISTANT', content: "Ops, ocorreu um erro de conexão." }
      ]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="tw-chat-flex tw-chat-flex-col tw-chat-w-80 tw-chat-h-[500px] tw-chat-bg-white tw-chat-rounded-2xl tw-chat-shadow-2xl tw-chat-overflow-hidden tw-chat-border tw-chat-border-gray-200">
      {/* Header */}
      <div 
        className="tw-chat-flex tw-chat-justify-between tw-chat-items-center tw-chat-p-4 tw-chat-text-white tw-chat-bg-chat-primary"
      >
        <h3 className="tw-chat-font-semibold tw-chat-text-base tw-chat-m-0">Atendimento</h3>
        <button onClick={onClose} className="tw-chat-text-white hover:tw-chat-text-gray-200 tw-chat-transition-colors">
          <X size={20} />
        </button>
      </div>

      {/* Messages Area */}
      <div className="tw-chat-flex-1 tw-chat-overflow-y-auto tw-chat-p-4 tw-chat-bg-gray-50">
        {messages.length === 0 && (
          <div className="tw-chat-text-center tw-chat-text-gray-500 tw-chat-text-sm tw-chat-mt-10">
            Como posso ajudar hoje?
          </div>
        )}
        {messages.map((msg, index) => (
          <ChatMessage key={index} role={msg.role} content={msg.content} />
        ))}
        {isLoading && (
          <div className="tw-chat-flex tw-chat-justify-start tw-chat-mb-4">
            <div className="tw-chat-bg-gray-200 tw-chat-text-gray-500 tw-chat-px-4 tw-chat-py-2 tw-chat-rounded-2xl tw-chat-rounded-bl-none tw-chat-text-sm tw-chat-animate-pulse">
              Digitando...
            </div>
          </div>
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Input Area */}
      <ChatInput onSend={handleSend} disabled={isLoading} />
    </div>
  );
}
