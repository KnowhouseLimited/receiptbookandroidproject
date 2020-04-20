/*
 * Created on: 13th April, 2020 9:39pm
 */

package com.knowhouse.thereceiptbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
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
import java.util.Objects;
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
        signUp.setOnClickListener(v -> {

            //userIsLoggedIn();
            if (!Objects.requireNonNull(phoneNumber.getText()).toString().isEmpty() && phoneNumber.getText().toString().length() == 10 ||
                    !phoneNumber.getText().toString().isEmpty() && phoneNumber.getText().toString().length() == 9 ) {

                if(Objects.requireNonNull(userPassword.getText()).toString().trim()
                        .equals(Objects.requireNonNull(confirmUserPassword.getText()).
                                toString().trim())){

                    String phoneNum = "+" + mCountryCode + phoneNumber.getText().toString();
                    Log.d("TAG", "onCLick: Phone number is " + phoneNum);
                    startPhoneNumberVerification(phoneNum);

                }else{
                    Toast.makeText(RegisterActivity.this, "Passwords don't match",  //Text to display if passwords
                            Toast.LENGTH_SHORT).show();                                         //don't match.
                }
            }
            else{
                phoneNumber.setError("Phone number is not valid");
            }

        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //signInWithPhoneAuthCredential(phoneAuthCredential);
                Log.i("OnVerificationCompleted","Verification is completed");
                    createUser();
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(RegisterActivity.this,"can not create an account"+e.getMessage(),Toast.LENGTH_LONG).show();
            }


            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.i("OnCodeSent","Called OnCodeSent");
                    mVerificationId = verificationId;
                    showEditDialog();
            }

        };

    }


    /*Method to run when the sign up button is pressed*/
    private void createUser() {

        progressDialog.setMessage("Registering User ...");  //Progress dialog message to display to the user
        progressDialog.show();                              //display the progress dialog to user

        //Save user input in string
        final String userFullName = Objects.requireNonNull(fullName.getText()).toString().trim();   //get text from fullName layout and remove spaces
        final String userCompanyName = Objects.requireNonNull(companyName.getText()).toString().trim(); //get text from companyName layout and remove spaces
        final String userPhoneNumber = Objects.requireNonNull(phoneNumber.getText()).toString().trim(); //get text from phoneNumber layout and remove spaces
        final String userPasswordValue = Objects.requireNonNull(userPassword.getText()).toString().trim();  //get text from userPassword layout and remove spaces
        final String userConfirmedPassword = Objects.requireNonNull(confirmUserPassword.getText()).toString().trim();   //get text from confirmUserPassword layout and remove spaces

        final RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).     //get instance to MySingleton Class for requests
                getRequestQueue();
        requestQueue.start();      //start the request queue for the volley database
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                response -> {   //Code responsible for the responses from the database
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
                }, error -> {
                    progressDialog.dismiss();   //dismiss the progress dialog from the screen
                    Toast.makeText(getApplicationContext(),"Unable to connect",Toast.LENGTH_LONG).show();    //display message to the user
                    error.printStackTrace();    //print error to android studio editor
                    requestQueue.stop();    //stop the request queue
                }) {
            @Override
            protected Map<String, String> getParams() {
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



    private void startPhoneNumberVerification(String phoneNum) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private void showEditDialog(){
        FragmentManager fm = getSupportFragmentManager();
        VerificationDialogFragment verificationDialogFragment = VerificationDialogFragment.newInstance("Verification Dialog");
        verificationDialogFragment.show(fm,"fragment_verification");
    }

    @Override
    public void onFinishedVerificationDialog(String inputText) {
        mCode = inputText;
    }

}
