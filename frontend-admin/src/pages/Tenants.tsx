import React, { useEffect, useState } from 'react';
import { api } from '../services/api';
import type { Tenant } from '../types';
import { Plus, Search, Edit2, Trash2 } from 'lucide-react';

export const Tenants: React.FC = () => {
  const [tenants, setTenants] = useState<Tenant[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchTenants = async () => {
      try {
        const data = await api.getTenants();
        setTenants(data);
      } catch (error) {
        console.error("Failed to load tenants", error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchTenants();
  }, []);

  return (
    <div className="space-y-6 h-full flex flex-col">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-textPrimary tracking-tight">Tenants</h2>
          <p className="text-textSecondary mt-1">Manage your chatbot clients and their API keys.</p>
        </div>
        
        <button className="btn-primary flex items-center gap-2 shadow-lg shadow-primary/20">
          <Plus size={18} />
          <span>New Tenant</span>
        </button>
      </div>

      <div className="glass-panel rounded-2xl flex-1 flex flex-col overflow-hidden">
        <div className="p-4 border-b border-white/10 flex items-center gap-4">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-textSecondary" size={18} />
            <input 
              type="text" 
              placeholder="Search tenants by name or ID..." 
              className="input-field pl-10"
            />
          </div>
        </div>

        <div className="flex-1 overflow-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="border-b border-white/10 bg-white/5 text-textSecondary text-sm">
                <th className="font-medium p-4 pl-6">ID</th>
                <th className="font-medium p-4">Name</th>
                <th className="font-medium p-4">Status</th>
                <th className="font-medium p-4">API Key</th>
                <th className="font-medium p-4">Created At</th>
                <th className="font-medium p-4 text-right pr-6">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/10">
              {isLoading ? (
                <tr>
                  <td colSpan={6} className="p-8 text-center text-textSecondary">
                    <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
                  </td>
                </tr>
              ) : tenants.length === 0 ? (
                <tr>
                  <td colSpan={6} className="p-8 text-center text-textSecondary">
                    No tenants found.
                  </td>
                </tr>
              ) : (
                tenants.map(tenant => (
                  <tr key={tenant.id} className="hover:bg-white/5 transition-colors group">
                    <td className="p-4 pl-6 font-mono text-sm text-textSecondary">
                      {tenant.id}
                    </td>
                    <td className="p-4 font-medium text-textPrimary">
                      {tenant.name}
                    </td>
                    <td className="p-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium border ${
                        tenant.status === 'active' 
                          ? 'bg-green-500/10 text-green-400 border-green-500/20' 
                          : 'bg-slate-500/10 text-slate-400 border-slate-500/20'
                      }`}>
                        {tenant.status.charAt(0).toUpperCase() + tenant.status.slice(1)}
                      </span>
                    </td>
                    <td className="p-4 font-mono text-sm text-textSecondary">
                      ••••{tenant.apiKey.slice(-4)}
                    </td>
                    <td className="p-4 text-sm text-textSecondary">
                      {new Date(tenant.createdAt).toLocaleDateString()}
                    </td>
                    <td className="p-4 pr-6 text-right">
                      <div className="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                        <button className="p-2 text-textSecondary hover:text-primary transition-colors rounded-lg hover:bg-white/10">
                          <Edit2 size={16} />
                        </button>
                        <button className="p-2 text-textSecondary hover:text-red-400 transition-colors rounded-lg hover:bg-white/10">
                          <Trash2 size={16} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
