package com.knowhouse.thereceiptbook.UtitlityClasses;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import androidx.core.widget.NestedScrollView;

import com.knowhouse.thereceiptbook.AsynTaskClasses.GetWeatherTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SaveWeatherTask;
import com.knowhouse.thereceiptbook.R;

import java.net.URL;
import java.net.URLEncoder;


public class WeatherClass{


    private String city;
    private View view;
    private Context context;

    public WeatherClass(String city,
                        View view, Context context) {
       this.city = city;
       this.view = view;
       this.context = context;
       retrieveWeatherData();
    }

    private void retrieveWeatherData(){
        /*
         * Recycler view for the weather section
         */


        if(isNetworkAvailable()){
            URL url = createURL(city);
            SaveWeatherTask getLocalWeatherTask = new SaveWeatherTask();
            getLocalWeatherTask.execute(url,view,context);
        }
        else{
            GetWeatherTask getWeatherTask = new GetWeatherTask();
            getWeatherTask.execute(view,context);
        }

    }

    //Create an openweathermap.org web service URL using city
    private URL createURL(String city){
        String apiKey = context.getString(R.string.api_key);
        String baseUrl = context.getString(R.string.web_service_url);

        try{
            //create URL for specified city and metric units (degrees celsius)
            String urlString = baseUrl + URLEncoder.encode(city,"UTF-8") +
                    "&units=metric&appid="+apiKey;
            return new URL(urlString);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
