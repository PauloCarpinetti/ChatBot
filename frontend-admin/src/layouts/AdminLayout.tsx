import React from 'react';
import { Outlet, Link, useLocation } from 'react-router-dom';
import { LayoutDashboard, Users, MessageSquare, Settings, LogOut } from 'lucide-react';

export const AdminLayout: React.FC = () => {
  const location = useLocation();

  const navItems = [
    { name: 'Dashboard', path: '/', icon: LayoutDashboard },
    { name: 'Tenants', path: '/tenants', icon: Users },
    { name: 'Analytics', path: '/analytics', icon: MessageSquare },
  ];

  return (
    <div className="flex h-screen bg-background">
      {/* Sidebar */}
      <aside className="w-64 flex-shrink-0 border-r border-white/10 bg-surface backdrop-blur-md flex flex-col">
        <div className="h-16 flex items-center px-6 border-b border-white/10">
          <h1 className="text-xl font-bold bg-gradient-to-r from-primary to-secondary bg-clip-text text-transparent">
            ChatBot Admin
          </h1>
        </div>
        
        <nav className="flex-1 overflow-y-auto py-4">
          <ul className="space-y-1 px-3">
            {navItems.map((item) => {
              const isActive = location.pathname === item.path || 
                (item.path !== '/' && location.pathname.startsWith(item.path));
              
              return (
                <li key={item.name}>
                  <Link
                    to={item.path}
                    className={`flex items-center gap-3 px-3 py-2.5 rounded-lg transition-all ${
                      isActive 
                        ? 'bg-primary/20 text-primary font-medium' 
                        : 'text-textSecondary hover:bg-white/5 hover:text-textPrimary'
                    }`}
                  >
                    <item.icon size={20} className={isActive ? 'text-primary' : ''} />
                    {item.name}
                  </Link>
                </li>
              );
            })}
          </ul>
        </nav>

        <div className="p-4 border-t border-white/10">
          <button className="flex items-center gap-3 px-3 py-2 text-textSecondary hover:text-red-400 transition-colors w-full">
            <LogOut size={20} />
            <span>Logout</span>
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 flex flex-col overflow-hidden">
        <header className="h-16 flex items-center justify-end px-8 border-b border-white/10 bg-surface/50 backdrop-blur-sm">
          <div className="flex items-center gap-4">
            <button className="p-2 text-textSecondary hover:text-textPrimary rounded-full hover:bg-white/10 transition-colors">
              <Settings size={20} />
            </button>
            <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-primary to-secondary flex items-center justify-center text-sm font-medium">
              AD
            </div>
          </div>
        </header>
        
        <div className="flex-1 overflow-y-auto p-8 relative">
          {/* Subtle background glow effect */}
          <div className="absolute top-0 left-1/4 w-96 h-96 bg-primary/20 rounded-full blur-[100px] pointer-events-none" />
          <div className="relative z-10 h-full">
            <Outlet />
          </div>
        </div>
      </main>
    </div>
  );
};
