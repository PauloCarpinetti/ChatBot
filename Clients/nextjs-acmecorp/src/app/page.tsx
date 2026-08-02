import React from 'react';

export default function Home() {
  return (
    <main className="min-h-screen bg-slate-950 text-white selection:bg-cyan-500/30 relative overflow-hidden font-sans">
      
      {/* Background Ornaments */}
      <div className="absolute top-0 left-0 w-full h-full overflow-hidden z-0 pointer-events-none">
        <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-cyan-600/20 rounded-full blur-[120px] mix-blend-screen" />
        <div className="absolute bottom-[-10%] right-[-5%] w-[50%] h-[50%] bg-indigo-600/20 rounded-full blur-[120px] mix-blend-screen" />
      </div>

      <div className="relative z-10 max-w-6xl mx-auto px-6 py-20 flex flex-col min-h-screen">
        
        {/* Header */}
        <header className="flex justify-between items-center mb-24">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-cyan-400 to-blue-600 flex items-center justify-center font-bold text-xl shadow-lg shadow-cyan-500/30">
              AC
            </div>
            <span className="text-2xl font-bold tracking-tighter">Acme Corp</span>
          </div>
          <nav className="hidden md:flex gap-8 text-sm font-medium text-slate-400">
            <a href="#" className="hover:text-white transition-colors">Produtos</a>
            <a href="#" className="text-cyan-400">Política de Trocas</a>
            <a href="#" className="hover:text-white transition-colors">Contato</a>
          </nav>
        </header>

        {/* Hero Section */}
        <section className="flex flex-col items-center text-center max-w-3xl mx-auto mb-20">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-slate-800/50 border border-slate-700 mb-8">
            <span className="relative flex h-2 w-2">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-cyan-400 opacity-75"></span>
              <span className="relative inline-flex rounded-full h-2 w-2 bg-cyan-500"></span>
            </span>
            <span className="text-xs font-medium text-slate-300 uppercase tracking-wider">Atendimento ao Consumidor</span>
          </div>
          <h1 className="text-5xl md:text-7xl font-extrabold tracking-tight mb-6 bg-clip-text text-transparent bg-gradient-to-r from-white via-slate-200 to-slate-500">
            Bem vindo a Acme Corp!
          </h1>
          <p className="text-xl text-slate-400 font-light leading-relaxed">
            Conheça nossa <strong className="text-white font-medium">Política de Trocas</strong>. Estamos comprometidos com a sua satisfação em todas as compras realizadas conosco.
          </p>
        </section>

        {/* Cards Grid */}
        <section className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-20">
          
          <div className="group relative p-8 rounded-3xl bg-slate-900/50 border border-slate-800 backdrop-blur-sm hover:bg-slate-800/50 transition-colors">
            <div className="absolute inset-0 bg-gradient-to-br from-cyan-500/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity rounded-3xl" />
            <div className="relative z-10">
              <div className="w-14 h-14 rounded-2xl bg-cyan-500/10 flex items-center justify-center mb-6 border border-cyan-500/20 text-cyan-400 group-hover:scale-110 transition-transform">
                <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
              </div>
              <h3 className="text-2xl font-bold mb-3 text-slate-100">Prazo de 30 Dias</h3>
              <p className="text-slate-400 leading-relaxed">
                Para trocar um produto, você tem até <strong className="text-cyan-400 font-semibold">30 dias</strong> após a data de compra. O item deve estar na embalagem original.
              </p>
            </div>
          </div>

          <div className="group relative p-8 rounded-3xl bg-slate-900/50 border border-slate-800 backdrop-blur-sm hover:bg-slate-800/50 transition-colors">
            <div className="absolute inset-0 bg-gradient-to-br from-red-500/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity rounded-3xl" />
            <div className="relative z-10">
              <div className="w-14 h-14 rounded-2xl bg-red-500/10 flex items-center justify-center mb-6 border border-red-500/20 text-red-400 group-hover:scale-110 transition-transform">
                <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M18.364 18.364A9 9 0 005.636 5.636m12.728 12.728A9 9 0 015.636 5.636m12.728 12.728L5.636 5.636" /></svg>
              </div>
              <h3 className="text-2xl font-bold mb-3 text-slate-100">Produtos de Saldo</h3>
              <p className="text-slate-400 leading-relaxed">
                Atenção: <strong className="text-red-400 font-semibold">Produtos de saldo não podem ser trocados</strong>. Verifique as condições do item antes de finalizar sua compra.
              </p>
            </div>
          </div>

        </section>

        {/* Contact CTA */}
        <section className="mt-auto relative overflow-hidden rounded-3xl bg-gradient-to-r from-blue-900/40 to-indigo-900/40 border border-blue-500/20 p-8 md:p-12 flex flex-col md:flex-row items-center justify-between gap-8">
          <div className="relative z-10">
            <h2 className="text-2xl md:text-3xl font-bold text-white mb-2">Dúvidas sobre Frete?</h2>
            <p className="text-blue-200">Nossa equipe de atendimento está pronta para ajudar.</p>
          </div>
          <a href="mailto:sac@acmecorp.com" className="relative z-10 group flex items-center justify-center gap-3 px-8 py-4 bg-white text-slate-900 font-bold rounded-2xl hover:bg-blue-50 transition-colors shadow-xl shadow-blue-900/50">
            <svg className="w-5 h-5 text-blue-600 group-hover:-translate-y-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" /></svg>
            sac@acmecorp.com
          </a>
        </section>

      </div>
    </main>
  );
}
