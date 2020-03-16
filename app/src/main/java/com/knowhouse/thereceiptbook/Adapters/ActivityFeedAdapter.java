package com.knowhouse.thereceiptbook.Adapters;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.TransactionsClass;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityFeedAdapter extends
        RecyclerView.Adapter<ActivityFeedAdapter.ViewHolder> {

    private String full_name;
    private String userPhoneNumber;
    private String[] date;
    private Bitmap user_image;
    private String[] app_text;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //Define the view to be used for each data item

        private CardView cardView;

        public ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    public ActivityFeedAdapter(TransactionsClass transactionsClass){
        this.full_name = transactionsClass.getFullName();
        this.userPhoneNumber = transactionsClass.getPhoneNumber();
        this.date = transactionsClass.getDate();
        this.user_image = transactionsClass.getImage();
        this.app_text = transactionsClass.getTransactions();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cv = (CardView)LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activityfeed,viewGroup,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CardView cardView = viewHolder.cardView;
        TextView user_fullname = cardView.findViewById(R.id.user_full_name);
        TextView app_textview = cardView.findViewById(R.id.info_text);
        TextView user_phone_number = cardView.findViewById(R.id.user_phone_number);
        TextView time_of_issue = cardView.findViewById(R.id.time_of_issue);
        CircleImageView user_images = cardView.findViewById(R.id.profile_image);

        //append 0 to the phone number
        String phoneNumberString = "0"+ String.valueOf(userPhoneNumber);
        //set views
        user_fullname.setText(full_name);
        user_phone_number.setText(phoneNumberString);
        app_textview.setText(app_text[i]);
        time_of_issue.setText(date[i]);

        if(user_image != null){
            user_images.setImageBitmap(user_image);
        }

    }

    @Override
    public int getItemCount() {
        return app_text.length;
    }

}
