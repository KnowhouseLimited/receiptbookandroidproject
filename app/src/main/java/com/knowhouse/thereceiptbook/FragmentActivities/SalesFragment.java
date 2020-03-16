package com.knowhouse.thereceiptbook.FragmentActivities;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.knowhouse.thereceiptbook.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesFragment extends Fragment {


    private PieChart pieChart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pieChartView = inflater.inflate(R.layout.fragment_sales,
                container, false);


        pieChart = pieChartView.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(34f,"DataA"));
        yValues.add(new PieEntry(23f,"DataB"));
        yValues.add(new PieEntry(14f,"DataC"));
        yValues.add(new PieEntry(35f,"DataD"));
        yValues.add(new PieEntry(40f,"DataE"));
        yValues.add(new PieEntry(23f,"DataF"));

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues,"Statistics");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);


        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);

        return pieChartView;
    }

}
