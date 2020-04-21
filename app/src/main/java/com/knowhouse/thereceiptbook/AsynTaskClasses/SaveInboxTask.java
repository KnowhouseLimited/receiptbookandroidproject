package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.knowhouse.thereceiptbook.Adapters.InboxFeedAdapter;
import com.knowhouse.thereceiptbook.model.Chat;

import java.util.ArrayList;

public class SaveInboxTask {

    private ArrayList<Chat> mList;
    private Context context;

    public SaveInboxTask(Context context,RecyclerView recyclerView){
        this.context = context;
        loadUserChatList(recyclerView);
    }


    private void loadUserChatList(RecyclerView recyclerView){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("chat");
        mList = new ArrayList<>();
        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    assert mUser != null;
                    Log.i("ChatIssue","Check Log");
                    if(mUser.getUid().equals(chat.getReceiver())){
                        mList.add(chat);
                    }
                }

                loadRecyclerView(recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadRecyclerView(RecyclerView recyclerView){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        InboxFeedAdapter inboxFeedAdapter = new InboxFeedAdapter(mList);
        recyclerView.setAdapter(inboxFeedAdapter);
    }

}
