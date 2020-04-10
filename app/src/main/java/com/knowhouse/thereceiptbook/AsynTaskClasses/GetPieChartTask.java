package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.SQLiteDatabaseClasses.TheReceiptBookDatabaseHelper;

import java.util.ArrayList;

public class GetPieChartTask extends AsyncTask<Object,Void,Object[]> {

    @Override
    protected Object[] doInBackground(Object... objects) {

        View view = (View)objects[0];
        Context context = (Context)objects[1];
        return retrievePieChartValues(view,context);
    }

    @Override
    protected void onPostExecute(Object[] list) {
        loadPieChartValues(list);
    }

    //Function to add bar entry values to the graph
    private Object[] retrievePieChartValues(View pieChartView, Context context){
        ArrayList<String> item = new ArrayList<>();
        ArrayList<Float> entry = new ArrayList<>();
        TheReceiptBookDatabaseHelper helper = new TheReceiptBookDatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("PIECHARTFEED",
                new String[]{"item","entry"},
                null,null,null,null,
                null);
        if(cursor.moveToFirst()){
            do{
                item.add(cursor.getString(0));
                entry.add(cursor.getFloat(1));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        helper.close();

        return new Object[]{item,entry,pieChartView,context};
    }

    private void loadPieChartValues(Object[] list){

        ArrayList<String> item = (ArrayList<String>)list[0];
        ArrayList<Float> entry = (ArrayList<Float>)list[1];
        View pieChartView = (View)list[2];

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

        for(int j=0 ;j<item.size();j++){
            entries.add(new BarEntry(entry.get(j),j));
            PieEntryLabels.add(item.get(j));
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
        data.setValueFormatter((value, entry1, dataSetIndex, viewPortHandler) -> String.valueOf((int)Math.floor(value)));
        pieChart.setData(data);

    }
}
