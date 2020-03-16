package com.knowhouse.thereceiptbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.thereceiptbook.LoginSingleton.SharedPrefManager;
import com.knowhouse.thereceiptbook.VolleyClasses.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    private ImageView profileImageIcon;
    private TextInputEditText fullNameEditText;
    private TextInputEditText phoneNumberEditText;
    private TextInputEditText companyNameEditText;
    private Button editButton;
    private Button saveButton;

    private ProgressDialog progressDialog;

    public static final String USERID = "id";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String FULL_NAME = "fullName";
    public static final String COMPANY_NAME = "companyName";
    public static final String IMAGE_URL = "image";

    private String fullName;
    private int phoneNumber;
    private String companyName;
    private int userid;
    private String userimageid;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImageIcon = findViewById(R.id.profileImageSetup);


        //get a reference to the TextInputEditText view
        fullNameEditText = findViewById(R.id.full_name_edittext);
        phoneNumberEditText = findViewById(R.id.user_phone_number_edittext);
        companyNameEditText = findViewById(R.id.company_name_edittext);

        //get reference to the button view
        editButton = findViewById(R.id.edit_button);
        saveButton = findViewById(R.id.save_button);

        //create a progress dialog object
        progressDialog = new ProgressDialog(this);

        intent = getIntent();
        userid = intent.getExtras().getInt(USERID);
        fullName = intent.getExtras().getString(FULL_NAME).trim();
        phoneNumber = intent.getExtras().getInt(PHONE_NUMBER);
        String phoneNumberString = "0".concat(String.valueOf(phoneNumber));
        companyName = intent.getExtras().getString(COMPANY_NAME).trim();

        //fullNameDisplay.setText(fullName);
        fullNameEditText.setText(fullName);
        phoneNumberEditText.setText(phoneNumberString);
        companyNameEditText.setText(companyName);


        String imageUrl = intent.getExtras().getString(IMAGE_URL).trim();
        if(imageUrl.equals("null")){
            profileImageIcon.setImageDrawable(getDrawable(R.drawable.people));
            profileImageIcon.setEnabled(false);
        }else{
            Bitmap bitmap = ImageConverter.convertFromStringToImg(imageUrl);
            profileImageIcon.setImageBitmap(bitmap);
            profileImageIcon.setEnabled(false);
        }

        editButton.setOnClickListener(this);
        profileImageIcon.setOnClickListener(this);
        saveButton.setOnClickListener(this);

    }

    //check for the various on click listeners
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profileImageSetup:
                selectImage();
                break;

            case R.id.save_button:
                if(fullNameEditText.isEnabled() || phoneNumberEditText.isEnabled()
                        || companyNameEditText.isEnabled()){
                    saveUserInfo();
                }else{
                    Toast.makeText(this,"Information Already Saved",
                            Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.edit_button:
                fullNameEditText.setEnabled(true);
                phoneNumberEditText.setEnabled(true);
                companyNameEditText.setEnabled(true);
                profileImageIcon.setEnabled(true);
                break;
        }
    }

    //Select image for the user profile image
    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.
                        getBitmap(getContentResolver(),path);
                profileImageIcon.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //Create a string request for the volley
    private void saveUserInfo(){
        progressDialog.setMessage("Saving ...");
        progressDialog.show();

        //get text in fields
        final String userPhoneNumber = phoneNumberEditText.getText().toString().trim();
        final String userFullName = fullNameEditText.getText().toString().trim();
        final String useridentity = String.valueOf(userid);
        final String userCompany = companyNameEditText.getText().toString().trim();

        final RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).
                getRequestQueue();
        requestQueue.start();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USERPROFILE_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject obj  = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),obj.getString("message"),
                                        Toast.LENGTH_LONG).show();

                                //Logout
                                finish();
                                SharedPrefManager.getInstance(getApplicationContext()).logout();
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),error.getMessage(),
                        Toast.LENGTH_LONG).show();
                requestQueue.stop();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userid",useridentity);
                params.put("full_name",userFullName);
                params.put("phone_number",userPhoneNumber);
                params.put("image_url",userFullName+userPhoneNumber);
                params.put("image",imageToString(bitmap));
                params.put("company",userCompany);
                return  params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

}
