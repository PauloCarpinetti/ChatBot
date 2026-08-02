import React from 'react';
import ReactMarkdown from 'react-markdown';

interface ChatMessageProps {
  role: 'USER' | 'ASSISTANT';
  content: string;
}

export function ChatMessage({ role, content }: ChatMessageProps) {
  const isUser = role === 'USER';

  return (
    <div className={`tw-chat-flex tw-chat-w-full tw-chat-mb-4 ${isUser ? 'tw-chat-justify-end' : 'tw-chat-justify-start'}`}>
      <div
        className={`tw-chat-max-w-[80%] tw-chat-p-3 tw-chat-rounded-2xl ${isUser
          ? 'tw-chat-bg-chat-primary tw-chat-text-white tw-chat-rounded-br-none'
          : 'tw-chat-bg-gray-100 tw-chat-text-gray-800 tw-chat-rounded-bl-none'
          }`}
      >
        {isUser ? (
          <p className="tw-chat-text-sm tw-chat-m-0">{content}</p>
        ) : (
          <div className="tw-chat-text-sm tw-chat-prose tw-chat-prose-sm">
            <ReactMarkdown>{content}</ReactMarkdown>
          </div>
        )}
      </div>
    </div>
  );
}
