package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.knowhouse.thereceiptbook.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetWeatherTask
        extends AsyncTask<Object,Void, JSONObject> {

    public AsyncResponse delegate = null;


    @Override
    protected JSONObject doInBackground(Object... obj) {
        HttpURLConnection connection = null;
        URL url = (URL)obj[0];
        View view = (View)obj[1];

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
        delegate.processFinish(jsonObject);
    }

}

