import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import type { ChatSession } from '../types';
import { Bot, User, MessageSquare } from 'lucide-react';

export const Analytics: React.FC = () => {
  const [chats, setChats] = useState<ChatSession[]>([]);
  const [selectedChat, setSelectedChat] = useState<ChatSession | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchChats = async () => {
      try {
        const data = await api.getChats();
        setChats(data);
        if (data.length > 0) setSelectedChat(data[0]);
      } catch (error) {
        console.error("Failed to load chats", error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchChats();
  }, []);

  return (
    <div className="h-full flex flex-col space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-textPrimary tracking-tight">Chat Analytics</h2>
        <p className="text-textSecondary mt-1">Inspect conversation history and evaluate LLM responses.</p>
      </div>

      <div className="flex-1 flex gap-6 min-h-0">
        {/* Chat List (Sidebar) */}
        <div className="w-80 glass-panel rounded-2xl flex flex-col overflow-hidden">
          <div className="p-4 border-b border-white/10 font-semibold">
            Recent Sessions
          </div>
          <div className="flex-1 overflow-y-auto p-2 space-y-1">
            {isLoading ? (
               <div className="p-4 text-center text-textSecondary text-sm">Loading...</div>
            ) : chats.map(chat => (
              <button
                key={chat.id}
                onClick={() => setSelectedChat(chat)}
                className={`w-full text-left p-3 rounded-xl transition-all ${
                  selectedChat?.id === chat.id 
                    ? 'bg-primary/20 border border-primary/30' 
                    : 'hover:bg-white/5 border border-transparent'
                }`}
              >
                <div className="flex justify-between items-start mb-1">
                  <span className="font-medium text-sm text-textPrimary truncate">
                    {chat.messages[0]?.content || 'Empty Chat'}
                  </span>
                </div>
                <div className="flex justify-between items-center text-xs text-textSecondary">
                  <span>Tenant: {chat.tenantId}</span>
                  <span>{new Date(chat.startedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                </div>
              </button>
            ))}
          </div>
        </div>

        {/* Chat Viewer */}
        <div className="flex-1 glass-panel rounded-2xl flex flex-col overflow-hidden">
          {selectedChat ? (
            <>
              <div className="p-4 border-b border-white/10 flex items-center justify-between bg-white/5">
                <div>
                  <h3 className="font-semibold text-textPrimary">Session {selectedChat.id}</h3>
                  <p className="text-xs text-textSecondary mt-0.5">
                    Tenant: {selectedChat.tenantId} | User: {selectedChat.userId}
                  </p>
                </div>
                <span className="px-2.5 py-1 text-xs font-medium rounded-full bg-surface border border-white/10">
                  {new Date(selectedChat.startedAt).toLocaleString()}
                </span>
              </div>
              
              <div className="flex-1 overflow-y-auto p-6 space-y-6">
                {selectedChat.messages.map((msg) => {
                  const isUser = msg.role === 'user';
                  return (
                    <div key={msg.id} className={`flex gap-4 ${isUser ? 'flex-row-reverse' : ''}`}>
                      <div className={`w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 ${
                        isUser ? 'bg-primary text-white' : 'bg-emerald-500 text-white'
                      }`}>
                        {isUser ? <User size={16} /> : <Bot size={16} />}
                      </div>
                      
                      <div className={`max-w-[70%] rounded-2xl p-4 ${
                        isUser 
                          ? 'bg-primary/20 border border-primary/20 text-textPrimary rounded-tr-sm' 
                          : 'bg-white/5 border border-white/10 text-textPrimary rounded-tl-sm'
                      }`}>
                        <p className="whitespace-pre-wrap text-sm leading-relaxed">{msg.content}</p>
                        <span className="text-[10px] text-textSecondary mt-2 block opacity-60 text-right">
                          {new Date(msg.timestamp).toLocaleTimeString()}
                        </span>
                      </div>
                    </div>
                  );
                })}
              </div>
            </>
          ) : (
            <div className="flex-1 flex flex-col items-center justify-center text-textSecondary">
              <MessageSquare size={48} className="mb-4 opacity-20" />
              <p>Select a session to view the transcript</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
