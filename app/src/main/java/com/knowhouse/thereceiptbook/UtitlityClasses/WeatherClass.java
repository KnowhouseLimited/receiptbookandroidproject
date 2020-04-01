package com.knowhouse.thereceiptbook.UtitlityClasses;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.knowhouse.thereceiptbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class WeatherClass implements AsyncResponse{


    private String city;
    private NestedScrollView view;
    private Context context;

    private double humidity;
    private double pressure;
    private double wind;
    private String icon;
    private double temperature;
    private double feelsLike;
    private String cloud;
    private String townString;
    private long dateLong;



    private String dateString;
    private String stringTown;
    private String humidityString;
    private String pressureString;
    private String windString;
    private String iconString;
    private String temperatureString;
    private String feelsLikeString;
    private String cloudString;

    private Map<String, Bitmap> bitmaps = new HashMap<>();


    public WeatherClass(String city,
                        NestedScrollView view, Context context) {
       this.city = city;
       this.view = view;
       this.context = context;
       retrieveWeatherData();
    }

    @Override
    public void processFinish(JSONObject forecast) {
        convertJson(forecast);
        convertPrimitiveToString();
        populateCardView();
    }

    private void populateCardView() {
        TextView date = view.findViewById(R.id.weatherDate);
        TextView town = view.findViewById(R.id.weatherTown);
        TextView humidity = view.findViewById(R.id.weatherHumidity);
        TextView pressure = view.findViewById(R.id.weatherPressure);
        TextView wind = view.findViewById(R.id.weatherWind);
        ImageView icon = view.findViewById(R.id.weatherIcon);
        TextView temperature = view.findViewById(R.id.weatherTemp);
        TextView feelsLike = view.findViewById(R.id.weatherFeelsLike);
        TextView cloud = view.findViewById(R.id.weatherCloud);

        town.setText(stringTown);
        humidity.setText(context.getString((R.string.humidity_57),String.valueOf(humidityString)));
        pressure.setText(context.getString((R.string.pressure_1015_hpa),String.valueOf(pressureString)));
        wind.setText(context.getString((R.string.wind_14_km_h_sse),String.valueOf(windString)));
        temperature.setText(String.valueOf(temperatureString));
        feelsLike.setText(context.getString((R.string.feels_like),String.valueOf(feelsLikeString)));
        cloud.setText(cloudString);
        date.setText(dateString);

        if(bitmaps.containsKey(iconString)){
            icon.setImageBitmap(bitmaps.get(iconString));
        }else{
            LoadImageTask loadImageTask = new LoadImageTask(icon);
            loadImageTask.execute(iconString);
        }

    }


    private void retrieveWeatherData(){
        /*
         * Recycler view for the weather section
         */

        URL url = createURL(city);
        if(url != null){
            GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
            getLocalWeatherTask.delegate = this;
            getLocalWeatherTask.execute(url,view);
        }
        else{
            Snackbar.make(view.findViewById(R.id.coordinatorLayout),
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


    private void convertJson(JSONObject forecast) {

        try{
            JSONObject main = forecast.getJSONObject("main");
            JSONObject windJSON = forecast.getJSONObject("wind");
            JSONArray weatherArray = forecast.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);

            //Declaration of the values for the weather cardview
            //Declaration of the values for the weather cardview
            humidity = main.getDouble("humidity");
            pressure = main.getDouble("pressure");
            wind = windJSON.getDouble("speed");
            icon = weatherObject.getString("icon");
            temperature = main.getDouble("temp");
            feelsLike = main.getDouble("feels_like");
            cloud = weatherObject.getString("description");
            townString = forecast.getString("name");
            dateLong = forecast.getLong("dt");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void convertPrimitiveToString(){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        dateString = convertTimeStampToDay(this.dateLong);
        stringTown = this.townString;
        humidityString = NumberFormat.getPercentInstance().format(this.humidity/100.0);
        pressureString = numberFormat.format(this.pressure);
        windString = numberFormat.format(this.wind);
        iconString = "http://openweathermap.org/img/w/"+this.icon+".png";
        temperatureString = numberFormat.format(this.temperature) + "\u00B0C";
        feelsLikeString = numberFormat.format(this.feelsLike)+"\u00B0C";
        cloudString = this.cloud;
    }

    //Convert the date
    private  static String convertTimeStampToDay(long timeStamp){
        Calendar calendar = Calendar.getInstance();     //create calendar
        calendar.setTimeInMillis(timeStamp*1000);
        TimeZone tz = TimeZone.getDefault();        //get device's time zone

        //Adjust time for device's time zone
        calendar.add(Calendar.MILLISECOND,
                tz.getOffset(calendar.getTimeInMillis()));

        //SimpleDateFormat that returns the day's name
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM, d, yyyy - EEE", Locale.US);
        return dateFormatter.format(calendar.getTime());
    }

    //AsyncTask to load weather condition icons in a separate thread
    private class LoadImageTask extends AsyncTask<String,Void,Bitmap> {
        private ImageView imageView;        //display the thumbnail

        //store ImageView on which to set the downloaded Bitmap
        LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        //load image; params[0] is the String URL representing the image
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);   //create URL for image

                //open an HTTPURLConnection, get its InputStream
                //and download the image
                connection = (HttpURLConnection) url.openConnection();

                try (InputStream inputStream = connection.getInputStream()) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(params[0], bitmap);  //cache for later use
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                assert connection != null;
                connection.disconnect();    //close the HTTPURLConnection
            }

            return bitmap;
        }

        //set weather condition image in list item
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
