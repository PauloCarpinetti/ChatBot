package br.com.paulo.chatbot.presentation.dto;

import java.time.Instant;
import java.util.UUID;

public class ChatResponse {
    
    private UUID sessionId;
    private String reply;
    private Instant timestamp;

    public ChatResponse() {
    }

    public ChatResponse(UUID sessionId, String reply, Instant timestamp) {
        this.sessionId = sessionId;
        this.reply = reply;
        this.timestamp = timestamp;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
