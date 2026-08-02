export async function sendMessage(message: string, sessionId: string | null, token: string) {
  const url = 'http://localhost:8080/api/v1/chat/message';

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
