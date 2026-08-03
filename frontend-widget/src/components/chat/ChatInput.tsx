import React, { useState } from 'react';
import { Send } from 'lucide-react';

interface ChatInputProps {
  onSend: (message: string) => void;
  disabled: boolean;
}

export function ChatInput({ onSend, disabled }: ChatInputProps) {
  const [text, setText] = useState('');

  const handleSend = () => {
    if (text.trim() && !disabled) {
      onSend(text.trim());
      setText('');
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSend();
    }
  };

  return (
    <div className="twchat:flex twchat:gap-2 twchat:p-4 twchat:border-t twchat:border-gray-200 twchat:bg-white">
      <input
        type="text"
        className="twchat:flex-1 twchat:px-4 twchat:py-2 twchat:border twchat:border-gray-300 twchat:rounded-full twchat:focus:outline-none twchat:focus:border-chat-primary disabled:twchat:opacity-50 twchat:text-sm"
        placeholder="Digite sua mensagem..."
        value={text}
        onChange={(e) => setText(e.target.value)}
        onKeyDown={handleKeyDown}
        disabled={disabled}
      />
      <button
        onClick={handleSend}
        disabled={disabled || !text.trim()}
        className="twchat:p-2 twchat:bg-chat-primary twchat:text-white twchat:rounded-full hover:twchat:bg-chat-primary-hover disabled:twchat:opacity-50 twchat:transition-colors"
      >
        <Send size={20} />
      </button>
    </div>
  );
}
