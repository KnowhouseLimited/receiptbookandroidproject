/*
 * Created on: 13th April, 2020 9:39pm
 */

package com.knowhouse.thereceiptbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.knowhouse.thereceiptbook.VerficationDialogFragments.VerificationDialogFragment;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity implements VerificationDialogFragment.VerificationDialogListener {

    private Button signUp;      //Sign up button variable
    private TextInputEditText fullName; //TextInput edit for full name variable
    private TextInputEditText companyName;  //TextInput edit for company name variable
    private TextInputEditText phoneNumber;  //TextInput edit for phone number variable
    private TextInputEditText userPassword; //TextInput edit for user password variable
    private TextInputEditText confirmUserPassword;  //TextInput edit for confirm password variable
    private ProgressDialog progressDialog;  //Progress dialog variable

    private final String result = "Already Registered"; //To display if user is already available
    private String mCountryCode = "233";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean verifyClick;
    private boolean opt;
    private String mVerificationId;
    private String mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);


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
                    if(isAuthenticated()){
                        showEditDialog();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Passwords don't match",  //Text to display if passwords
                            Toast.LENGTH_SHORT).show();                                         //don't match.
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }


            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(RegisterActivity.this,"can not create an account"+e.getMessage(),Toast.LENGTH_LONG).show();
                verifyClick = false;
            }


            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mVerificationId = verificationId;
                progressDialog.setMessage("Verify Code");
            }
        };

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
                            if(!object.getBoolean("error")){//if the response for error is false the code below will run
                                    //startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
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

    public boolean isAuthenticated(){


        //userIsLoggedIn();
        if (!phoneNumber.getText().toString().isEmpty() && phoneNumber.getText().toString().length() == 10 ||
                !phoneNumber.getText().toString().isEmpty() && phoneNumber.getText().toString().length() == 9 ) {

            String phoneNum = "+" + mCountryCode + phoneNumber.getText().toString();
            Log.d("TAG", "onCLick: Phone number is " + phoneNum);

            return startPhoneNumberVerification(phoneNum);

        }
        else{
            phoneNumber.setError("Phone number is not valid");
        }
        return false;
    }

    private void verifyPhoneNumberWithCode(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mCode);
        signInWithPhoneAuthCredential(credential);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user != null){
                        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()){
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("phone", user.getPhoneNumber());
                                    userMap.put("name", user.getPhoneNumber());
                                    mUserDB.updateChildren(userMap);
                                }
                                //userIsLoggedIn();
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });
                    }

                }

            }
        });
    }


   /* private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            //startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            finish();
        }
    }*/

    private boolean startPhoneNumberVerification(String phoneNum) {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                60L,
                TimeUnit.SECONDS,
                this,
                mCallbacks);

        return true;

    }

    private void showEditDialog(){
        FragmentManager fm = getSupportFragmentManager();
        VerificationDialogFragment verificationDialogFragment = VerificationDialogFragment.newInstance("Verification Dialog");
        verificationDialogFragment.show(fm,"fragment_verification");
    }

    @Override
    public void onFinishedVerificationDialog(String inputText) {
        mCode = inputText;
        if (mVerificationId != null){
            verifyPhoneNumberWithCode();
            createUser();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("OnStart","This starts the activity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("OnPause","This pause the activity");
    }
}
