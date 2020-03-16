package com.knowhouse.thereceiptbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.thereceiptbook.LoginSingleton.SharedPrefManager;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //Declaration of variables from the text inputs
    private TextInputEditText phoneNumber;      //phone number field in layout
    private TextInputEditText password;         //password field in layout
    private ProgressDialog progressDialog;      //progress dialog variable declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){       //Check to see if user is already logged in
            finish();               //if so remove the previous activity from stack
            startActivity(new Intent(this,MainActivity.class)); //launch the main activity class' layout
        }

        //Initializing the widgets
        phoneNumber = findViewById(R.id.phone_number);  //get a reference to the phoneNumber layout
        password = findViewById(R.id.password);         //get a reference to the password layout
        //login button from layout
        progressDialog = new ProgressDialog(this);  //create a progress dialog object

        Button loginButton = findViewById(R.id.login_button);  //get a reference to the login button layout

        //set an on click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();        //This is the method that will handle database queries for the login
            }
        });
    }


    private void userLogin(){
        progressDialog.setMessage("Loggin In User ..."); //progress dialog message to display after click login button
        progressDialog.show();  //display progress dialog to user

        final String userPhoneNumber = phoneNumber.getText().toString().trim(); //get the phoneNumber and remove spaces before/after it
        final String userPassword = password.getText().toString().trim();   //get the password and remove spaces before/after it


        /*Creating a string request for the database queries*/
        final RequestQueue requestQueue = MySingleton.getInstance(this).
                getRequestQueue();
        requestQueue.start();   //start the request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //to handle responses
                        progressDialog.dismiss();
                        try{
                            JSONObject obj = new JSONObject(response);      //JSon object to get object information from database
                            if(!obj.getBoolean("error")){               //if for JSon object error in database, the message is not true this code will run
                                SharedPrefManager.getInstance(getApplicationContext())  //The following variables will be taken and passed to the userLogin method
                                        .userLogin(                                     //in SharedPrefManager class
                                                obj.getInt("id"),   //      //get the id of the user who just logged in
                                                obj.getInt("phone_number"), //get the phone number of the user who just logged in
                                                obj.getString("full_name"), //get the full name of the user who just logged in
                                                obj.getString("company"),   //get the company name of the user who just logged in
                                                obj.getString("image")       //get the image of the user who just logged in
                                        );
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG)
                                        .show();

                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);  //Create an intent to launch MainActivity class

                                int id = SharedPrefManager.getInstance(getApplicationContext()).getUserID();    //get user id from SharedPrefManager class
                                int phoneNumber = SharedPrefManager.getInstance(getApplicationContext()).getUserPhoneNumber();  //get phoneNumber from SharedPrefManager class
                                String fullName = SharedPrefManager.getInstance(getApplicationContext()).getUserFullName(); ////get fullName from SharedPrefManager class
                                String companyName = SharedPrefManager.getInstance(getApplicationContext()).getUserCompany();   //get companyName from SharedPrefManager class
                                String image = SharedPrefManager.getInstance(getApplicationContext()).getUserImage();   //get user image from SharedPrefManager class

                                intent.putExtra(MainActivity.USERID,id);    //pass the user id as extra information to the intent
                                intent.putExtra(MainActivity.PHONE_NUMBER,phoneNumber); //pass the phoneNumber as extra information to the intent
                                intent.putExtra(MainActivity.FULL_NAME,fullName);       //pass the fullName as extra information to the intent
                                intent.putExtra(MainActivity.COMPANY_NAME,companyName); //pass the companyName as extra information to the intent
                                intent.putExtra(MainActivity.IMAGE_URL,image);  //pass the user image as extra information to the intent
                                startActivity(intent);  //start the MainActivity class
                                finish();   //Clear LoginActivity from stack
                            }else{  //if there is an JSon object error the error is true then this code will run
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();    //print the error in android studio editor
                        }
                        requestQueue.stop();    //stop request queue after query
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //to handle errors
                progressDialog.dismiss();   //Dismiss progress dialog
                Toast.makeText(getApplicationContext(),
                        "Unable to connect",
                        Toast.LENGTH_LONG).show();  //print unable to connect to user
                error.printStackTrace();    //print the error in android studio editor
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();   //Create a hash map of key and value
                params.put("phone_number", userPhoneNumber);    //send the userPhoneNumber to database key phone_number
                params.put("confirm_password", userPassword);   //send the userPassword to database key confirm_password
                return params;                                  //return the params hash map to volley
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    //Method to handle registration text
    public void launchRegistration(View v){
        Intent intent = new Intent(this,RegisterActivity.class );
        startActivity(intent);
        finish();
    }
}
