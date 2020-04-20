package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.knowhouse.thereceiptbook.ReceiptPageActivity;
import com.knowhouse.thereceiptbook.model.Chat;

import java.util.ArrayList;

public class SaveInboxTask {

    private ArrayList<Chat> mList;

    public SaveInboxTask(){

        loadUserChatList();
    }


    private void loadUserChatList(){
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
