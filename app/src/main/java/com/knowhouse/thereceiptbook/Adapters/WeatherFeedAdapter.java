package com.knowhouse.thereceiptbook.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.knowhouse.thereceiptbook.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class WeatherFeedAdapter extends
        RecyclerView.Adapter<WeatherFeedAdapter.ViewHolder> {

    //Declaration of the data for the weatherFeedAdapter
    private String date;
    private String town;
    private String humidity;
    private String pressure;
    private String wind;
    private String icon;
    private String temperature;
    private String feelsLike;
    private String cloud;
    private Context context;
    private Map<String,Bitmap> bitmaps = new HashMap<>();

    static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        //Define the view to be used for each data item

        ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    //Constructor for the WeatherFeedAdapter
    public WeatherFeedAdapter(long date,String town,double humidity,double pressure,
                              double wind,String icon,double temperature,double feelsLike,
                              String cloud,Context context){

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        this.date = convertTimeStampToDay(date);
        this.town = town;
        this.humidity = NumberFormat.getPercentInstance().format(humidity/100.0);
        this.pressure = numberFormat.format(pressure);
        this.wind = numberFormat.format(wind);
        this.icon = "http://openweathermap.org/img/w/"+icon+".png";
        this.temperature = numberFormat.format(temperature) + "\u00B0C";
        this.feelsLike = numberFormat.format(feelsLike)+"\u00B0C";
        this.cloud = cloud;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cv = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.weatherfeed,viewGroup,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CardView cardView = viewHolder.cardView;
        TextView user_date = cardView.findViewById(R.id.weatherDate);
        TextView user_town = cardView.findViewById(R.id.weatherTown);
        TextView user_humidity = cardView.findViewById(R.id.weatherHumidity);
        TextView user_pressure = cardView.findViewById(R.id.weatherPressure);
        TextView user_wind = cardView.findViewById(R.id.weatherWind);
        ImageView user_icon = cardView.findViewById(R.id.weatherIcon);
        TextView user_temperature = cardView.findViewById(R.id.weatherTemp);
        TextView user_feelsLike = cardView.findViewById(R.id.weatherFeelsLike);
        TextView user_cloud = cardView.findViewById(R.id.weatherCloud);


        user_town.setText(town);
        user_humidity.setText(context.getString((R.string.humidity_57),String.valueOf(humidity)));
        user_pressure.setText(context.getString((R.string.pressure_1015_hpa),String.valueOf(pressure)));
        user_wind.setText(context.getString((R.string.wind_14_km_h_sse),String.valueOf(wind)));
        user_temperature.setText(String.valueOf(temperature));
        user_feelsLike.setText(context.getString((R.string.feels_like),String.valueOf(feelsLike)));
        user_cloud.setText(cloud);
        user_date.setText(date);

        if(bitmaps.containsKey(icon)){
            user_icon.setImageBitmap(bitmaps.get(icon));
        }else{
            LoadImageTask loadImageTask = new LoadImageTask(user_icon);
            loadImageTask.execute(icon);
        }


    }

    @Override
    public int getItemCount() {

        return 1;
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
        LoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        //load image; params[0] is the String URL representing the image
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try{
                URL url = new URL(params[0]);   //create URL for image

                //open an HTTPURLConnection, get its InputStream
                //and download the image
                connection = (HttpURLConnection) url.openConnection();

                try(InputStream inputStream = connection.getInputStream()){
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(params[0],bitmap);  //cache for later use
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
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
