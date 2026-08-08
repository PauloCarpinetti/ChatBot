import type { Tenant, ChatSession, DashboardMetrics } from '../types';

// Mock Data
const MOCK_TENANTS: Tenant[] = [
  { id: '1', name: 'Acme Corp', status: 'active', apiKey: 'sk-acme-1234', systemPrompt: 'You are a helpful assistant for Acme Corp.', createdAt: '2026-08-01T10:00:00Z' },
  { id: '2', name: 'Globex Inc', status: 'active', apiKey: 'sk-globex-5678', systemPrompt: 'You are a technical support bot for Globex.', createdAt: '2026-08-02T11:30:00Z' },
  { id: '3', name: 'Initech', status: 'inactive', apiKey: 'sk-ini-9999', systemPrompt: 'You are an HR bot.', createdAt: '2026-08-05T09:15:00Z' }
];

const MOCK_CHATS: ChatSession[] = [
  {
    id: 'chat-101',
    tenantId: '1',
    userId: 'user-xyz',
    status: 'completed',
    startedAt: '2026-08-07T14:20:00Z',
    messages: [
      { id: 'm1', role: 'user', content: 'How do I reset my password?', timestamp: '2026-08-07T14:20:05Z' },
      { id: 'm2', role: 'assistant', content: 'You can reset it by clicking "Forgot Password" on the login page.', timestamp: '2026-08-07T14:20:10Z' }
    ]
  }
];

export const api = {
  getMetrics: async (): Promise<DashboardMetrics> => {
    return new Promise(resolve => setTimeout(() => resolve({
      totalTenants: 3,
      activeChats: 42,
      apiCallsToday: 1530,
      averageResponseTime: 850
    }), 500));
  },
  
  getTenants: async (): Promise<Tenant[]> => {
    return new Promise(resolve => setTimeout(() => resolve([...MOCK_TENANTS]), 500));
  },

  getChats: async (): Promise<ChatSession[]> => {
    return new Promise(resolve => setTimeout(() => resolve([...MOCK_CHATS]), 500));
  }
};
