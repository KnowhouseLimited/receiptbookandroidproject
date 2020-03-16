package com.knowhouse.thereceiptbook.LoginSingleton;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String SHARE_PREF_NAME = "mysharedpref12";
    private static final String KEY_PHONENUMBER = "phonenumber";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_ID = "userid";
    private static final String KEY_COMPANY = "company";
    private static final String KEY_USERIMAGE = "image";
    private SharedPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(int id,int phoneNumber,String fullname,String companyName,String imageUrl){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID,id);
        editor.putInt(KEY_PHONENUMBER,phoneNumber);
        editor.putString(KEY_FULLNAME,fullname);
        editor.putString(KEY_COMPANY,companyName);
        editor.putString(KEY_USERIMAGE,imageUrl);

        editor.apply();
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences.getInt(KEY_PHONENUMBER,0)!=0){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public int getUserPhoneNumber(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_PHONENUMBER,0);
    }

    public String getUserFullName(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FULLNAME,null);
    }

    public int getUserID(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID,0);
    }

    public String getUserCompany(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_COMPANY,null);
    }

    public String getUserImage(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERIMAGE,null);
    }
}
