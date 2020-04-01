//Author: Enoch Viewu
//Cooperation: KnowHouse
//Date Modified: 4th Feb, 2020
//Code: The MainActivityFragment of the application

package com.knowhouse.thereceiptbook.FragmentActivities;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
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
public class MainActivityFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NestedScrollView view = (NestedScrollView) inflater.inflate(R.layout.fragment_main,
                container, false);

        //Create weather card view
        new WeatherClass("Sunyani", view,getContext());
        new DataClass("0548409523","2020-03-28",
                view,getContext());
        new GraphClass("0548409523","2020-03-28",
                view,getContext());

        return view;

    }

}
