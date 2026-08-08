export interface Tenant {
  id: string;
  name: string;
  status: 'active' | 'inactive';
  apiKey: string;
  systemPrompt: string;
  createdAt: string;
}

export interface ChatMessage {
  id: string;
  role: 'user' | 'assistant' | 'system';
  content: string;
  timestamp: string;
}

export interface ChatSession {
  id: string;
  tenantId: string;
  userId: string;
  status: 'active' | 'completed';
  startedAt: string;
  messages: ChatMessage[];
}

export interface DashboardMetrics {
  totalTenants: number;
  activeChats: number;
  apiCallsToday: number;
  averageResponseTime: number; // in ms
}
