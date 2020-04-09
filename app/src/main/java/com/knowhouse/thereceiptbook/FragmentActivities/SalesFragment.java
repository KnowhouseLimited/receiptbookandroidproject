package com.knowhouse.thereceiptbook.FragmentActivities;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.data.Entry;
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

        new PieChartClass("0548409523",pieChartView,
                                                    getContext());
        return pieChartView;
    }
}
