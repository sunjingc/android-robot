package com.androb.androidrobot.utils.questionUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.androb.androidrobot.utils.dbUtil.VolleySingleton;
import com.androb.androidrobot.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaki on 2018/05/11.
 */

public class QuestionStatusManager {

    //the constants
    private static final String TAG = "QuestionStatusManager";

    private static final String SHARED_PREF_NAME = "sharedUserPref";
    private static final String KEY_APICALL = "apicall";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_QUESID = "quesId";
    private static final String KEY_TYPE = "type";

    private static QuestionStatusManager mInstance;
    private static Context mCtx;

    private String uname = "";
    private int quesId = 0;
    private boolean status = false;
    private String quesType;

    private QuestionStatusManager(Context context) {
        mCtx = context;
    }

    public static synchronized QuestionStatusManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new QuestionStatusManager(context);
        }
        return mInstance;
    }

    public boolean checkQuestionStatus(final String type, int qid) {
        quesId = qid;
        quesType = type;

        // 用username和quesId两个param查询，看看结果是否>0
        SharedPreferences sp = mCtx.getSharedPreferences("sharedUserPref", Context.MODE_PRIVATE);
        uname = sp.getString(KEY_USERNAME, null);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            System.out.println("response: " + response);
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                status = obj.getBoolean("status");
                                System.out.println("record for ques: " + obj.getString("status"));
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
                params.put(KEY_APICALL, "quesCheck");
                params.put(KEY_USERNAME, uname);
                params.put(KEY_QUESID, String.valueOf(quesId));
                params.put(KEY_TYPE, quesType);
                return params;
            }
        };

        VolleySingleton.getInstance(mCtx).addToRequestQueue(stringRequest);

        System.out.println("in QuesStatMan, before return: " + status);

        return status;
    }


    public void updateStatus(String type, int qid) {
        Log.d(TAG, "UpdateStatus");

        SharedPreferences sp = mCtx.getSharedPreferences("sharedUserPref", Context.MODE_PRIVATE);
        uname = sp.getString(KEY_USERNAME, null);
        quesId = qid;
        quesType = type;

        StringRequest stringRequest = stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DRAG_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("in updateStatus, respo: " + response);
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
                params.put(KEY_APICALL, "quesUpdate");
                params.put(KEY_USERNAME, uname);
                params.put(KEY_QUESID, String.valueOf(quesId));
                params.put(KEY_TYPE, quesType);
                return params;
            }
        };

        VolleySingleton.getInstance(mCtx).addToRequestQueue(stringRequest);
        Log.d(TAG, "in UpdateScore: after VolleySingleton");
    }
}
