package com.example.projectprm392.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChatResponse {
    @SerializedName("reply")
    private String reply;

    @SerializedName("intent")
    private String intent;

    @SerializedName("mentionedRecipes")
    private List<String> mentionedRecipes;

    @SerializedName("suggestedRecipeIds")
    private List<Integer> suggestedRecipeIds;

    @SerializedName("suggestions")
    private List<String> suggestions;

    @SerializedName("quickReplies")
    private List<QuickReply> quickReplies;

    public ChatResponse() {
        this.reply = "";
        this.intent = "General";
        this.mentionedRecipes = new ArrayList<>();
        this.suggestedRecipeIds = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<String> getMentionedRecipes() {
        return mentionedRecipes;
    }

    public void setMentionedRecipes(List<String> mentionedRecipes) {
        this.mentionedRecipes = mentionedRecipes;
    }

    public List<Integer> getSuggestedRecipeIds() {
        return suggestedRecipeIds;
    }

    public void setSuggestedRecipeIds(List<Integer> suggestedRecipeIds) {
        this.suggestedRecipeIds = suggestedRecipeIds;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public List<QuickReply> getQuickReplies() {
        return quickReplies;
    }

    public void setQuickReplies(List<QuickReply> quickReplies) {
        this.quickReplies = quickReplies;
    }
}
