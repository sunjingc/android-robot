package com.androb.androidrobot.codeMode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androb.androidrobot.R;
import com.androb.androidrobot.utils.dbUtil.DBHelper;
import com.androb.androidrobot.utils.dbUtil.VolleyCallback;
import com.androb.androidrobot.utils.userUtil.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/28.
 */

public class CodeModeStartActivity extends AppCompatActivity implements OnClickListener {

    @BindView(R.id.code_ques_1)
    Button _codeQues1;
    @BindView(R.id.code_ques_2)
    Button _codeQues2;
    @BindView(R.id.code_ques_3)
    Button _codeQues3;
    @BindView(R.id.code_ques_4)
    Button _codeQues4;

    private Intent mIntent;
    private DBHelper dbHelper;

    String codeString= "";

    private Context mContext;


    protected void onStart() {
        System.out.println("in CodeMode onStart");
        super.onStart();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        System.out.println("college main: " + codeString);


        if(UserManager.getInstance(this).isLoggedIn()) {
            dbHelper = new DBHelper(this.getApplicationContext());

            // Create Inner Thread Class
            Thread dbThread = new Thread(new Runnable() {

                // After call for background.start this run method call
                public void run() {
                    try {

                        codeString = dbHelper.checkQuestionStatus("code", new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                codeString = result;

                                SharedPreferences sharedPreferences = mContext.getSharedPreferences("sharedUserPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("codeString", codeString);
                                System.out.println("codeString: " + codeString);
                                editor.apply();

                                System.out.println("onSuccess codeString: " + codeString);
                            }
                        });

                        threadMsg(codeString);

                    } catch (Throwable t) {
                        // just end the background thread
                        Log.i("Code", "Thread  exception " + t);
                    }
                }

                private void threadMsg(String msg) {

                    if (!msg.equals(null) && !msg.equals("")) {
                        Message msgObj = handler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("message", msg);
                        msgObj.setData(b);
                        handler.sendMessage(msgObj);
                    }
                }

                // Define the Handler that receives messages from the thread and update the progress
                private final Handler handler = new Handler() {

                    public void handleMessage(Message msg) {

//                    String aResponse = msg.getData().getString("message");
                        System.out.println("handling msg");

                    }
                };

            });
            // Start Thread
            dbThread.start();  //After call start method thread called run Method

        }

        setContentView(R.layout.code_mode_start_layout);
        ButterKnife.bind(this);

//        mIntent = new Intent(getApplicationContext(), CodeModeQuestionActivity.class);
        mIntent = new Intent(getApplicationContext(), newCodeQuestionActivity.class);

        _codeQues1.setOnClickListener(this);
        _codeQues2.setOnClickListener(this);
        _codeQues3.setOnClickListener(this);
        _codeQues4.setOnClickListener(this);

        System.out.println("in CodeMode onCreate");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_ques_1:
                mIntent.putExtra("btn_id", "1");
                break;
            case R.id.code_ques_2:
                mIntent.putExtra("btn_id", "2");
                break;
            case R.id.code_ques_3:
                mIntent.putExtra("btn_id", "3");
                break;
            case R.id.code_ques_4:
                mIntent.putExtra("btn_id", "4");
                break;
            default:
                break;
        }
        startActivity(mIntent);
    }

}
