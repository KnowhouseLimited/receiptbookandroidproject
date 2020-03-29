//Author: Enoch Viewu
//Cooperation: KnowHouse
//Date Modified: 4th Feb, 2020
//Code: The MainActivityFragment of the application

package com.knowhouse.thereceiptbook.FragmentActivities;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.UtitlityClasses.DataClass;
import com.knowhouse.thereceiptbook.UtitlityClasses.GraphClass;
import com.knowhouse.thereceiptbook.UtitlityClasses.WeatherClass;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NestedScrollView view = (NestedScrollView) inflater.inflate(R.layout.fragment_main,
                container, false);
        //weather recycler details
        recyclerRun(view);
        return view;

    }

    private void recyclerRun(View view) {


        RecyclerView weatherRecyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView graphRecyclerView = view.findViewById(R.id.recycler_view1);
        RecyclerView dataFeedRecyclerView = view.findViewById(R.id.recycler_view2);


        weatherRecyclerView.setHasFixedSize(true);
        weatherRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        weatherRecyclerView.setLayoutManager(layoutManager);


        RecyclerView.LayoutManager secondLayout = new LinearLayoutManager(getContext());
        graphRecyclerView.setHasFixedSize(true);
        graphRecyclerView.setItemAnimator(new DefaultItemAnimator());
        graphRecyclerView.setLayoutManager(secondLayout);

        RecyclerView.LayoutManager thirdLayout = new LinearLayoutManager(getContext());
        graphRecyclerView.setHasFixedSize(true);
        dataFeedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dataFeedRecyclerView.setLayoutManager(thirdLayout);


        /*
         * Recycler view for the graph section
         */

        WeatherClass weatherClass = new WeatherClass("Sunyani", weatherRecyclerView,view,getContext());
        DataClass dataClass = new DataClass("0548409523","2020-03-29",getContext(),
                dataFeedRecyclerView);
        GraphClass graphClass = new GraphClass("0548409523","2020-03-29", graphRecyclerView,
                                    getContext());

        weatherClass.retrieveWeatherData();
        dataClass.retrieveDataFeed();
        graphClass.retrieveGraphValues();


    }
}
