//Author: Enoch Viewu
//Cooperation: KnowHouse
//Date Modified: 4th Feb, 2020
//Code: The MainActivityFragment of the application

package com.knowhouse.thereceiptbook.FragmentActivities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowhouse.thereceiptbook.Adapters.WeatherFeedAdapter;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.UtitlityClasses.WeatherClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private WeatherFeedAdapter adapter;
    private WeatherClass myDataSet;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView weatherRecycler;

    private long date;
    private String town;
    private double humidity;
    private double pressure;
    private double wind;
    private String icon;
    private double temperature;
    private double feelsLike;
    private String cloud;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        weatherRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_main,
                container, false);



        weatherRecycler.setHasFixedSize(true);
        weatherRecycler.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getContext());
        weatherRecycler.setLayoutManager(layoutManager);

        URL url = createURL("Sunyani");
        if(url != null){
            GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
            getLocalWeatherTask.execute(url);
        }
        else{
            Snackbar.make(weatherRecycler.findViewById(R.id.coordinatorLayout),
                    R.string.invalid_url,Snackbar.LENGTH_LONG).show();
        }
        return weatherRecycler;
    }

    //Create an openweathermap.org web service URL using city
    private URL createURL(String city){
        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.web_service_url);

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


    //Making the REST web service call to get weather data and
    //saves the data to a local HTML file
    private class GetWeatherTask
            extends AsyncTask<URL,Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection connection = null;

            try{
                connection = (HttpURLConnection) urls[0].openConnection();
                int response = connection.getResponseCode();

                if(response == HttpURLConnection.HTTP_OK){
                    StringBuilder builder = new StringBuilder();

                    try(BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))){

                        String line;

                        while ((line = reader.readLine()) != null){
                            builder.append(line);
                        }
                    }
                    catch (IOException e){
                        Snackbar.make(weatherRecycler.findViewById(R.id.coordinatorLayout),
                                R.string.read_error,Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                }
                else{
                    Snackbar.make(weatherRecycler.findViewById(R.id.coordinatorLayout),
                            R.string.read_error,Snackbar.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                Snackbar.make(weatherRecycler.findViewById(R.id.coordinatorLayout),
                        R.string.connect_error,Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                connection.disconnect();    //close the HttpURLConnection
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            convertJSONArrayToArrayList(jsonObject);
        }
    }

    //Create weather objects from JSONObject containing the forecast
    private void convertJSONArrayToArrayList(JSONObject forecast){

        try{
            JSONObject main = forecast.getJSONObject("main");
            JSONObject windJSON = forecast.getJSONObject("wind");
            String townString = forecast.getString("name");
            long dateLong = forecast.getLong("dt");
            JSONArray weatherArray = forecast.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);

            this.humidity = main.getDouble("humidity");
            this.pressure = main.getDouble("pressure");
            this.wind = windJSON.getDouble("speed");
            this.icon = weatherObject.getString("icon");
            this.temperature = main.getDouble("temp");
            this.feelsLike = main.getDouble("feels_like");
            this.cloud = weatherObject.getString("description");
            this.date = dateLong;
            this.town = townString;

            WeatherClass weatherClass = new WeatherClass(date,town,humidity,pressure,
                    wind,icon,temperature,feelsLike,cloud);

            WeatherFeedAdapter weatherFeedAdapter = new WeatherFeedAdapter(weatherClass,getContext());
            weatherRecycler.setAdapter(weatherFeedAdapter);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


    }

}
