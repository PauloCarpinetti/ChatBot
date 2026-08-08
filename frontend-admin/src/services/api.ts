import type { Tenant, ChatSession, DashboardMetrics } from '../types';

const HEADERS = {
  'Content-Type': 'application/json',
  'Authorization': 'Basic YWRtaW46YWRtaW4=' // admin:admin
};

export const api = {
  getMetrics: async (): Promise<DashboardMetrics> => {
    const res = await fetch('/api/v1/admin/analytics/metrics', { headers: HEADERS });
    if (!res.ok) throw new Error('Failed to fetch metrics');
    return res.json();
  },
  
  getTenants: async (): Promise<Tenant[]> => {
    const res = await fetch('/api/v1/admin/tenants', { headers: HEADERS });
    if (!res.ok) throw new Error('Failed to fetch tenants');
    return res.json();
  },

  getChats: async (): Promise<ChatSession[]> => {
    const res = await fetch('/api/v1/admin/analytics/chats', { headers: HEADERS });
    if (!res.ok) throw new Error('Failed to fetch chats');
    return res.json();
  }
};
