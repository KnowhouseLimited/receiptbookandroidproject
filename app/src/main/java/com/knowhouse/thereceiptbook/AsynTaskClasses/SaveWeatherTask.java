package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.SQLiteDatabaseClasses.TheReceiptBookDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class SaveWeatherTask
        extends AsyncTask<Object,Void, ArrayList<String>>{


    private Context context;
    private ArrayList<Object> list;
    private View view;
    private Map<String, Bitmap> bitmaps = new HashMap<>();
    private String encodedImage;



    @Override
    protected ArrayList<String> doInBackground(Object... obj) {
        HttpURLConnection connection = null;
        URL url = (URL)obj[0];
        view = (View)obj[1];
        context = (Context)obj[2];


        try{
            connection = (HttpURLConnection) url.openConnection();
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
                    Snackbar.make(view.findViewById(R.id.main_fragment_recycler),
                            R.string.read_error,Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                ArrayList<Object> list = convertJson(new JSONObject(builder.toString()));
                return convertAndSavePrimitiveToString(list);
            }
            else{
                Snackbar.make(view.findViewById(R.id.main_fragment_recycler),
                        R.string.read_error,Snackbar.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Snackbar.make(view.findViewById(R.id.main_fragment_recycler),
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
    protected void onPostExecute(ArrayList<String> list) {
        GetWeatherTask getWeatherTask = new GetWeatherTask();
        getWeatherTask.execute(view,context);
    }

    /**
     * This is the method that convert the JSON objects to the various
     * primitive types
     * @param forecast is the input parameter for the method
     * @return list is the output of the method
     */
    private ArrayList<Object> convertJson(JSONObject forecast) {

        ArrayList<Object> list = new ArrayList<>();

        try{
            JSONObject main = forecast.getJSONObject("main");
            JSONObject windJSON = forecast.getJSONObject("wind");
            JSONArray weatherArray = forecast.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);

            //Declaration of the values for the weather cardview
            double humidity = main.getDouble("humidity");
            double pressure = main.getDouble("pressure");
            double wind = windJSON.getDouble("speed");
            String iconValue = weatherObject.getString("icon");
            double temperature = main.getDouble("temp");
            double feelsLike = main.getDouble("feels_like");
            String cloud = weatherObject.getString("description");
            String townString = forecast.getString("name");
            long dateLong = forecast.getLong("dt");

            list.add(dateLong);
            list.add(townString);
            list.add(humidity);
            list.add(pressure);
            list.add(wind);
            list.add(iconValue);
            list.add(temperature);
            list.add(feelsLike);
            list.add(cloud);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;

    }


    /**
     * This part converts from the various primitive parts to a string array
     * @param list  this is the input paramter of the method
     * @return  stringList is the returned value of the method
     */
    private ArrayList<String> convertAndSavePrimitiveToString(ArrayList<Object> list){

        ArrayList<String> stringList = new ArrayList<>();

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        String dateString = convertTimeStampToDay((long)list.get(0));
        String stringTown = (String)list.get(1);
        String humidityString = NumberFormat.getPercentInstance().format((double)list.get(2) / 100.0);
        String pressureString = numberFormat.format((double)list.get(3));
        String windString = numberFormat.format((double)list.get(4));
        String iconString = "http://openweathermap.org/img/w/" + list.get(5) + ".png";
        String temperatureString = numberFormat.format((double)list.get(6)) + "\u00B0C";
        String feelsLikeString = numberFormat.format((double)list.get(7)) + "\u00B0C";
        String cloudString = (String)list.get(8);

        stringList.add(dateString);
        stringList.add(stringTown);
        stringList.add(humidityString);
        stringList.add(pressureString);
        stringList.add(windString);
        stringList.add(iconString);
        stringList.add(temperatureString);
        stringList.add(feelsLikeString);
        stringList.add(cloudString);

            TheReceiptBookDatabaseHelper helper = new TheReceiptBookDatabaseHelper(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.delete("WEATHER",null,null);
            helper.insertWeather(db,dateString,stringTown,humidityString,windString,
                    encodedImage,temperatureString,feelsLikeString,cloudString,pressureString);

        return stringList;
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
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
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

