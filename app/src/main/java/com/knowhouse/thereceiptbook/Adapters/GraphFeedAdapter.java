package com.knowhouse.thereceiptbook.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.UtitlityClasses.GraphClass;

import java.util.ArrayList;


public class GraphFeedAdapter extends
        RecyclerView.Adapter<GraphFeedAdapter.ViewHolder> {


    private String[] item;
    private float[] entry;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        //Define the view to be used for each data view
        ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    public GraphFeedAdapter(String[] item,float[] entry){
        this.item = item;
        this.entry = entry;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardView cv = (CardView)LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.graphfeed,viewGroup,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CardView cardView = viewHolder.cardView;
        BarChart chart = cardView.findViewById(R.id.chart1);

        ArrayList<BarEntry> barEntry = new ArrayList<>();
        ArrayList<String> barEntryLabels = new ArrayList<>();
        for(int j=0 ;j<item.length;j++){
            barEntry.add(new BarEntry(entry[j],j));
            barEntryLabels.add(item[j]);
            chart.notifyDataSetChanged();
            chart.invalidate();
        }

        BarDataSet barDataSet = new BarDataSet(barEntry, "");
        BarData barData = new BarData(barEntryLabels, barDataSet);
        barData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> String.valueOf((int)Math.floor(value)));
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        YAxis yLAxis = chart.getAxisLeft();
        yLAxis.setAxisMaxValue(GraphClass.maximumYData(barEntry) + 10f);
        yLAxis.setAxisMinValue(0f);

        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setAxisMaxValue(GraphClass.maximumYData(barEntry) + 10f);
        yRAxis.setAxisMinValue(0f);

        chart.getAxisRight().setDrawLabels(false);
        chart.animateY(3000);
        chart.getXAxis().setSpaceBetweenLabels(0);
        chart.setData(barData);
    }

    @Override
    public int getItemCount() {
        return 1;
    }


}
