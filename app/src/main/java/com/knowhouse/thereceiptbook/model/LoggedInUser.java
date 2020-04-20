package com.knowhouse.thereceiptbook.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String name;
    private String phone;

    public LoggedInUser(){

    }

    public LoggedInUser(String phone,String name) {
        this.phone = phone;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
