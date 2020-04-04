package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.knowhouse.thereceiptbook.R;
import com.knowhouse.thereceiptbook.SQLiteDatabaseClasses.TheReceiptBookDatabaseHelper;

import java.util.ArrayList;

public class GetDataFeedTask extends AsyncTask<Object,Void, ArrayList<String>> {

    private View view;
    private Context context;

    @Override
    protected ArrayList<String> doInBackground(Object... objects) {
        this.view = (View)objects[0];
        this.context = (Context)objects[1];
        String date = (String) objects[2];
        return retrieveData(context, date);
    }

    @Override
    protected void onPostExecute(ArrayList<String> listStrings) {
        TextView user_company = view.findViewById(R.id.issuer_company_name);
        TextView number_of_receipt = view.findViewById(R.id.tNumberOfReceipt);
        TextView total_amount_made = view.findViewById(R.id.totalAmountMade);
        TextView highestReceiptIssuedItem = view.findViewById(R.id.highest_receipt_issued_item);
        TextView totalItemSold = view.findViewById(R.id.numberOfItemsSold);

        user_company.setText(context.getString(R.string.company_giant_inc,listStrings.get(0)));
        number_of_receipt.setText(context.getString(R.string.total_number_of_receipt_issued_,listStrings.get(1)));
        total_amount_made.setText(context.getString(R.string.total_amount_made,listStrings.get(2)));
        highestReceiptIssuedItem.setText(context.getString(R.string.item_with_most_receipt,listStrings.get(3)));
        totalItemSold.setText(context.getString(R.string.total_number_of_items_sold,listStrings.get(4)));
    }

    private ArrayList<String> retrieveData(Context context,String date){

        ArrayList<String> list = new ArrayList<>();
        TheReceiptBookDatabaseHelper helper = new TheReceiptBookDatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("DATAFEED",
                new String[]{"issuer_company","no_of_receipt_issued_per_day",
                "total_amount_made_per_day","item_with_most_receipt",
                        "total_of_items_sold_per_day"},
                "date = ?",new String[]{date},null,null,null);

        if(cursor.moveToFirst()){
            list.add(cursor.getString(0));      //issuer company data
            list.add(cursor.getString(1));   //No of receipt issued per day data
            list.add(cursor.getString(2)); //total amount made per day
            list.add(cursor.getString(3));   //items with most receipt
            list.add(cursor.getString(4));    //total of items sold per day
        }

        return list;
    }
}
