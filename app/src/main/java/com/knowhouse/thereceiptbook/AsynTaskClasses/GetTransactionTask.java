package com.knowhouse.thereceiptbook.AsynTaskClasses;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.knowhouse.thereceiptbook.Adapters.ActivityFeedAdapter;
import com.knowhouse.thereceiptbook.ImageConverter;
import com.knowhouse.thereceiptbook.SQLiteDatabaseClasses.TheReceiptBookDatabaseHelper;
import com.knowhouse.thereceiptbook.UtitlityClasses.TransactionsClass;

import java.util.ArrayList;

public class GetTransactionTask extends AsyncTask<Object,Void, Object[]> {

    private Context context;
    private RecyclerView recyclerView;
    private Activity activity;


    @Override
    protected Object[] doInBackground(Object... objects) {
        this.context = (Context)objects[0];
        this.activity = (Activity)objects[1];
        this.recyclerView = (RecyclerView)objects[2];

        return retrieveTransactions(context);
    }




    @Override
    protected void onPostExecute(Object[] list) {
        ArrayList<String> date = (ArrayList<String>) list[0];
        ArrayList<String> application = (ArrayList<String>)list[1];
        ArrayList<String> image = (ArrayList<String>)list[2];
        ArrayList<Integer> phoneNumber = (ArrayList<Integer>)list[3];
        ArrayList<String> fullName = (ArrayList<String>)list[4];
        ArrayList<Integer> identity = (ArrayList<Integer>)list[5];

        String[] dates = new String[date.size()];
        String[] applications = new String[application.size()];
        String[] images = new String[image.size()];
        int[] phoneNumbers = new int[phoneNumber.size()];
        String[] fullNames = new String[fullName.size()];
        int[] identities = new int[identity.size()];

        for(int i=0;i<date.size();i++){
            dates[i] = date.get(i);
            applications[i] = application.get(i);
            images[i] = image.get(i);
            phoneNumbers[i] = phoneNumber.get(i);
            fullNames[i] = fullName.get(i);
            identities[i] = identity.get(i);
        }

        String full_name = fullNames[0];
        String phone_number = String.valueOf(phoneNumbers[0]);
        String image_string = images[0];

        Bitmap bitmap = ImageConverter.convertFromStringToImg(image_string);
        TransactionsClass transactionsClass = new TransactionsClass(identities,full_name,phone_number,
                applications,dates,bitmap);
        ActivityFeedAdapter adapter = new ActivityFeedAdapter(transactionsClass);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);


    }

    private Object[] retrieveTransactions(Context context){
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> applicationTexts = new ArrayList<>();
        ArrayList<Integer> phoneNumbers = new ArrayList<>();
        ArrayList<String> image = new ArrayList<>();
        ArrayList<String> fullName = new ArrayList<>();
        ArrayList<Integer> identity = new ArrayList<>();
        TheReceiptBookDatabaseHelper helper = new TheReceiptBookDatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("TRANSACTIONSFEED",
                new String[]{"date","application","image","phone_number","full_name","identity"},
                null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                dates.add(cursor.getString(0));
                applicationTexts.add(cursor.getString(1));
                image.add(cursor.getString(2));
                phoneNumbers.add(cursor.getInt(3));
                fullName.add(cursor.getString(4));
                identity.add(cursor.getInt(5));
            }while (cursor.moveToNext());
        }

        return new Object[]{dates,applicationTexts,image,phoneNumbers,
                                            fullName,identity};

    }
}
