import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import type { DashboardMetrics } from '../types';
import { Users, MessageSquare, Activity, Clock } from 'lucide-react';

export const Dashboard: React.FC = () => {
  const [metrics, setMetrics] = useState<DashboardMetrics | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchMetrics = async () => {
      try {
        const data = await api.getMetrics();
        setMetrics(data);
      } catch (error) {
        console.error("Failed to load metrics", error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchMetrics();
  }, []);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  const cards = [
    { title: 'Total Tenants', value: metrics?.totalTenants, icon: Users, color: 'text-blue-400', bg: 'bg-blue-400/10' },
    { title: 'Active Chats', value: metrics?.activeChats, icon: MessageSquare, color: 'text-green-400', bg: 'bg-green-400/10' },
    { title: 'API Calls (Today)', value: metrics?.apiCallsToday, icon: Activity, color: 'text-purple-400', bg: 'bg-purple-400/10' },
    { title: 'Avg Response', value: `${metrics?.averageResponseTime}ms`, icon: Clock, color: 'text-amber-400', bg: 'bg-amber-400/10' },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-textPrimary tracking-tight">System Overview</h2>
        <p className="text-textSecondary mt-1">Monitor the health and usage of your ChatBot instances.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {cards.map((card, idx) => (
          <div key={idx} className="glass-panel p-6 rounded-2xl relative overflow-hidden group hover:border-white/20 transition-all">
            <div className={`absolute top-0 right-0 p-4 opacity-10 group-hover:opacity-20 transition-opacity`}>
              <card.icon size={64} className={card.color} />
            </div>
            
            <div className="relative z-10">
              <div className={`w-12 h-12 rounded-xl flex items-center justify-center mb-4 ${card.bg} ${card.color}`}>
                <card.icon size={24} />
              </div>
              <p className="text-textSecondary text-sm font-medium">{card.title}</p>
              <h3 className="text-3xl font-bold text-textPrimary mt-1">{card.value}</h3>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-8">
        <div className="glass-panel rounded-2xl p-6 h-96 flex flex-col">
          <h3 className="text-lg font-semibold mb-4">Traffic Over Time</h3>
          <div className="flex-1 flex items-center justify-center border border-white/5 rounded-xl bg-black/20">
            <span className="text-textSecondary text-sm">[Chart Placeholder]</span>
          </div>
        </div>
        <div className="glass-panel rounded-2xl p-6 h-96 flex flex-col">
          <h3 className="text-lg font-semibold mb-4">Top Active Tenants</h3>
          <div className="flex-1 flex items-center justify-center border border-white/5 rounded-xl bg-black/20">
            <span className="text-textSecondary text-sm">[List Placeholder]</span>
          </div>
        </div>
      </div>
    </div>
  );
};
