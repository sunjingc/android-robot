package com.androb.androidrobot.utils.messageUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androb.androidrobot.utils.connectionUtil.BluetoothMsgUtil;
import com.androb.androidrobot.dragMode.DragModeQuestionActivity;
import com.androb.androidrobot.utils.userUtil.UserManager;

/**
 * Created by kaki on 2018/05/14.
 */

public class JSONMessageReceiver extends BroadcastReceiver {

    private String jsonResult;
    private boolean isLoggedin;

    private BluetoothMsgUtil btMsgUtil = new BluetoothMsgUtil();

    @Override
    public void onReceive(Context context, Intent intent) {
        jsonResult = intent.getStringExtra("json");
        isLoggedin = intent.getBooleanExtra("loginStatus", false);

        System.out.println("JSONMessageReceiver received jsonResult: " + jsonResult);

        Toast.makeText(context, "JSONMessageReceiver received jsonResult: " + jsonResult, Toast.LENGTH_SHORT).show();

//            this.sendBtMsg(jsonResult);
        btMsgUtil.sendMsg(jsonResult);
//        if(checkAnswer(jsonResult) == true){
//            Toast.makeText(context, "回答正确", Toast.LENGTH_SHORT).show();
//            if(isLoggedin) {
//                UserManager.getInstance(context).updateScore();
//            }
//        }
//        else {
//            Toast.makeText(context, "回答不对哦", Toast.LENGTH_SHORT).show();
//        }

        Toast.makeText(context, "after sendBtMsg", Toast.LENGTH_SHORT).show();
    }
}
