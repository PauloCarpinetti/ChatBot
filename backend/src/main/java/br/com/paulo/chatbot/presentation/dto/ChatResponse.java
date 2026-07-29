package br.com.paulo.chatbot.presentation.dto;

import java.time.Instant;
import java.util.UUID;

public class ChatResponse {
    
    private UUID sessionId;
    private String response;
    private Instant timestamp;

    public ChatResponse() {
    }

    public ChatResponse(UUID sessionId, String response, Instant timestamp) {
        this.sessionId = sessionId;
        this.response = response;
        this.timestamp = timestamp;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
