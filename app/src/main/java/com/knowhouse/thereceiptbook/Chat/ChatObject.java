package com.knowhouse.thereceiptbook.Chat;

import com.knowhouse.thereceiptbook.model.UserObject;

import java.util.ArrayList;

public class ChatObject {
    private String key;
    private ArrayList<UserObject> value;

    public ChatObject(String key,ArrayList<UserObject> value){
        this.key = key;
        this.value = value;
    }

    public ChatObject(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<UserObject> getValue() {
        return value;
    }

    public void setValue(ArrayList<UserObject> value) {
        this.value = value;
    }
}
