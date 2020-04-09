package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.knowhouse.thereceiptbook.AsynTaskClasses.GetPieChartTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SavePieChartTask;

public class PieChartClass {

    private Context context;

    public PieChartClass(String phoneNumber, View pieChartView,
                         Context context){
        this.context = context;

        if(isNetworkAvailable()){
            SavePieChartTask savePieChartTask = new SavePieChartTask(phoneNumber,pieChartView,
                    context);
            savePieChartTask.retrievePieChartValues();      //Call method to save and populate data into pie chart
        }else{
            GetPieChartTask getPieChartTask = new GetPieChartTask();
            getPieChartTask.execute(pieChartView,context);
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
