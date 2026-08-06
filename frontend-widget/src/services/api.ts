export async function sendMessage(message: string, sessionId: string | null, token: string) {
  const url = 'http://localhost:8081/api/v1/chat/message';

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ message, sessionId }),
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new Error('Token Inválido ou Expirado (401).');
      }
      if (response.status === 504) {
        throw new Error('Timeout da IA (504). Tente novamente.');
      }
      throw new Error(`Erro na API (${response.status}).`);
    }

    const data = await response.json();
    return data;
  } catch (error: any) {
    console.error('Erro na chamada da API:', error);
    throw error;
  }
}

export async function sendMessageStream(
  message: string, 
  sessionId: string | null, 
  token: string,
  onChunk: (chunk: string) => void
): Promise<{ sessionId: string }> {
  const url = 'http://localhost:8081/api/v1/chat/message/stream';

  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({ message, sessionId }),
  });

  if (!response.ok) {
    if (response.status === 401) throw new Error('Token Inválido ou Expirado (401).');
    if (response.status === 504) throw new Error('Timeout da IA (504). Tente novamente.');
    throw new Error(`Erro na API (${response.status}).`);
  }

  const returnedSessionId = response.headers.get('X-Session-Id') || sessionId || '';
  
  if (!response.body) throw new Error("Sem suporte a streaming no navegador.");
  
  const reader = response.body.getReader();
  const decoder = new TextDecoder("utf-8");

  while (true) {
    const { done, value } = await reader.read();
    if (done) break;
    
    const chunk = decoder.decode(value, { stream: true });
    if (chunk) {
      onChunk(chunk);
    }
  }

  return { sessionId: returnedSessionId };
}
