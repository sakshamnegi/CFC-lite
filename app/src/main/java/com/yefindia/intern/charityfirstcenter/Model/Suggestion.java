package com.yefindia.intern.charityfirstcenter.Model;

public class Suggestion {
    private String suggestionText, senderName, senderEmail;

    public Suggestion(){  }

    public Suggestion(String suggestionText, String senderName, String senderEmail) {
        this.suggestionText = suggestionText;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSuggestionText() {
        return suggestionText;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }
}
