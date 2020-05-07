package com.knowhouse.thereceiptbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.knowhouse.thereceiptbook.LoginSingleton.SharedPrefManager;
import com.knowhouse.thereceiptbook.Utils.SendNotification;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;
import com.knowhouse.thereceiptbook.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReceiptPageActivity extends AppCompatActivity {


    //Declaration of widgets
    private TextInputEditText customerName;
    private TextInputEditText customerPhoneNumber;
    private TextInputEditText itemPurchased;
    private TextInputEditText amountPaid;
    private ProgressDialog progressDialog;
    //private boolean isSent;
    private FirebaseUser firebaseUser;
    private String userid;
    private Bitmap bitmap;
    private String image;
    private LoggedInUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_page);

        //Definition of widgets
        customerName = findViewById(R.id.customer_full_name);
        customerPhoneNumber = findViewById(R.id.customer_phone_number);
        itemPurchased = findViewById(R.id.item_purchased);
        amountPaid = findViewById(R.id.amount_paid);

        //Progress Dialog definition
        progressDialog = new ProgressDialog(this);


        //Definition of button for issuance of receipt
        Button issueButton = findViewById(R.id.issue_button);

        //Set onClick Listener for the issue button
        issueButton.setOnClickListener(v -> issueTheReceipt());
    }


    private void issueTheReceipt(){
        progressDialog.setMessage("Sending receipt...");
        progressDialog.show();



        final String customer_name = Objects.requireNonNull(customerName.getText()).toString().trim();
        final String item_purchased = Objects.requireNonNull(itemPurchased.getText()).toString().trim();
        final String customer_phone_number = Objects.requireNonNull(customerPhoneNumber.getText()).toString().trim();
        final String amount_paid = Objects.requireNonNull(amountPaid.getText()).toString().trim();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
        StringBuilder myPhoneNum = new StringBuilder(customer_phone_number);
        myPhoneNum.setCharAt(0,'3');
        String internationalPhoneNumber = "+23"+myPhoneNum.toString();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    Log.i("Chatobject_info","Log issue");
                    loggedInUser = dataSnapshot1.getValue(LoggedInUser.class);
                    assert loggedInUser != null;
                    if(!loggedInUser.getPhone().equals(firebaseUser.getPhoneNumber())){
                        if(loggedInUser.getPhone().equals(internationalPhoneNumber)){
                            userid = dataSnapshot1.getKey();
                        }

                        if(!SharedPrefManager.getInstance(getApplicationContext()).getUserImage().equals("null")){
                            image = SharedPrefManager.getInstance(getApplicationContext()).getUserImage();
                        }else{
                            image = "default";
                        }
                    }
                }

                String companyName = SharedPrefManager.getInstance(getApplicationContext()).getUserCompany();
                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = df.format(c);
                String messageToSend = "This receipt is being issued to you from "
                        +SharedPrefManager.getInstance(getApplicationContext()).getUserFullName()+
                        " for the purchase of "+item_purchased+" which cost an amount of GHS "+ amount_paid;


                sendMessage(firebaseUser.getUid(),userid,messageToSend,image,companyName,
                        currentDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });



        final int id = SharedPrefManager.getInstance(getApplicationContext()).getUserID();
        final int phoneNumber = SharedPrefManager.getInstance(getApplicationContext()).getUserPhoneNumber();
        final String fullName = SharedPrefManager.getInstance(getApplicationContext()).getUserFullName();
        final String companyName = SharedPrefManager.getInstance(getApplicationContext()).getUserCompany();
        final String image_url = SharedPrefManager.getInstance(getApplicationContext()).getUserImage();

        final RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        requestQueue.start();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RECEIPT_ISSUE,
                response -> {
                    progressDialog.dismiss();
                    try{
                        JSONObject object = new JSONObject(response);
                        if(!object.getBoolean("error")){
                            Intent intent = new Intent(ReceiptPageActivity.this,MainActivity.class);

                            intent.putExtra(MainActivity.USERID,id);
                            intent.putExtra(MainActivity.PHONE_NUMBER,phoneNumber);
                            intent.putExtra(MainActivity.FULL_NAME,fullName);
                            intent.putExtra(MainActivity.COMPANY_NAME,companyName);
                            intent.putExtra(MainActivity.IMAGE_URL,image_url);
                            Toast.makeText(getApplicationContext(),"Receipt issued",Toast.LENGTH_LONG).show();
                            //isSent = true;
                            intent.putExtra(MainActivity.RECEIPTISSUED, true);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),object.getString("message"),
                                    Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    requestQueue.stop();
                }, error -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(),
                            Toast.LENGTH_LONG).show();
                    requestQueue.stop();
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer_full_name",customer_name);
                params.put("customers_phone_number",customer_phone_number);
                params.put("purchased_item",item_purchased);
                params.put("amount_paid",amount_paid);
                params.put("receipt_issued_by",String.valueOf(phoneNumber));
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void sendMessage(String sender,String receiver,String message,String image,
                             String senderCompany,String time){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("image",image);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("senderCompany",senderCompany);
        hashMap.put("time",time);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(receiver.equals(snapshot.getKey())){
                        LoggedInUser receiverDetails = snapshot.getValue(LoggedInUser.class);
                        String messageTrimmed = message.substring(0,20);
                        assert receiverDetails != null;
                        new SendNotification(messageTrimmed,"Receipt from "+senderCompany,receiverDetails.getNotificationKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("chat").push().setValue(hashMap);
    }

}
