package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.thereceiptbook.Adapters.DataFeedAdapter;
import com.knowhouse.thereceiptbook.Constants;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class DataClass {
    private String issuerCompanyName;   //variable to hold the issuer of receipts company name
    private String noOfReceiptIssuedPerDay;   //variable to hold the number of receipts issued in a day
    private String totalOfItemsSoldPerDay;    //variable to hold the number of items sold in a day
    private String totalAmountMadePerDay;      //variable to hold the number of items gotten in a day
    private String itemWithHighestReceiptNumberPerDay;   //variable to hold the number of highest receipt issued item

    private String phoneNumber;
    private String date;
    private Context context;
    private RecyclerView dataFeedRecyclerView;

    //Function to feed data to the data feed layout through the DataFeedAdapter
    public DataClass(String phoneNumber, String date, Context context,
                     RecyclerView dataFeedRecyclerView){

        this.phoneNumber = phoneNumber;
        this.date = date;
        this.context = context;
        this.dataFeedRecyclerView = dataFeedRecyclerView;
    }

    public void retrieveDataFeed(){
        final RequestQueue requestQueue = MySingleton.getInstance(context)
                .getRequestQueue();
        requestQueue.start();   //start the request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_DATE_FEED,
                response -> {
                    try {
                        JSONArray obj = new JSONArray(response);  //JSon object to get object information from database
                        JSONObject[] object  = new JSONObject[obj.length()];
                        for(int i = 0; i<obj.length();i++)
                        {
                            object[i] = obj.getJSONObject(i);
                            if (!object[i].getBoolean("error")) {   //if for json object error in database, this message will be retrieved
                                issuerCompanyName = object[i].getString("issuerCompany");   //variable to hold the issuer of receipts company name
                                noOfReceiptIssuedPerDay = String.valueOf(object[i].getInt("noReceiptIssued"));   //variable to hold the number of receipts issued in a day
                                totalOfItemsSoldPerDay = String.valueOf(object[i].getInt("totalItemsSold"));    //variable to hold the number of items sold in a day
                                NumberFormat currency = NumberFormat.getCurrencyInstance();
                                totalAmountMadePerDay = currency.format(object[i].getInt("totalPriceOfItemsSold"));      //variable to hold the number of items gotten in a day
                                itemWithHighestReceiptNumberPerDay = object[i].getString("itemWithHighestReceipt");   //variable to hold the number of highest receipt issued item

                                DataFeedAdapter dataFeedAdapter = new DataFeedAdapter(issuerCompanyName, noOfReceiptIssuedPerDay,
                                        totalOfItemsSoldPerDay, totalAmountMadePerDay, itemWithHighestReceiptNumberPerDay,context);

                                dataFeedRecyclerView.setAdapter(dataFeedAdapter);
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

}
