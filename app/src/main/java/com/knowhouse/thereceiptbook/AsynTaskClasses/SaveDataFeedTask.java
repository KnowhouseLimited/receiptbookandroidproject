package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.thereceiptbook.Constants;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.SQLiteDatabaseClasses.TheReceiptBookDatabaseHelper;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SaveDataFeedTask {

    private View view;
    private Context context;
    private String phoneNumber;
    private String date;

    public SaveDataFeedTask(View view, Context context,
                            String phoneNumber,String date){
        this.view = view;
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.date = date;
    }


    public void retrieveDataFeed(){

        final RequestQueue requestQueue = MySingleton.getInstance(context)
                .getRequestQueue();
        requestQueue.start();   //start the request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_DATE_FEED,
                response -> {
                    try {
                        TheReceiptBookDatabaseHelper helper = new TheReceiptBookDatabaseHelper(context);    //This is the receipt book database helper object
                        SQLiteDatabase db = helper.getWritableDatabase();   //this is the sqlite database object
                        db.delete("DATAFEED",null,null);
                        JSONArray obj = new JSONArray(response);  //JSon object to get object information from database
                        JSONObject[] object  = new JSONObject[obj.length()];
                        ArrayList<String> listStrings = new ArrayList<>();
                        for(int i = 0; i<obj.length();i++)
                        {
                            object[i] = obj.getJSONObject(i);
                            if (!object[i].getBoolean("error")) {   //if for json object error in database, this message will be retrieved
                                String issuerCompanyName = object[i].getString("issuerCompany");   //variable to hold the issuer of receipts company name
                                String noOfReceiptIssuedPerDay = String.valueOf(object[i].getInt("noReceiptIssued"));   //variable to hold the number of receipts issued in a day
                                String totalOfItemsSoldPerDay = String.valueOf(object[i].getInt("totalItemsSold"));    //variable to hold the number of items sold in a day

                                Locale locale = new Locale("English","Ghana","GH");
                                String symbols = "GHS ";
                                DecimalFormat currency = (DecimalFormat) NumberFormat.getCurrencyInstance();
                                DecimalFormatSymbols symbol = new DecimalFormatSymbols(locale);
                                symbol.setCurrencySymbol(symbols);
                                currency.setDecimalFormatSymbols(symbol);
                                String totalAmountMadePerDay = currency.format(object[i].getInt("totalPriceOfItemsSold"));      //variable to hold the number of items gotten in a day

                                String itemWithHighestReceiptNumberPerDay = object[i].getString("itemWithHighestReceipt");   //variable to hold the number of highest receipt issued item

                                helper.insertDataFeed(db,issuerCompanyName,noOfReceiptIssuedPerDay, //calls the insertDataFeed method of TheReceiptBookDatabaseHelper
                                        totalAmountMadePerDay,itemWithHighestReceiptNumberPerDay,totalOfItemsSoldPerDay,date);
                                listStrings.add(issuerCompanyName);
                                listStrings.add(noOfReceiptIssuedPerDay);
                                listStrings.add(totalAmountMadePerDay);
                                listStrings.add(itemWithHighestReceiptNumberPerDay);
                                listStrings.add(totalOfItemsSoldPerDay);
                                populateView(listStrings);
                            }else {
                                Toast.makeText(context,"Check parameters",Toast.LENGTH_LONG).show();
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestQueue.stop();
                }, error -> {
            Toast.makeText(context,"Please check internet connection",Toast.LENGTH_LONG).show();
            requestQueue.stop();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("user_phone_no", phoneNumber);
                params.put("current_date", date);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void populateView(ArrayList<String> listStrings){
        TextView user_company = view.findViewById(R.id.issuer_company_name);
        TextView number_of_receipt = view.findViewById(R.id.tNumberOfReceipt);
        TextView total_amount_made = view.findViewById(R.id.totalAmountMade);
        TextView highestReceiptIssuedItem = view.findViewById(R.id.highest_receipt_issued_item);
        TextView totalItemSold = view.findViewById(R.id.numberOfItemsSold);

        user_company.setText(context.getString(R.string.company_giant_inc,listStrings.get(0)));
        number_of_receipt.setText(context.getString(R.string.total_number_of_receipt_issued_,listStrings.get(1)));
        total_amount_made.setText(context.getString(R.string.total_amount_made,listStrings.get(2)));
        highestReceiptIssuedItem.setText(context.getString(R.string.item_with_most_receipt,listStrings.get(3)));
        totalItemSold.setText(context.getString(R.string.total_number_of_items_sold,listStrings.get(4)));
    }
}
