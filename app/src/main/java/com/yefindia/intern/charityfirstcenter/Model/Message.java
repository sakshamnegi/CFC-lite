package com.yefindia.intern.charityfirstcenter.Model;

import java.util.Date;

public class Message {
    private String messageText, senderUserId, receiverUserId;
    private long messageTime;

    public Message(){}

    public Message(String messageText, String senderUserId, String receiverUserId) {
        this.messageText = messageText;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;

        //set time to current time
        messageTime = new Date().getTime();
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public long getMessageTime() {
        return messageTime;
    }
}
