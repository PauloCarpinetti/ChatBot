import React, { useState, useRef, useEffect } from 'react';
import { ChatMessage } from './ChatMessage';
import { ChatInput } from './ChatInput';
import { sendMessageStream } from '../../services/api';
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
      let isFirstChunk = true;
      
      const response = await sendMessageStream(text, sessionId, token, (chunk) => {
          if (isFirstChunk) {
              setMessages((prev) => [
                  ...prev,
                  { role: 'ASSISTANT', content: chunk }
              ]);
              isFirstChunk = false;
              setIsLoading(false); // hide loading indicator when first chunk arrives
          } else {
              setMessages((prev) => {
                  const newMessages = [...prev];
                  const lastMessageIndex = newMessages.length - 1;
                  newMessages[lastMessageIndex] = {
                      ...newMessages[lastMessageIndex],
                      content: newMessages[lastMessageIndex].content + chunk
                  };
                  return newMessages;
              });
          }
      });
      
      // Atualiza session se for a 1a vez
      if (!sessionId && response.sessionId) {
        setSessionId(response.sessionId);
      }

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
    <div className="twchat:flex twchat:flex-col twchat:w-80 twchat:h-[500px] twchat:bg-white twchat:rounded-2xl twchat:shadow-2xl twchat:overflow-hidden twchat:border twchat:border-gray-200">
      {/* Header */}
      <div 
        className="twchat:flex twchat:justify-between twchat:items-center twchat:p-4 twchat:text-white twchat:bg-chat-primary"
      >
        <h3 className="twchat:font-semibold twchat:text-base twchat:m-0">Atendimento</h3>
        <button onClick={onClose} className="twchat:text-white hover:twchat:text-gray-200 twchat:transition-colors">
          <X size={20} />
        </button>
      </div>

      {/* Messages Area */}
      <div className="twchat:flex-1 twchat:overflow-y-auto twchat:p-4 twchat:bg-gray-50">
        {messages.length === 0 && (
          <div className="twchat:text-center twchat:text-gray-500 twchat:text-sm twchat:mt-10">
            Como posso ajudar hoje?
          </div>
        )}
        {messages.map((msg, index) => (
          <ChatMessage key={index} role={msg.role} content={msg.content} />
        ))}
        {isLoading && (
          <div className="twchat:flex twchat:justify-start twchat:mb-4">
            <div className="twchat:bg-gray-200 twchat:text-gray-500 twchat:px-4 twchat:py-2 twchat:rounded-2xl twchat:rounded-bl-none twchat:text-sm twchat:animate-pulse">
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
