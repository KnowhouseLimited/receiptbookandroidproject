package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.knowhouse.thereceiptbook.Adapters.WeatherFeedAdapter;
import com.knowhouse.thereceiptbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherClass {

    private Context context;
    private RecyclerView weatherRecyclerView;
    private View view;
    private String city;


    public WeatherClass(String city, RecyclerView weatherRecyclerView, View view,
                        Context context) {

        this.context = context;
        this.weatherRecyclerView = weatherRecyclerView;
        this.view = view;
        this.city = city;

    }

    public void retrieveWeatherData(){
        /*
         * Recycler view for the weather section
         */
        URL url = createURL(city);
        if(url != null){
            GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
            getLocalWeatherTask.execute(url);
        }
        else{
            Snackbar.make(weatherRecyclerView.findViewById(R.id.coordinatorLayout),
                    R.string.invalid_url,Snackbar.LENGTH_LONG).show();
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

    //Making the REST web service call to get weather data and
    //saves the data to a local HTML file
    private class GetWeatherTask
            extends AsyncTask<URL,Void, JSONObject> {
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
                        Snackbar.make(view.findViewById(R.id.coordinatorLayout),
                                R.string.read_error,Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                }
                else{
                    Snackbar.make(view.findViewById(R.id.coordinatorLayout),
                            R.string.read_error,Snackbar.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                Snackbar.make(view.findViewById(R.id.coordinatorLayout),
                        R.string.connect_error,Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                assert connection != null;
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

            //Declaration of the values for the weather cardview
            double humidity = main.getDouble("humidity");
            double pressure = main.getDouble("pressure");
            double wind = windJSON.getDouble("speed");
            String icon = weatherObject.getString("icon");
            double temperature = main.getDouble("temp");
            double feelsLike = main.getDouble("feels_like");
            String cloud = weatherObject.getString("description");

            WeatherFeedAdapter weatherFeedAdapter = new WeatherFeedAdapter(dateLong,townString, humidity,
                    pressure, wind, icon, temperature, feelsLike, cloud,context);
            weatherRecyclerView.setAdapter(weatherFeedAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
