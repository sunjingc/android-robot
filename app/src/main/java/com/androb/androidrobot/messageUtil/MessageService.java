package com.androb.androidrobot.messageUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androb.androidrobot.connectionUtil.BluetoothService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kaki on 2018/04/26.
 */

public class MessageService extends Service {


    // 定义
    private final int CAR_FORWARD = 1;
    private final int CAR_BACKWARD = 0;

    private final int CAR_RIGHT = 2;
    private final int CAR_LEFT = 3;

    private String quesType;
    private String quesId;
    private String answerStr;

    private JSONObject jsonResult;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MessageService", "onBind");
        return null;
    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent btIntent = new Intent(this, BluetoothService.class);

        System.out.println("MessageService onStartCommand invoke");

        answerStr = intent.getStringExtra("answerStr");
        quesType = intent.getStringExtra("quesType");
        quesId = intent.getStringExtra("quesId");

        int qid = Integer.valueOf(quesId);

        System.out.println("message is here: " + answerStr);
        System.out.println("ques Type is: " + quesType);
        System.out.println("ques Id is: " + quesId);

//        startService(btIntent);
        switch (quesType) {
            case "code":
                jsonResult = this.getCodeJSON(answerStr);
                break;

            case "graph":
                try {
                    jsonResult = this.getGraphJSON(answerStr, qid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
        System.out.println("onDestroy invoke");
        super.onDestroy();
    }


    /**
     * 转换Code模式的json
     * @param result
     * @return
     */
    private JSONObject getCodeJSON(String result) {
        JSONObject graphJson = null;



        return graphJson;
    }


    /**
     * 转换Graph模式的json
     * @param result
     * @return
     */
    private JSONObject getGraphJSON(String result, int quesId) throws JSONException {
        JSONObject graphJson = new JSONObject();

        String lowerStr = result.toLowerCase();
        int length = lowerStr.length();

        lowerStr = lowerStr.substring(1, length - 1);
        System.out.println("getGraphJSON-lowerStr: " + lowerStr);

        String strs[] = lowerStr.split(",");

        System.out.println("strs length: " + strs.length);

        switch(quesId) {
            case 1:

                for(String curStr : strs) {
                    System.out.println("curStr: " + curStr);

                    String curSplitStr[] = curStr.split("\\.");

                    System.out.println("curSplitStr length: " + curSplitStr.length);

//                    for(String cs : curSplitStr) {
//                        System.out.println("curSplitStr[]: " + cs);
//                    }

                    String action = curSplitStr[1].split("\\(")[0];

                    String param = curSplitStr[1].split("\\(")[1];

                    System.out.println("action: " + action);

                    switch(action) {
                        case "forward":
                            graphJson.put(CAR_FORWARD + "", param);
                            break;

                        case "backward":
                            graphJson.put(CAR_BACKWARD + "", param);
                            break;

                        case "right":
                            graphJson.put(CAR_RIGHT + "", param);
                            break;

                        case "left":
                            graphJson.put(CAR_LEFT + "", param);
                            break;

                    }

                }

                System.out.println("JSON: " + graphJson.toString());

                break;

            case 2:

                for(String curStr : strs) {
                    String curSplitStr[] = curStr.split(".");
                    String action = curSplitStr[1].split("\\(")[0];

                    String param = curSplitStr[1].split("\\(")[1];

                    switch(action) {
                        case "forward":
                            graphJson.put(CAR_FORWARD + "", param);
                            break;

                        case "backward":
                            graphJson.put(CAR_BACKWARD + "", param);
                            break;

                        case "right":
                            graphJson.put(CAR_RIGHT + "", param);
                            break;

                        case "left":
                            graphJson.put(CAR_LEFT + "", param);
                            break;

                    }

                }
        }


        return graphJson;
    }
}
