package com.knowhouse.thereceiptbook.FragmentActivities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SaveInboxTask;
import com.knowhouse.thereceiptbook.R;
import com.onesignal.OneSignal;

public class InboxFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseUser fUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView =  (RecyclerView) inflater.inflate(R.layout.fragment_inbox,
                container,false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        OneSignal.startInit(getContext()).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference().child("user").child(fUser.getUid()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
        // new SendNotification("testing notification","heading",null);

        Fresco.initialize(requireContext());

        if(isNetworkAvailable()){
            new SaveInboxTask(getContext(),recyclerView);
        }else{
            //GetInboxTask getInboxTask = new GetInboxTask();
            //getInboxTask.execute(getContext(),getActivity(),recyclerView);

        }

        return  recyclerView;
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE); //TODO: Check requireNonNull
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
