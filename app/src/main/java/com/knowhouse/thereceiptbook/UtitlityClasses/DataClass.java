package com.knowhouse.thereceiptbook.UtitlityClasses;

public class DataClass {

    private String totalNumberOfCustomer;       //string to hold total number of customers for the day
    private String totalNumberOfPurchasedItems;      //String to hold total number of purchased items day
    private String totalAmountOfSoldItems;          //String to hold the total amount of everything sold for the day
    private int userId;                             //int to hold the user id


    //Constructor
    public DataClass(int userId){

        this.userId = userId;   //set the user id
    }

    //Getter function to get totalNumberOfCustomers transacted with for the day
    public String getTotalNumberOfCustomer() {
        return totalNumberOfCustomer;
    }

    //Setter function for the total number of customers transacted with for the day
    public void setTotalNumberOfCustomer(String totalNumberOfCustomer) {
        this.totalNumberOfCustomer = totalNumberOfCustomer;
    }

    //getter for total number of purchased items
    public String getTotalNumberOfPurchasedItems() {
        return totalNumberOfPurchasedItems;
    }

    //setter for total number of purchased items
    public void setTotalNumberOfPurchasedItems(String totalNumberOfPurchasedItems) {
        this.totalNumberOfPurchasedItems = totalNumberOfPurchasedItems;
    }

    //getter for total amount of purchased items
    public String getTotalAmountOfSoldItems() {
        return totalAmountOfSoldItems;
    }

    //setter for total amount of purchased items
    public void setTotalAmountOfSoldItems(String totalAmountOfSoldItems) {
        this.totalAmountOfSoldItems = totalAmountOfSoldItems;
    }

    //function to get the user id
    public int getUserId() {
        return userId;
    }
}
