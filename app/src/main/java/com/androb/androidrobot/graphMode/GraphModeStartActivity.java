package com.androb.androidrobot.graphMode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class GraphModeStartActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.graph_ques_1)
    Button _graphQues1;
    @BindView(R.id.graph_ques_2)
    Button _graphQues2;
    @BindView(R.id.graph_ques_3)
    Button _graphQues3;
    @BindView(R.id.graph_ques_4)
    Button _graphQues4;

    private Intent mIntent;

    // question status
    private Context mContext;
    private DBHelper dbHelper;
    private String graphString = "";

    protected void onStart() {
        super.onStart();

        mContext = this.getApplicationContext();
        if(UserManager.getInstance(this).isLoggedIn()) {
            dbHelper = new DBHelper(this.getApplicationContext());

            // Create Inner Thread Class
            Thread dbThread = new Thread(new Runnable() {

                // After call for background.start this run method call
                public void run() {
                    try {

                        graphString = dbHelper.checkQuestionStatus("graph", new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                graphString = result;

                                SharedPreferences sharedPreferences = mContext.getSharedPreferences("sharedUserPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("graphString", graphString);
                                System.out.println("graphString: " + graphString);
                                editor.apply();

                                System.out.println("onSuccess graphString: " + graphString);
                            }
                        });

                        threadMsg(graphString);

                    } catch (Throwable t) {
                        // just end the background thread
                        Log.i("Graph", "Thread  exception " + t);
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
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.graph_mode_start_layout);

        ButterKnife.bind(this);

        mIntent = new Intent(getApplicationContext(), GraphModeQuestionActivity.class);

        _graphQues1.setOnClickListener(this);
        _graphQues2.setOnClickListener(this);
        _graphQues3.setOnClickListener(this);
        _graphQues4.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.graph_ques_1:
                // 请求码的值随便设置，但必须>=0
                mIntent.putExtra("btn_id", "1");
                System.out.println("clicked 1 in Start");
                startActivity(mIntent);
                break;
            case R.id.graph_ques_2:
                mIntent.putExtra("btn_id", "2");
                startActivity(mIntent);
                break;
            case R.id.graph_ques_3:
                mIntent.putExtra("btn_id", "3");
                startActivity(mIntent);
                break;
            case R.id.graph_ques_4:
                mIntent.putExtra("btn_id", "4");
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }
}
