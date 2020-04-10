package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.thereceiptbook.Adapters.ActivityFeedAdapter;
import com.knowhouse.thereceiptbook.Constants;
import com.knowhouse.thereceiptbook.ImageConverter;
import com.knowhouse.thereceiptbook.LoginSingleton.SharedPrefManager;
import com.knowhouse.thereceiptbook.SQLiteDatabaseClasses.TheReceiptBookDatabaseHelper;
import com.knowhouse.thereceiptbook.UtitlityClasses.TransactionsClass;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SaveTransactionTask {

    private final Context context;
    private final Activity activity;

    public SaveTransactionTask(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void loadTransactions(final RecyclerView activityFeedRecycler){

        final int userid = SharedPrefManager.getInstance(context).getUserID();    //get user id from SharedPrefManager class
        final int phoneNumber = SharedPrefManager.getInstance(context).getUserPhoneNumber();  //get phoneNumber from SharedPrefManager class
        final String  fullName = SharedPrefManager.getInstance(context).getUserFullName(); ////get fullName from SharedPrefManager class
        final String image_url = SharedPrefManager.getInstance(context).getUserImage();   //get user image from SharedPrefManager class


        final RequestQueue requestQueue = MySingleton.getInstance(context).getRequestQueue();
        requestQueue.start();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_TRANSACTIONFRAGMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray obj = new JSONArray(response);        //JSon Array to get array from php
                            int max = obj.length();                         //the maximum length of the array
                            int[] id = new int[max];                        //Getting the id of the from the database
                            String[] date = new String[max];            //Time the receipt was issued
                            String[] applicationText = new String[max];     //Receipt text
                            JSONObject transObject;     //Declaration of JSonObject

                            TheReceiptBookDatabaseHelper helper = new TheReceiptBookDatabaseHelper(context);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            db.delete("TRANSACTIONSFEED",null,null);

                            /*For loop the get the details from the database*/
                            for (int i = 0; i <obj.length() ; i++) {
                                transObject = obj.getJSONObject(i);
                                id[i] = transObject.getInt("id");
                                applicationText[i] = transObject.getString("transactions");
                                date[i] = transObject.getString("date");

                            }

                            //Transaction class to handle the information from the database

                            if (image_url.equals("null")){
                                TransactionsClass transactionsClass = new TransactionsClass(id,fullName,String.valueOf(phoneNumber),
                                        applicationText,date,null);
                                for(int i = 0;i<applicationText.length;i++){
                                    helper.insertTransactionFeed(db,date[i],applicationText[i],image_url,phoneNumber,
                                            fullName,id[i]);
                                }
                                ActivityFeedAdapter adapter = new ActivityFeedAdapter(transactionsClass);
                                activityFeedRecycler.setAdapter(adapter);
                            }else{
                                Bitmap bitmap = ImageConverter.convertFromStringToImg(image_url);
                                TransactionsClass transactionsClass = new TransactionsClass(id,fullName,String.valueOf(phoneNumber),
                                        applicationText,date,bitmap);
                                for(int i = 0;i<applicationText.length;i++){
                                    helper.insertTransactionFeed(db,date[i],applicationText[i],image_url,phoneNumber,
                                            fullName,id[i]);
                                }
                                ActivityFeedAdapter adapter = new ActivityFeedAdapter(transactionsClass);
                                activityFeedRecycler.setAdapter(adapter);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                            activityFeedRecycler.setLayoutManager(linearLayoutManager);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                requestQueue.stop();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("users_credential_id", String.valueOf(userid));
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
