package com.knowhouse.thereceiptbook;

import android.graphics.Bitmap;

public class TransactionsClass {
    private int[] id;
    private String[] transactions;
    private String fullName;
    private String[] date;
    private String phoneNumber;
    private Bitmap image;

    public TransactionsClass(int[] id, String fullName, String phoneNumber, String[] transactions, String[] date, Bitmap image){
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.transactions = transactions;
        this.date = date;
        this.image = image;
    }

    public int[] getId() {
        return id;
    }

    public String[] getTransactions() {
        return transactions;
    }

    public String[] getDate() {
        return date;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Bitmap getImage() {
        return image;
    }
}
