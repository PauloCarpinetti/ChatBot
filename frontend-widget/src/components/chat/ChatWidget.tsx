"use client";

import React, { useState } from 'react';
import { ChatWindow } from './ChatWindow';
import { MessageCircle } from 'lucide-react';

interface ChatWidgetProps {
  token: string;
}

export function ChatWidget({ token }: ChatWidgetProps) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="twchat:fixed twchat:bottom-6 twchat:right-6 twchat:z-50 twchat:flex twchat:flex-col twchat:items-end">
      {isOpen && (
        <div className="twchat:mb-4">
          <ChatWindow 
            token={token} 
            onClose={() => setIsOpen(false)} 
          />
        </div>
      )}
      
      {!isOpen && (
        <button
          onClick={() => setIsOpen(true)}
          className="twchat:p-4 twchat:text-white twchat:bg-chat-primary twchat:rounded-full twchat:shadow-lg hover:twchat:scale-105 hover:twchat:bg-chat-primary-hover twchat:transition-all twchat:duration-200"
        >
          <MessageCircle size={28} />
        </button>
      )}
    </div>
  );
}
