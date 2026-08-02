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
    <div className="tw-chat-flex tw-chat-gap-2 tw-chat-p-4 tw-chat-border-t tw-chat-border-gray-200 tw-chat-bg-white">
      <input
        type="text"
        className="tw-chat-flex-1 tw-chat-px-4 tw-chat-py-2 tw-chat-border tw-chat-border-gray-300 tw-chat-rounded-full tw-chat-focus:outline-none tw-chat-focus:border-chat-primary disabled:tw-chat-opacity-50 tw-chat-text-sm"
        placeholder="Digite sua mensagem..."
        value={text}
        onChange={(e) => setText(e.target.value)}
        onKeyDown={handleKeyDown}
        disabled={disabled}
      />
      <button
        onClick={handleSend}
        disabled={disabled || !text.trim()}
        className="tw-chat-p-2 tw-chat-bg-chat-primary tw-chat-text-white tw-chat-rounded-full hover:tw-chat-bg-chat-primary-hover disabled:tw-chat-opacity-50 tw-chat-transition-colors"
      >
        <Send size={20} />
      </button>
    </div>
  );
}
