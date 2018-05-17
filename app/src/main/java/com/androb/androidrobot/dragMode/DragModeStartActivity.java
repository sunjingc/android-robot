package com.androb.androidrobot.dragMode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.utils.dbUtil.DBHelper;
import com.androb.androidrobot.utils.dbUtil.VolleyCallback;
import com.androb.androidrobot.utils.userUtil.UserManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/25.
 */

public class DragModeStartActivity extends AppCompatActivity implements View.OnClickListener {

//    @BindView(R.id.dragMode_content)
//    DragFillBlankView dragModeContent;

    @BindView(R.id.drag_ques_1)
    Button _dragQues1;
    @BindView(R.id.drag_ques_2)
    Button _dragQues2;
    @BindView(R.id.drag_ques_3)
    Button _dragQues3;
    @BindView(R.id.drag_ques_4)
    Button _dragQues4;

//    private TitleBar mTitleBar;
//    private Select_1Blank select_1btn;

    private Spinner sp;
    private List<String> list, list_test;

    private Intent mIntent;

    // question status
    private String dragString = "";
    private DBHelper dbHelper;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this.getApplicationContext();
        if(UserManager.getInstance(this).isLoggedIn()) {
            dbHelper = new DBHelper(this.getApplicationContext());

            // Create Inner Thread Class
            Thread dbThread = new Thread(new Runnable() {

                // After call for background.start this run method call
                public void run() {
                    try {

                        dragString = dbHelper.checkQuestionStatus("drag", new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                dragString = result;

                                SharedPreferences sharedPreferences = mContext.getSharedPreferences("sharedUserPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("dragString", dragString);
                                System.out.println("dragString: " + dragString);
                                editor.apply();

                                System.out.println("onSuccess dragString: " + dragString);
                            }
                        });

                        threadMsg(dragString);

                    } catch (Throwable t) {
                        // just end the background thread
                        Log.i("Drag", "Thread  exception " + t);
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

        setContentView(R.layout.drag_mode_start_layout);

        ButterKnife.bind(this);

        mIntent = new Intent(getApplicationContext(), DragModeQuestionActivity.class);

        _dragQues1.setOnClickListener(this);
        _dragQues2.setOnClickListener(this);
        _dragQues3.setOnClickListener(this);
        _dragQues4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        System.out.println("in DragStart onClick");
        switch (v.getId()) {
            case R.id.drag_ques_1:
                // 请求码的值随便设置，但必须>=0
                mIntent.putExtra("btn_id", "1");
                startActivity(mIntent);
                break;
            case R.id.drag_ques_2:
                mIntent.putExtra("btn_id", "2");
                startActivity(mIntent);
                break;
            case R.id.drag_ques_3:
                mIntent.putExtra("btn_id", "3");
                startActivity(mIntent);
                break;
            case R.id.drag_ques_4:
                mIntent.putExtra("btn_id", "4");
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }


}
