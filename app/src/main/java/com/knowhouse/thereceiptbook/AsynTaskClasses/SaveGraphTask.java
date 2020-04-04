package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.knowhouse.thereceiptbook.Constants;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.SQLiteDatabaseClasses.TheReceiptBookDatabaseHelper;
import com.knowhouse.thereceiptbook.UtitlityClasses.GraphClass;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SaveGraphTask {

    private Context context;
    private View view;
    private String phoneNumber;
    private String date;

    public SaveGraphTask(View view, Context context, String phoneNumber,
                            String date){
        this.context = context;
        this.view = view;
        this.phoneNumber = phoneNumber;
        this.date = date;
    }


    //Function to add bar entry values to the graph
    public void retrieveGraphValues(){

        final RequestQueue requestQueue = MySingleton.getInstance(context).getRequestQueue();
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_GRAPH_DATA,
                response -> {

                    ArrayList<String > item = new ArrayList<>();
                    ArrayList<Float> entry = new ArrayList<>();
                    TheReceiptBookDatabaseHelper helper = new TheReceiptBookDatabaseHelper(context);
                    SQLiteDatabase db =helper.getWritableDatabase();
                    db.delete("GRAPHFEED",
                            "date=?",new String[]{date});
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject objects;
                        for (int i=0; i<jsonArray.length();i++){
                            objects = jsonArray.getJSONObject(i);
                            if(!objects.getBoolean("error")){
                                item.add(objects.getString("item"));
                                entry.add((float)objects.getInt("entry"));
                                helper.insertGraphFeed(db,objects.getString("item"),
                                        (float)objects.getInt("entry"),date);
                            }
                        }
                        db.close();
                        GetGraphDataTask getGraphDataTask = new GetGraphDataTask();
                        getGraphDataTask.execute(view,context,date);
                    }catch (JSONException e){
                        e.printStackTrace();
                        requestQueue.stop();
                    }

                },error -> {
            Toast.makeText(context,"Please Check internet connection",Toast.LENGTH_LONG).show();
            requestQueue.stop();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("phone_number", phoneNumber);
                params.put("date", date);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
