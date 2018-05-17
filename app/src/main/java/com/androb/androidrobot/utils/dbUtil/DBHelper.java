package com.androb.androidrobot.utils.dbUtil;

import android.content.Context;
import android.content.SharedPreferences;

import com.androb.androidrobot.models.User;
import com.androb.androidrobot.utils.URLs;
import com.androb.androidrobot.utils.userUtil.UserManager;
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
 * Created by kaki on 2018/05/14.
 */

public class DBHelper {

    private static Context mContext;
    private String username;
    private String password;
    private String type;
    private User user;

    String idResult;


    public DBHelper(Context context) {
        mContext = context;
        SharedPreferences sp = mContext.getSharedPreferences("sharedUserPref", Context.MODE_PRIVATE);
        username = sp.getString("username", null);
        user = UserManager.getInstance(mContext).getUser();
    }


    public String checkQuestionStatus(String mtype, final VolleyCallback callback) {
        this.type = mtype;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
//                            System.out.println("response: " + response);
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                idResult = obj.getString("id");
                                System.out.println("DBHelper correct: " + idResult);
                                callback.onSuccess(idResult);
                            }
                            else {
                                System.out.println("DBHelper error");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("apicall", "quesFromXMode");
                params.put("username", username);
                params.put("type", type);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

        System.out.println("in dbHelper, before return: " + idResult);
        return idResult;
    }

}
