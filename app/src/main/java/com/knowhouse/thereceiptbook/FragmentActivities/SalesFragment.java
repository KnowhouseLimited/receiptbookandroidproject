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
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.UtitlityClasses.PieChartClass;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesFragment extends Fragment {


    private ArrayList<Entry> entries;
    private ArrayList<String> PieEntryLabels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pieChartView = inflater.inflate(R.layout.fragment_sales,
                container, false);

        PieChartClass pieChartClass = new PieChartClass("0548409523",pieChartView,
                                                    getContext());
        pieChartClass.retrievePieChartValues();
        return pieChartView;
    }
}
