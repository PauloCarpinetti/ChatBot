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
    <div className="tw-chat-fixed tw-chat-bottom-6 tw-chat-right-6 tw-chat-z-50 tw-chat-flex tw-chat-flex-col tw-chat-items-end">
      {isOpen && (
        <div className="tw-chat-mb-4">
          <ChatWindow 
            token={token} 
            onClose={() => setIsOpen(false)} 
          />
        </div>
      )}
      
      {!isOpen && (
        <button
          onClick={() => setIsOpen(true)}
          className="tw-chat-p-4 tw-chat-text-white tw-chat-bg-chat-primary tw-chat-rounded-full tw-chat-shadow-lg hover:tw-chat-scale-105 hover:tw-chat-bg-chat-primary-hover tw-chat-transition-all tw-chat-duration-200"
        >
          <MessageCircle size={28} />
        </button>
      )}
    </div>
  );
}
