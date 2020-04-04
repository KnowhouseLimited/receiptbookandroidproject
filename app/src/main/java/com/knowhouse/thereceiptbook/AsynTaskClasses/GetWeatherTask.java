package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.SQLiteDatabaseClasses.TheReceiptBookDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetWeatherTask extends AsyncTask<Object,Void,ArrayList<String>> {

    private View view;
    private Context context;


    @Override
    protected ArrayList<String> doInBackground(Object... objects) {
        view = (View)objects[0];
        context = (Context)objects[1];

        return retrieveData();
    }


    @Override
    protected void onPostExecute(ArrayList<String> list) {

        TextView date = view.findViewById(R.id.weatherDate);
        TextView town = view.findViewById(R.id.weatherTown);
        TextView humidity = view.findViewById(R.id.weatherHumidity);
        TextView pressure = view.findViewById(R.id.weatherPressure);
        TextView wind = view.findViewById(R.id.weatherWind);
        ImageView icon = view.findViewById(R.id.weatherIcon);
        TextView temperature = view.findViewById(R.id.weatherTemp);
        TextView feelsLike = view.findViewById(R.id.weatherFeelsLike);
        TextView cloud = view.findViewById(R.id.weatherCloud);

        Bitmap bitmap = null;

        try {
            byte[] encodeByte = Base64.decode(list.get(5), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        }catch (Exception e){
            e.printStackTrace();
        }

        date.setText(list.get(0));
        town.setText(list.get(1));
        humidity.setText(context.getString((R.string.humidity_57),list.get(2)));
        pressure.setText(context.getString((R.string.pressure_1015_hpa),list.get(3)));
        wind.setText(context.getString((R.string.wind_14_km_h_sse),list.get(4)));
        icon.setImageBitmap(bitmap);
        temperature.setText(list.get(6));
        feelsLike.setText(context.getString((R.string.feels_like),list.get(7)));
        cloud.setText(list.get(8));

    }

    private ArrayList<String> retrieveData() {

        ArrayList<String> list =  new ArrayList<>();;

        try{

            TheReceiptBookDatabaseHelper helper = new TheReceiptBookDatabaseHelper(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.query("WEATHER",
                    new String[]{"date","town","humidity","pressure","wind",
                            "icon","temperature","feelslike","cloud"},
                    null,null,
                    null,null,null);
            if(cursor.moveToFirst()){
                String dateString = cursor.getString(0);
                String stringTown = cursor.getString(1);
                String humidityString = cursor.getString(2);
                String pressureString = cursor.getString(3);
                String windString = cursor.getString(4);
                String iconString = cursor.getString(5);
                String temperatureString = cursor.getString(6);
                String feelsLikeString = cursor.getString(7);
                String cloudString = cursor.getString(8);

                list.add(dateString);
                list.add(stringTown);
                list.add(humidityString);
                list.add(pressureString);
                list.add(windString);
                list.add(iconString);
                list.add(temperatureString);
                list.add(feelsLikeString);
                list.add(cloudString);
                db.close();
                cursor.close();
                return list;
            }

        }catch (SQLiteException e){
            e.printStackTrace();
        }

        return null;
    }


}
