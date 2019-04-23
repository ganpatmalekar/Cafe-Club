package com.example.swapnil.coffeeshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.swapnil.coffeeshop.activities.HomeActivity;
import com.example.swapnil.coffeeshop.activities.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String IS_LOGIN = "IS_LOGIN";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    //public static final String EMAIL = "EMAIL";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    //create login session
    public void createSession(String name, String id){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(ID, id);
        //editor.putString(EMAIL, email);
        editor.commit();
    }

    //get login state
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLoggedIn()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((HomeActivity)context).finish();
        }
    }

    //get stored session data
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        //user.put(EMAIL, sharedPreferences.getString(EMAIL, null));

        return user;
    }

    //clear session details
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((HomeActivity)context).finish();
    }
}
