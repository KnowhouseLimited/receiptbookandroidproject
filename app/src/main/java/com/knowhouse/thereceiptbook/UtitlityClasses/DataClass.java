package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;


import androidx.core.widget.NestedScrollView;

import com.knowhouse.thereceiptbook.AsynTaskClasses.GetDataFeedTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SaveDataFeedTask;


public class DataClass{

    private Context context;

    //Function to feed data to the data feed layout through the
    public DataClass(String phoneNumber, String date, View view, Context context){
        this.context = context;

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
