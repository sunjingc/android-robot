package com.androb.androidrobot.userManagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.androb.androidrobot.CollegeStuMainActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kaki on 2018/05/08.
 */

public class SharedUserManager {

    //the constants
    private static final String TAG = "SharedUserManager";

    private static final String SHARED_PREF_NAME = "sharedPref";
    private static final String KEY_APICALL = "apicall";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SCORE = "score";

    private static SharedUserManager mInstance;
    private static Context mCtx;

    private String uname = "";
    private int oriScore = 0;
    private int newScore = 0;

    private SharedUserManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedUserManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedUserManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_SCORE, user.getScore() + "");
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_SCORE, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, StartActivity.class));
    }

    public void updateScore() {
        Log.d(TAG, "UpdateScore");

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        uname = this.getUser().getUsername();
        oriScore = Integer.valueOf(sharedPreferences.getString(KEY_SCORE, ""));
        System.out.println("oriScore: " + oriScore);
        newScore = oriScore + 10;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("respo: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_APICALL, "update");
                params.put(KEY_USERNAME, uname);
                params.put(KEY_SCORE, String.valueOf(newScore));
                return params;
            }
        };

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SCORE, newScore + "");
        // commit changes
        editor.commit();

        VolleySingleton.getInstance(mCtx).addToRequestQueue(stringRequest);
        Log.d(TAG, "in UpdateScore: after VolleySingleton");
    }
}