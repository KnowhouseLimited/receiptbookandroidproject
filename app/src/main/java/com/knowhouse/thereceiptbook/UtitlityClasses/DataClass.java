package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.thereceiptbook.AsynTaskClasses.GetDataFeedTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SaveDataFeedTask;
import com.knowhouse.thereceiptbook.Constants;
import com.knowhouse.thereceiptbook.FragmentActivities.MainActivityFragment;
import com.knowhouse.thereceiptbook.R;
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

public class DataClass{

    private Context context;
    private NestedScrollView view;

    //Function to feed data to the data feed layout through the
    public DataClass(String phoneNumber, String date, NestedScrollView view,Context context){
        this.context = context;
        this.view = view;

        if(isNetworkAvailable()){
            SaveDataFeedTask saveAndPopulate = new SaveDataFeedTask(view,context,phoneNumber,date);
            saveAndPopulate.retrieveDataFeed();
        }else{
            GetDataFeedTask getDataFeed = new GetDataFeedTask();
            getDataFeed.execute(view,context,date);
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
