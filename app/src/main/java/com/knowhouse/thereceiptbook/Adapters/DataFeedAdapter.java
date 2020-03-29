package com.knowhouse.thereceiptbook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knowhouse.thereceiptbook.R;


public class DataFeedAdapter
        extends RecyclerView.Adapter<DataFeedAdapter.ViewHolder> {

    private String issuerCompanyName;   //variable to hold the issuer of receipts company name
    private String noOfReceiptIssuedPerDay;   //variable to hold the number of receipts issued in a day
    private String totalOfItemsSoldPerDay;    //variable to hold the number of items sold in a day
    private String totalAmountMadePerDay;      //variable to hold the number of items gotten in a day
    private String itemWithHighestReceiptNumberPerDay;   //variable to hold the number of highest receipt issued item
    private Context context;        //get the context from the parent fragment

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    public DataFeedAdapter(String issuerCompanyName,String noOfReceiptIssuedPerDay,String totalOfItemsSoldPerDay,
                           String totalAmountMadePerDay,String itemWithHighestReceiptNumberPerDay,Context context){
        this.issuerCompanyName = issuerCompanyName;
        this.itemWithHighestReceiptNumberPerDay =itemWithHighestReceiptNumberPerDay;
        this.noOfReceiptIssuedPerDay = noOfReceiptIssuedPerDay;
        this.totalAmountMadePerDay = totalAmountMadePerDay;
        this.totalOfItemsSoldPerDay = totalOfItemsSoldPerDay;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardView cv = (CardView)LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.datafeed,viewGroup,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CardView cardView = viewHolder.cardView;
        TextView user_company = cardView.findViewById(R.id.issuer_company_name);
        TextView number_of_receipt = cardView.findViewById(R.id.tNumberOfReceipt);
        TextView total_amount_made = cardView.findViewById(R.id.totalAmountMade);
        TextView highestReceiptIssuedItem = cardView.findViewById(R.id.highest_receipt_issued_item);
        TextView totalItemSold = cardView.findViewById(R.id.numberOfItemsSold);

        user_company.setText(context.getString(R.string.company_giant_inc,issuerCompanyName));
        number_of_receipt.setText(context.getString(R.string.total_number_of_receipt_issued_,noOfReceiptIssuedPerDay));
        total_amount_made.setText(context.getString(R.string.total_amount_made,totalAmountMadePerDay));
        highestReceiptIssuedItem.setText(context.getString(R.string.item_with_most_receipt,itemWithHighestReceiptNumberPerDay));
        totalItemSold.setText(context.getString(R.string.total_number_of_items_sold,totalOfItemsSoldPerDay));
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
