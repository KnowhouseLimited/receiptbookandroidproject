package com.knowhouse.thereceiptbook.Adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.model.Chat;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InboxFeedAdapter extends
        RecyclerView.Adapter<InboxFeedAdapter.ViewHolder> {

    private ArrayList<Chat> chats;

    static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    public InboxFeedAdapter(ArrayList<Chat> chats){
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_fragment_feed,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        Chat chat = chats.get(position);

        TextView user_fullname = cardView.findViewById(R.id.user_full_name);
        TextView app_textview = cardView.findViewById(R.id.info_text);
        TextView user_phone_number = cardView.findViewById(R.id.user_phone_number);
        TextView time_of_issue = cardView.findViewById(R.id.time_of_issue);
        CircleImageView user_images = cardView.findViewById(R.id.profile_image);

        app_textview.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
