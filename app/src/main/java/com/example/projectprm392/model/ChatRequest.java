package com.example.projectprm392.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChatRequest {
    @SerializedName("message")
    private String message;

    @SerializedName("conversationHistory")
    private List<ChatMessage> conversationHistory;

    public ChatRequest() {
        this.message = "";
        this.conversationHistory = new ArrayList<>();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChatMessage> getConversationHistory() {
        return conversationHistory;
    }

    public void setConversationHistory(List<ChatMessage> conversationHistory) {
        this.conversationHistory = conversationHistory;
    }
}
