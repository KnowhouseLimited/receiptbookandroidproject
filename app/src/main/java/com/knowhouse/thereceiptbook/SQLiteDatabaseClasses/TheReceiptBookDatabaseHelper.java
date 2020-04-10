package com.knowhouse.thereceiptbook.SQLiteDatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class TheReceiptBookDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "thereceiptbook";
    private static final int DB_VERSION = 1;


    public TheReceiptBookDatabaseHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        Log.d("Database operation","Database created ...");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE WEATHER(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "town TEXT," +
                "humidity TEXT," +
                "pressure TEXT," +
                "wind TEXT," +
                "icon TEXT," +
                "temperature TEXT," +
                "feelslike TEXT," +
                "cloud TEXT);");

        db.execSQL("CREATE TABLE DATAFEED(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "issuer_company TEXT," +
                "no_of_receipt_issued_per_day TEXT," +
                "total_amount_made_per_day TEXT," +
                "item_with_most_receipt TEXT," +
                "total_of_items_sold_per_day TEXT," +
                "date TEXT);");

        db.execSQL("CREATE TABLE GRAPHFEED(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "item TEXT," +
                "entry NUMERIC," +
                "date TEXT);");

        db.execSQL("CREATE TABLE TRANSACTIONSFEED(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "application TEXT," +
                "image TEXT," +
                "phone_number NUMERIC," +
                "full_name TEXT," +
                "identity NUMERIC);");

        db.execSQL("CREATE TABLE PIECHARTFEED(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "item TEXT," +
                "entry NUMERIC);");

        Log.d("Database operation","Table created ...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertWeather(SQLiteDatabase db, String date,
                                      String town, String humidity, String wind,
                                      String icon,String temperature, String feelsLike, String cloud,String pressure){
        ContentValues weatherValues = new ContentValues();
        weatherValues.put("date",date);
        weatherValues.put("town",town);
        weatherValues.put("humidity",humidity);
        weatherValues.put("wind",wind);
        weatherValues.put("icon", icon);
        weatherValues.put("temperature",temperature);
        weatherValues.put("feelslike",feelsLike);
        weatherValues.put("cloud",cloud);
        weatherValues.put("pressure",pressure);
        db.insert("WEATHER",null,weatherValues);
    }

    public void insertDataFeed(SQLiteDatabase db, String issuerCompanyName,
                               String tNumberOfReceipt,String totalAmountMade,
                               String highestReceiptIssuedItem,String totalItemSold,String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put("issuer_company",issuerCompanyName);
        contentValues.put("no_of_receipt_issued_per_day",tNumberOfReceipt);
        contentValues.put("total_amount_made_per_day",totalAmountMade);
        contentValues.put("item_with_most_receipt",highestReceiptIssuedItem);
        contentValues.put("total_of_items_sold_per_day",totalItemSold);
        contentValues.put("date",date);
        db.insert("DATAFEED",null,contentValues);
    }

    public void insertGraphFeed(SQLiteDatabase db, String item,float entry,String date){

        ContentValues contentValues = new ContentValues();
        contentValues.put("item",item);
        contentValues.put("entry",entry);
        contentValues.put("date",date);
        db.insert("GRAPHFEED",null,contentValues);
    }

    public void insertTransactionFeed(SQLiteDatabase db, String date, String applicationText,String image,
                                      int phoneNumber,String fullName,int identity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",date);
        contentValues.put("application",applicationText);
        contentValues.put("image",image);
        contentValues.put("phone_number",phoneNumber);
        contentValues.put("full_name",fullName);
        contentValues.put("identity",identity);
        db.insert("TRANSACTIONSFEED",null,contentValues);
    }

    public void insertPieChartFeed(SQLiteDatabase db, String item,float entry){
        ContentValues contentValues = new ContentValues();
        contentValues.put("item",item);
        contentValues.put("entry",entry);
        db.insert("PIECHARTFEED",null,contentValues);
    }
}
