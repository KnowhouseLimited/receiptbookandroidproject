package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.SQLiteDatabaseClasses.TheReceiptBookDatabaseHelper;

import java.util.ArrayList;

public class GetGraphDataTask extends AsyncTask<Object,Void, Object[]> {


    private Context context;
    private View view;
    private String date;

    @Override
    protected Object[] doInBackground(Object... objects) {

        this.view = (View)objects[0];
        this.context = (Context)objects[1];
        this.date = (String)objects[2];
        return retrieveGraphValues();
    }

    @Override
    protected void onPostExecute(Object[] list) {
        populateCardView(list);
    }

    //Function to add bar entry values to the graph
    private Object[] retrieveGraphValues(){

        ArrayList<String> item = new ArrayList<>();
        ArrayList<Float> entry = new ArrayList<>();
        TheReceiptBookDatabaseHelper helper = new TheReceiptBookDatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("GRAPHFEED",
                new String[]{"item","entry"},
                "date=?",new String[]{date},null,null,
                null);

        if(cursor.moveToFirst()){

            do{
                item.add(cursor.getString(0));
                entry.add(cursor.getFloat(1));
            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return new Object[]{item,entry};
    }

    private void populateCardView(Object[] list){


        ArrayList<String> item = (ArrayList<String>) list[0];
        ArrayList<Float> entry = (ArrayList<Float>) list[1];

        BarChart chart = view.findViewById(R.id.chart1);

        ArrayList<BarEntry> barEntry = new ArrayList<>();
        ArrayList<String> barEntryLabels = new ArrayList<>();
        for(int j=0 ;j<item.size();j++){
            barEntry.add(new BarEntry(entry.get(j),j));
            barEntryLabels.add(item.get(j));
            chart.notifyDataSetChanged();
            chart.invalidate();
        }

        BarDataSet barDataSet = new BarDataSet(barEntry, "");
        BarData barData = new BarData(barEntryLabels, barDataSet);
        barData.setValueFormatter((value, entry1, dataSetIndex, viewPortHandler) -> String.valueOf((int)Math.floor(value)));
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        YAxis yLAxis = chart.getAxisLeft();
        yLAxis.setAxisMaxValue(maximumYData(barEntry) + 10f);
        yLAxis.setAxisMinValue(0f);

        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setAxisMaxValue(maximumYData(barEntry) + 10f);
        yRAxis.setAxisMinValue(0f);

        chart.getAxisRight().setDrawLabels(false);
        chart.animateY(3000);
        chart.getXAxis().setSpaceBetweenLabels(0);
        chart.setData(barData);
    }


    private float maximumYData(ArrayList<BarEntry> barEntry){
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
