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
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button signUp;      //Sign up button variable
    private TextInputEditText fullName; //TextInput edit for full name variable
    private TextInputEditText companyName;  //TextInput edit for company name variable
    private TextInputEditText phoneNumber;  //TextInput edit for phone number variable
    private TextInputEditText userPassword; //TextInput edit for user password variable
    private TextInputEditText confirmUserPassword;  //TextInput edit for confirm password variable
    private ProgressDialog progressDialog;  //Progress dialog variable
    private final String result = "Already Registered"; //To display if user is already available

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize the various fields
        fullName = findViewById(R.id.full_name);    //reference to full name
        companyName = findViewById(R.id.company_name);  //reference to company name
        phoneNumber = findViewById(R.id.phone_number);  //reference to phone number
        userPassword = findViewById(R.id.password); //reference to user password
        confirmUserPassword = findViewById(R.id.confirm_password);  //reference to confirm password
        progressDialog = new ProgressDialog(this);  //reference to progress dialog
        signUp = findViewById(R.id.sign_up_button); //reference to sign up

        //Create an onClick Listener for button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userPassword.getText().toString().trim()
                        .equals(confirmUserPassword.getText().
                                toString().trim())){
                    createUser();
                }else{
                    Toast.makeText(RegisterActivity.this, "Passwords don't match",  //Text to display if passwords
                            Toast.LENGTH_SHORT).show();                                         //don't match.
                }
            }
        });
    }


    /*Method to run when the sign up button is pressed*/
    private void createUser() {

        progressDialog.setMessage("Registering User ...");  //Progress dialog message to display to the user
        progressDialog.show();                              //display the progress dialog to user

        //Save user input in string
        final String userFullName = fullName.getText().toString().trim();   //get text from fullName layout and remove spaces
        final String userCompanyName = companyName.getText().toString().trim(); //get text from companyName layout and remove spaces
        final String userPhoneNumber = phoneNumber.getText().toString().trim(); //get text from phoneNumber layout and remove spaces
        final String userPasswordValue = userPassword.getText().toString().trim();  //get text from userPassword layout and remove spaces
        final String userConfirmedPassword = confirmUserPassword.getText().toString().trim();   //get text from confirmUserPassword layout and remove spaces

        final RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).     //get instance to MySingleton Class for requests
                getRequestQueue();
        requestQueue.start();      //start the request queue for the volley database
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Code responsible for the responses from the database
                        progressDialog.dismiss();   //dismiss the progress dialog from the screen
                        try{
                            JSONObject object = new JSONObject(response);   //get the objects using the JSON object from database
                            if(!object.getBoolean("error")){            //if the response for error is false the code below will run
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);  //Create an intent to launch the login activity class
                                startActivity(intent);  //start the login activity class
                                finish();       //remove register activity from stack
                            }else{  //if response for error is true then the code below will run
                                Toast.makeText(getApplicationContext(),
                                        "Please Check Your Credentials",Toast.LENGTH_LONG).show();  //this message will display to the user
                            }
                        }catch (JSONException e){   //if an exception is caught code below will run
                            e.printStackTrace();    //error will be printed in the android studio's editor
                        }
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();   //dismiss the progress dialog from the screen
                Toast.makeText(getApplicationContext(),"Unable to connect",Toast.LENGTH_LONG).show();    //display message to the user
                error.printStackTrace();    //print error to android studio editor
                requestQueue.stop();    //stop the request queue
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();   //Create a hash map with key and value
                params.put("full_name", userFullName);  //save the userFullName value in database with key full_name
                params.put("company", userCompanyName); //save the userCompanyName value in database with key company
                params.put("phone_number", userPhoneNumber);    //save the userPhoneNumber value in database with key phone_number
                params.put("user_pass", userPasswordValue); //save the userPasswordValue value in database with key user_pass
                params.put("confirm_password", userConfirmedPassword);  //save the userConfirmedPassword value in database with key confirm_password
                return params;  //return the params value to volley database
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);  //add the request queue to the instance MySingleton's request queue
    }
}
