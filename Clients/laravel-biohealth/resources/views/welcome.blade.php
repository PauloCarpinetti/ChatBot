<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BioHealth - Manual do Convênio</title>

        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">

        <!-- Styles -->
        @if (file_exists(public_path('build/manifest.json')) || file_exists(public_path('hot')))
            @vite(['resources/css/app.css', 'resources/js/app.js'])
        @else
            <script src="https://cdn.tailwindcss.com"></script>
        @endif

        <style>
            body {
                font-family: 'Inter', sans-serif;
                background-color: #0f172a;
                color: #f8fafc;
                margin: 0;
                overflow-x: hidden;
            }
            .glass-panel {
                background: rgba(30, 41, 59, 0.7);
                backdrop-filter: blur(12px);
                -webkit-backdrop-filter: blur(12px);
                border: 1px solid rgba(255, 255, 255, 0.1);
                border-radius: 1.5rem;
            }
            .bg-blob {
                position: absolute;
                width: 50vw;
                height: 50vw;
                background: radial-gradient(circle, rgba(56, 189, 248, 0.2) 0%, rgba(15, 23, 42, 0) 70%);
                border-radius: 50%;
                z-index: -1;
                top: -10vw;
                left: -10vw;
                filter: blur(40px);
                animation: float 10s ease-in-out infinite;
            }
            .bg-blob-2 {
                position: absolute;
                width: 40vw;
                height: 40vw;
                background: radial-gradient(circle, rgba(16, 185, 129, 0.15) 0%, rgba(15, 23, 42, 0) 70%);
                border-radius: 50%;
                z-index: -1;
                bottom: -5vw;
                right: -5vw;
                filter: blur(40px);
                animation: float 12s ease-in-out infinite reverse;
            }
            @keyframes float {
                0% { transform: translate(0, 0); }
                50% { transform: translate(20px, 30px); }
                100% { transform: translate(0, 0); }
            }
        </style>
    </head>
    <body class="antialiased min-h-screen flex items-center justify-center relative">
        <div class="bg-blob"></div>
        <div class="bg-blob-2"></div>
        
        <main class="container mx-auto px-6 py-12 flex flex-col items-center justify-center relative z-10 w-full max-w-4xl">
            
            <div class="glass-panel p-8 md:p-12 shadow-2xl w-full transform transition-all duration-500 hover:scale-[1.01]">
                
                <header class="mb-10 text-center">
                    <div class="inline-flex items-center justify-center p-3 bg-sky-500/10 rounded-2xl mb-4 border border-sky-500/20">
                        <svg class="w-8 h-8 text-sky-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
                        </svg>
                    </div>
                    <h1 class="text-4xl md:text-5xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-sky-400 to-emerald-400 tracking-tight">
                        BioHealth
                    </h1>
                    <p class="text-slate-400 mt-2 text-lg font-medium tracking-wide uppercase text-sm">Manual do Convênio</p>
                </header>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-8 mb-10">
                    <div class="p-6 rounded-2xl bg-slate-800/50 border border-slate-700/50 hover:bg-slate-800 transition-colors duration-300">
                        <div class="w-10 h-10 rounded-full bg-emerald-500/20 flex items-center justify-center mb-4">
                            <svg class="w-5 h-5 text-emerald-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                        </div>
                        <h3 class="text-xl font-semibold text-white mb-2">Cobertura Nacional</h3>
                        <p class="text-slate-300 leading-relaxed">
                            A BioHealth oferece cobertura em todo o território nacional. Você está protegido onde quer que esteja.
                        </p>
                    </div>

                    <div class="p-6 rounded-2xl bg-slate-800/50 border border-slate-700/50 hover:bg-slate-800 transition-colors duration-300">
                        <div class="w-10 h-10 rounded-full bg-rose-500/20 flex items-center justify-center mb-4">
                            <svg class="w-5 h-5 text-rose-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                        </div>
                        <h3 class="text-xl font-semibold text-white mb-2">Carências</h3>
                        <ul class="text-slate-300 space-y-2">
                            <li class="flex items-start">
                                <span class="text-emerald-400 mr-2">✓</span>
                                <span>Consultas de emergência <strong>não possuem carência</strong>.</span>
                            </li>
                            <li class="flex items-start">
                                <span class="text-amber-400 mr-2">!</span>
                                <span>Exames de alta complexidade têm carência de <strong>180 dias</strong>.</span>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="mt-8 bg-gradient-to-r from-sky-900/40 to-indigo-900/40 rounded-2xl p-6 border border-sky-500/20 text-center relative overflow-hidden">
                    <div class="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAiIGhlaWdodD0iMjAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGNpcmNsZSBjeD0iMiIgY3k9IjIiIHI9IjIiIGZpbGw9InJnYmEoMjU1LDI1NSwyNTUsMC4wNSkiLz48L3N2Zz4=')] opacity-30"></div>
                    <div class="relative z-10">
                        <p class="text-sky-200 font-medium mb-2">Em caso de internação, ligue imediatamente para:</p>
                        <a href="tel:0800-BIO-1234" class="inline-flex items-center text-3xl md:text-4xl font-bold text-white hover:text-sky-300 transition-colors">
                            <svg class="w-8 h-8 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"></path></svg>
                            0800-BIO-1234
                        </a>
                    </div>
                </div>

            </div>
            
            <footer class="mt-12 text-center text-slate-500 text-sm">
                &copy; {{ date('Y') }} BioHealth. Todos os direitos reservados.
            </footer>
        </main>
        
        <!-- Chatbot Widget Integration -->
        <script>
            window.ChatBotConfig = {
                token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRJZCI6IjBkODlkMTIyLTIwOTYtNDY5Ny04YTVmLWFiYWUxZWJiM2M1YSIsImlhdCI6MTc4NTg4OTA5OH0.cNCoXV7lzG4lmV98BqKk0mYdUMZ3RqGin3pyDzvT4OA',
                themeColor: '#10b981'
            };
        </script>
        <script src="{{ asset('widget/embed.js') }}"></script>
    </body>
</html>
