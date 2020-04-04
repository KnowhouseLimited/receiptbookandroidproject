package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.knowhouse.thereceiptbook.AsynTaskClasses.GetGraphDataTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SaveGraphTask;
import com.knowhouse.thereceiptbook.Constants;
import com.knowhouse.thereceiptbook.FragmentActivities.MainActivityFragment;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphClass{

    private Context context;

    private NestedScrollView view;

    public GraphClass(String phoneNumber, String date, NestedScrollView view,
                      Context context){
        this.context = context;
        this.view = view;

        if(isNetworkAvailable()){
            SaveGraphTask saveGraphData = new SaveGraphTask(view,context,phoneNumber,date);
            saveGraphData.retrieveGraphValues();
        }else{
            GetGraphDataTask getGraphData = new GetGraphDataTask();
            getGraphData.execute(view,context,date);
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
