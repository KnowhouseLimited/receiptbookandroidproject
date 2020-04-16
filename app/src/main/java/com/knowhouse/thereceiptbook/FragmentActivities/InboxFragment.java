package com.knowhouse.thereceiptbook.FragmentActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.knowhouse.thereceiptbook.AsynTaskClasses.GetInboxTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.GetTransactionTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SaveInboxTask;
import com.knowhouse.thereceiptbook.AsynTaskClasses.SaveTransactionTask;
import com.knowhouse.thereceiptbook.R;
import com.onesignal.OneSignal;
import com.knowhouse.thereceiptbook.Chat.ChatListAdapter;
import com.knowhouse.thereceiptbook.Chat.ChatObject;
import com.knowhouse.thereceiptbook.User.UserObject;

import java.util.ArrayList;

public class InboxFragment extends Fragment {

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;
    private RecyclerView recyclerView;

    ArrayList<ChatObject> chatList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView =  (RecyclerView) inflater.inflate(R.layout.fragment_inbox,
                container,false);

        OneSignal.startInit(getContext()).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
        // new SendNotification("testing notification","heading",null);

        Fresco.initialize(getContext());

        if(isNetworkAvailable()){
            SaveInboxTask saveInboxTask = new SaveInboxTask();
            saveInboxTask.loadUserChatList();
        }else{
            //GetInboxTask getInboxTask = new GetInboxTask();
            //getInboxTask.execute(getContext(),getActivity(),recyclerView);

        }
        //return recyclerView;


        //getPermissions();
        //initializeRecyclerView(recyclerView);

        return  recyclerView;
    }



    private void getChatData(String chatId) {
        DatabaseReference mChatDB = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("info");
        mChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String chatId = "";

                    if(dataSnapshot.child("id").getValue() != null)
                        chatId = dataSnapshot.child("id").getValue().toString();

                    for(DataSnapshot userSnapshot : dataSnapshot.child("users").getChildren()){
                        for(ChatObject mChat : chatList){
                            if(mChat.getChatId().equals(chatId)){
                                UserObject mUser = new UserObject(userSnapshot.getKey());
                                mChat.addUserToArrayList(mUser);
                                getUserData(mUser);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUserData(UserObject mUser) {
        DatabaseReference mUserDb = FirebaseDatabase.getInstance().getReference().child("user").child(mUser.getUid());
        mUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserObject mUser = new UserObject(dataSnapshot.getKey());

                if(dataSnapshot.child("notificationKey").getValue() != null)
                    mUser.setNotificationKey(dataSnapshot.child("notificationKey").getValue().toString());

                for(ChatObject mChat : chatList){
                    for (UserObject mUserIt : mChat.getUserObjectArrayList()){
                        if(mUserIt.getUid().equals(mUser.getUid())){
                            mUserIt.setNotificationKey(mUser.getNotificationKey());
                        }
                    }
                }
                mChatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerView(RecyclerView mChatList) {
        chatList = new ArrayList<>();
        //mChatList= recyclerView.findViewById(R.id.chatList);
        mChatList.setNestedScrollingEnabled(false);
        mChatList.setHasFixedSize(false);
        mChatListLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mChatList.setLayoutManager(mChatListLayoutManager);
        mChatListAdapter = new ChatListAdapter(chatList);
        mChatList.setAdapter(mChatListAdapter);
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE); //TODO: Check requireNonNull
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
