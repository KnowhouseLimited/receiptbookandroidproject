//Author: Enoch Viewu
//Cooperation: KnowHouse
//Date Modified: 4th Feb, 2020
//Code: The SplashScreen of the application

package com.knowhouse.thereceiptbook;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.thereceiptbook.LoginSingleton.SharedPrefManager;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;


public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkOnlineDatabase();
    }

    private void checkOnlineDatabase(){
        final RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SPLASH_SCREEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class); //This is an intent to launch MainActivity

                            int id = SharedPrefManager.getInstance(getApplicationContext()).getUserID();
                            int phoneNumber = SharedPrefManager.getInstance(getApplicationContext()).getUserPhoneNumber();
                            String fullName = SharedPrefManager.getInstance(getApplicationContext()).getUserFullName();
                            String companyName = SharedPrefManager.getInstance(getApplicationContext()).getUserCompany();
                            String image = SharedPrefManager.getInstance(getApplicationContext()).getUserImage();

                            intent.putExtra(MainActivity.USERID,id);
                            intent.putExtra(MainActivity.PHONE_NUMBER,phoneNumber);
                            intent.putExtra(MainActivity.FULL_NAME,fullName);
                            intent.putExtra(MainActivity.COMPANY_NAME,companyName);
                            intent.putExtra(MainActivity.IMAGE_URL,image);

                            startActivity(intent);  //Start intent to launch MainActivity
                            finish();   //remove the splash screen from stack
                            requestQueue.stop();
                        }else{
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();   //remove the splash screen from stack
                            requestQueue.stop();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(),"User is offline",Toast.LENGTH_LONG);
                toast.show();
                if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                    Intent intent =new Intent(getApplicationContext(),MainActivity.class);

                    int id = SharedPrefManager.getInstance(getApplicationContext()).getUserID();
                    int phoneNumber = SharedPrefManager.getInstance(getApplicationContext()).getUserPhoneNumber();
                    String fullName = SharedPrefManager.getInstance(getApplicationContext()).getUserFullName();
                    String companyName = SharedPrefManager.getInstance(getApplicationContext()).getUserCompany();
                    String image = SharedPrefManager.getInstance(getApplicationContext()).getUserImage();

                    intent.putExtra(MainActivity.USERID,id);
                    intent.putExtra(MainActivity.PHONE_NUMBER,phoneNumber);
                    intent.putExtra(MainActivity.FULL_NAME,fullName);
                    intent.putExtra(MainActivity.COMPANY_NAME,companyName);
                    intent.putExtra(MainActivity.IMAGE_URL,image);

                    startActivity(intent);  //Start intent to launch MainActivity
                    finish();   //remove the splash screen from stack
                    requestQueue.stop();
                }else{
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();   //remove the splash screen from stack
                    requestQueue.stop();
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
