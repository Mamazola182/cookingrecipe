package com.example.projectprm392.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ChatMessage {
    @SerializedName("role")
    private String role; // "user" or "assistant"

    @SerializedName("content")
    private String content;

    @SerializedName("timestamp")
    private Date timestamp;

    public ChatMessage() {
        this.role = "user";
        this.content = "";
        this.timestamp = new Date();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
