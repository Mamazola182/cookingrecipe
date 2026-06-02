package com.example.projectprm392.model;

import com.google.gson.annotations.SerializedName;

public class QuickReply {
    @SerializedName("Label")
    private String label;

    @SerializedName("Action")
    private String action;

    public QuickReply() {
        this.label = "";
        this.action = "";
    }

    public QuickReply(String label, String action) {
        this.label = label;
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
