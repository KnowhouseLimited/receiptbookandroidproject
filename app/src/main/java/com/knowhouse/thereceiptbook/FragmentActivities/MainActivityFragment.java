//Author: Enoch Viewu
//Cooperation: KnowHouse
//Date Modified: 4th Feb, 2020
//Code: The MainActivityFragment of the application

package com.knowhouse.thereceiptbook.FragmentActivities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowhouse.thereceiptbook.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
