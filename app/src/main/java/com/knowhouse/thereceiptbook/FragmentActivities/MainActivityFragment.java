//Author: Enoch Viewu
//Cooperation: KnowHouse
//Date Modified: 1st April, 2020
//Code: The MainActivityFragment of the application

package com.knowhouse.thereceiptbook.FragmentActivities;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowhouse.thereceiptbook.LoginSingleton.SharedPrefManager;
import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.UtitlityClasses.DataClass;
import com.knowhouse.thereceiptbook.UtitlityClasses.GraphClass;
import com.knowhouse.thereceiptbook.UtitlityClasses.WeatherClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NestedScrollView view = (NestedScrollView) inflater.inflate(R.layout.fragment_main,
                container, false);

        int phoneNumber = SharedPrefManager.getInstance(getContext()).getUserPhoneNumber();
        String phone_number = String.valueOf(phoneNumber);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(c);

        //Create weather card view
        new WeatherClass("Sunyani", view,getContext());
        new DataClass(phone_number,currentDate,
                view,getContext());
        new GraphClass(phone_number,currentDate,
                view,getContext());

        return view;
    }

}
