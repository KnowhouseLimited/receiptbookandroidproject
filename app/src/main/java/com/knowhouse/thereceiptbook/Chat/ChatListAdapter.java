package com.knowhouse.thereceiptbook.Chat;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knowhouse.thereceiptbook.ChatActivity;
import com.knowhouse.thereceiptbook.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends
        RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    ArrayList<ChatObject> chatList;

    public ChatListAdapter(ArrayList<ChatObject> chatList){

        this.chatList = chatList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        //Define the view to be used for each data item

        private CardView cardView;

        public ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView layoutView = (CardView)LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_fragment_feed, null, false);
        //RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutView.setLayoutParams(lp);

        //ChatListViewHolder rcv = new ChatListViewHolder(layoutView);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.mTitle.setText(chatList.get(position).getChatId());
        //holder.mTitle.setText("chatList.get(position).getChatId()");

        //holder.mLayout.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), ChatActivity.class);
                //intent.putExtra("chatObject", chatList.get(holder.getAdapterPosition()));
                //v.getContext().startActivity(intent);
            //}
        //});
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }





    public class ChatListViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public LinearLayout mLayout;
        public ChatListViewHolder(View view){
            super(view);
            mTitle = view.findViewById(R.id.title);
            mLayout = view.findViewById(R.id.layout);
        }
    }
}
