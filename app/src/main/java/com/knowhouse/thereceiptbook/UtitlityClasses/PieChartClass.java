package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.knowhouse.thereceiptbook.Constants;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PieChartClass {

    private String phoneNumber;
    private Context context;
    private View pieChartView;
    private String[] item;
    private float[] entry;


    public PieChartClass(String phoneNumber, View pieChartView,
                         Context context){
        this.phoneNumber = phoneNumber;
        this.context = context;
        this.pieChartView = pieChartView;
    }

    //Function to add bar entry values to the graph
    public void retrievePieChartValues(){

        final RequestQueue requestQueue = MySingleton.getInstance(context).getRequestQueue();
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_PIE_CHART_DATA,
                response -> {

                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        int max = jsonArray.length();
                        JSONObject objects;
                        item = new String[max];
                        entry = new float[max];
                        for (int i=0; i<jsonArray.length();i++){
                            objects = jsonArray.getJSONObject(i);
                            if(!objects.getBoolean("error")){
                                item[i] = objects.getString("item");
                                entry[i] = (float)objects.getInt("entry");
                            }
                        }
                        loadPieChartValues();

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
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void loadPieChartValues(){

        PieChart pieChart = pieChartView.findViewById(R.id.piechart);
        //pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(60f);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);


        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> PieEntryLabels = new ArrayList<>();

        for(int j=0 ;j<item.length;j++){
            entries.add(new BarEntry(entry[j],j));
            PieEntryLabels.add(item[j]);
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(PieEntryLabels, dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> String.valueOf((int)Math.floor(value)));
        pieChart.setData(data);

    }
}
