package com.knowhouse.thereceiptbook.FragmentActivities;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowhouse.thereceiptbook.AsynTaskClasses.GetTransactionTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SaveTransactionTask;
import com.knowhouse.thereceiptbook.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {


    //public TransactionsClass transactionsClass;
    //public ActivityFeedAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView activityFeedRecycler = (RecyclerView) inflater.inflate(
                R.layout.fragment_transactions, container, false);

        if(isNetworkAvailable()){
            SaveTransactionTask saveTransactionTask = new SaveTransactionTask(getContext(),getActivity());
            saveTransactionTask.loadTransactions(activityFeedRecycler);
        }else{
            GetTransactionTask getTransactionTask = new GetTransactionTask();
            getTransactionTask.execute(getContext(),getActivity(),activityFeedRecycler);

        }
        return activityFeedRecycler;
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
