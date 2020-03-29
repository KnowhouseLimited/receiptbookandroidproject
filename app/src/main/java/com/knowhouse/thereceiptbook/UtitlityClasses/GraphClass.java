package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.data.BarEntry;
import com.knowhouse.thereceiptbook.Adapters.GraphFeedAdapter;
import com.knowhouse.thereceiptbook.Constants;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphClass {

    private String phoneNumber;
    private String date;
    private Context context;
    private RecyclerView graphRecyclerView;

    public GraphClass(String phoneNumber, String date, RecyclerView graphRecyclerView,Context context){
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.context = context;
        this.graphRecyclerView = graphRecyclerView;
    }

    //Function to add bar entry values to the graph
    public void retrieveGraphValues(){

        final RequestQueue requestQueue = MySingleton.getInstance(context).getRequestQueue();
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_GRAPH_DATA,
                response -> {

                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        int max = jsonArray.length();
                        JSONObject objects;
                        String[] item = new String[max];
                        float[] entry = new float[max];
                        for (int i=0; i<jsonArray.length();i++){
                            objects = jsonArray.getJSONObject(i);
                            if(!objects.getBoolean("error")){
                                item[i] = objects.getString("item");
                                entry[i] = (float)objects.getInt("entry");
                            }
                        }
                        GraphFeedAdapter graphFeedAdapter = new GraphFeedAdapter(item,entry);
                        graphRecyclerView.setAdapter(graphFeedAdapter);

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

    public static float maximumYData(ArrayList<BarEntry> barEntry){
        float[] listOfValues = new float[barEntry.size()];
        int i = 0;
        float test = 0.0f;
        float maximum = 0.0f;
        for(BarEntry a : barEntry){
            listOfValues[i] = a.getVal();
            if(listOfValues[i] > test)
                maximum = listOfValues[i];
            ++i;
        }
        return maximum;
    }

}
