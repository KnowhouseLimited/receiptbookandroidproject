package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import androidx.core.widget.NestedScrollView;

import com.knowhouse.thereceiptbook.AsynTaskClasses.GetGraphDataTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SaveGraphTask;

public class GraphClass{

    private Context context;

    public GraphClass(String phoneNumber, String date, NestedScrollView view,
                      Context context){
        this.context = context;

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
