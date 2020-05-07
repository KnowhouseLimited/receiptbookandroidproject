package com.knowhouse.thereceiptbook.model;

import androidx.annotation.NonNull;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String senderCompany;
    private String time;
    private String image;

    public Chat(){}

    public Chat(String sender, String receiver, String message,String image,
                String senderCompany,String time ) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.senderCompany = senderCompany;
        this.time = time;
        this.image = image;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderCompany() {
        return senderCompany;
    }

    public void setSenderCompany(String senderCompany) {
        this.senderCompany = senderCompany;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
