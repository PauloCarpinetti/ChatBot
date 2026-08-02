import Image from "next/image";
import { ChatWidget } from "../components/chat/ChatWidget";

export default function Home() {
  return (
    <div className="flex flex-col flex-1 items-center justify-center bg-zinc-50 font-sans dark:bg-black min-h-screen">
      <main className="flex flex-1 w-full max-w-3xl flex-col items-center py-32 px-16 bg-white dark:bg-black sm:items-start">
        <Image
          className="dark:invert"
          src="/next.svg"
          alt="Next.js logo"
          width={100}
          height={20}
          priority
        />
        <div className="flex flex-col items-center gap-6 text-center sm:items-start sm:text-left mt-10">
          <h1 className="max-w-xs text-3xl font-semibold leading-10 tracking-tight text-black dark:text-zinc-50">
            Frontend Widget ChatBot Test Page
          </h1>
          <p className="max-w-md text-lg leading-8 text-zinc-600 dark:text-zinc-400">
            Clique no botão flutuante no canto inferior direito para interagir com a IA (via Widget).
          </p>
        </div>
      </main>
      
      {/* 
        Injeção do Widget para testes locais.
        O token deve ser substituído por um JWT válido gerado pela API (Spec 004) caso o backend esteja exigindo.
      */}
      <ChatWidget token="test-token-mock" themeColor="#000000" />
    </div>
  );
}
