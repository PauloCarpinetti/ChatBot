import React from 'react';
import ReactMarkdown from 'react-markdown';

interface ChatMessageProps {
  role: 'USER' | 'ASSISTANT';
  content: string;
}

export function ChatMessage({ role, content }: ChatMessageProps) {
  const isUser = role === 'USER';

  return (
    <div className={`twchat:flex twchat:w-full twchat:mb-4 ${isUser ? 'twchat:justify-end' : 'twchat:justify-start'}`}>
      <div
        className={`twchat:max-w-[80%] twchat:p-3 twchat:rounded-2xl ${isUser
          ? 'twchat:bg-chat-primary twchat:text-white twchat:rounded-br-none'
          : 'twchat:bg-gray-100 twchat:text-gray-800 twchat:rounded-bl-none'
          }`}
      >
        {isUser ? (
          <p className="twchat:text-sm twchat:m-0">{content}</p>
        ) : (
          <div className="twchat:text-sm twchat:prose twchat:prose-sm">
            <ReactMarkdown>{content}</ReactMarkdown>
          </div>
        )}
      </div>
    </div>
  );
}
