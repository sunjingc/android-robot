package com.androb.androidrobot.messageUtil;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androb.androidrobot.R;
import com.androb.androidrobot.connectionUtil.BluetoothService;
import com.androb.androidrobot.connectionUtil.BluetoothSocketSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kaki on 2018/04/26.
 */

public class MessageService extends Service {


    // 定义
    private final int CAR_FORWARD = 1;
    private final int CAR_BACKWARD = 0;

    private final int CAR_RIGHT = 2;
    private final int CAR_LEFT = 3;

    private final int CAR_PAUSE = 4;
    private final int CAR_SING = 5;

    private final int CAR_REPEAT = 10;

    private String quesType;
    private String quesId;
    private String answerStr;

    private HashMap<String, Integer> dragTranslation = new HashMap<>();

    private List<String> codeBlankProperties = new ArrayList<String>();
    private List<String> codeActionList = new ArrayList<String>();
    private HashMap<String, Integer> codeTranslation = new HashMap<>();

    private JSONObject jsonResult;

    private BluetoothSocketSingleton btSingleton;
    private BluetoothSocket btSocket;


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

        dragTranslation.put("前进", CAR_FORWARD);
        dragTranslation.put("后退", CAR_BACKWARD);
        dragTranslation.put("左转", CAR_LEFT);
        dragTranslation.put("右转", CAR_RIGHT);
        dragTranslation.put("暂停", CAR_PAUSE);
        dragTranslation.put("唱歌", CAR_SING);


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
                try {
                    jsonResult = this.getCodeJSON(answerStr, qid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "graph":
                try {
                    jsonResult = this.getGraphJSON(answerStr, qid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "drag":
                try {
                    jsonResult = this.getDragJSON(answerStr, qid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

        Intent resultIntent = new Intent("com.androb.androidrobot.messageUtil.MessageService");
        resultIntent.putExtra("json", jsonResult.toString());
        sendBroadcast(resultIntent);

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
    private JSONObject getCodeJSON(String result, int quesId) throws JSONException {
        JSONObject codeJson = new JSONObject();

        codeBlankProperties.add("forward,f_param,r_direction,r_param,b_param");
        codeBlankProperties.add("f_param,pause,p_param,sing,s_param");
        codeBlankProperties.add("repeat_param,f_param,p_param");
        codeBlankProperties.add("s_param,repeat_param,f_param,r_direction,r_param");

        codeActionList.add("forward,right,backward");
        codeActionList.add("forward,pause,sing");
        codeActionList.add("repeat,forward,pause");
        codeActionList.add("sing,repeat,forward,righrNleft");

        int len = result.length();
        result = result.substring(1, len - 1);
        System.out.println("in getCodeJSON: " + result);

        String substrs[] = result.split(", ");
        int blankCount = substrs.length;

        switch(quesId) {
            case 1:
                codeJson.put("" + CAR_FORWARD, substrs[1]);
                codeJson.put("" + CAR_RIGHT, substrs[3]);
                codeJson.put("" + CAR_BACKWARD, substrs[4]);
                break;

            case 2:
                codeJson.put("" + CAR_FORWARD, substrs[0]);
                codeJson.put("" + CAR_PAUSE, substrs[2]);
                codeJson.put("" + CAR_SING, substrs[4]);
                break;

            case 3:
                JSONObject repeatAction = new JSONObject();
                repeatAction.put("" + CAR_FORWARD, substrs[1]);
                repeatAction.put("" + CAR_PAUSE, substrs[2]);

                JSONObject outerRepeat = new JSONObject();
                outerRepeat.put(substrs[0], repeatAction);

                codeJson.put("" + CAR_REPEAT, outerRepeat);
                break;

            case 4:
                codeJson.put("" + CAR_SING, substrs[0]);

                JSONObject repeatAction4 = new JSONObject();
                repeatAction4.put("" + CAR_FORWARD, substrs[2]);
                if(substrs[3].equals("right")) {
                    repeatAction4.put("" + CAR_RIGHT, substrs[4]);
                }
                else if(substrs[3].equals("left")) {
                    repeatAction4.put("" + CAR_LEFT, substrs[4]);
                }

                JSONObject outerRepeat4 = new JSONObject();
                outerRepeat4.put(substrs[1], repeatAction4);

                codeJson.put("" + CAR_REPEAT, outerRepeat4);

                break;

        }

        System.out.println("codeJson: " + codeJson.toString());
        return codeJson;
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

                    String action = curSplitStr[1].split("\\(")[0];

                    String param = curSplitStr[1].split("\\(")[1];

                    System.out.println("action: " + action);

                    this.switchActionforGraph(action, param, graphJson);

                }

                System.out.println("JSON: " + graphJson.toString());

                break;

            case 2:

                for(String curStr : strs) {
                    String curSplitStr[] = curStr.split(".");
                    String action = curSplitStr[1].split("\\(")[0];

                    String param = curSplitStr[1].split("\\(")[1];

                    this.switchActionforGraph(action, param, graphJson);

                }

            case 3:

                String firstRepeat = strs[0];

                String[] newstrs = new String[strs.length - 1];
                System.arraycopy(strs, 1, newstrs, 0, newstrs.length);

                String[] firstSplit = firstRepeat.split("< ");
                String times = firstSplit[1];

                JSONObject innerRepeat = new JSONObject();

                for(String curStr : newstrs) {
                    String curSplitStr[] = curStr.split(".");
                    String action = curSplitStr[1].split("\\(")[0];

                    String param = curSplitStr[1].split("\\(")[1];

                    this.switchActionforGraph(action, param, innerRepeat);
                }

                JSONObject outerRepeat = new JSONObject();
                outerRepeat.put(times, innerRepeat);
                graphJson.put("" + CAR_REPEAT, outerRepeat);

                break;

            case 4:
                Date today = new Date();
                Calendar cal = Calendar.getInstance();
                cal .setTime(today);
                int day = cal.get(Calendar.DAY_OF_WEEK);

                if(day == 7) {
                    String[] newstrs4 = new String[strs.length - 1];
                    System.arraycopy(strs, 1, newstrs4, 0, newstrs4.length);

                    for(String curStr : newstrs4) {
                        String curSplitStr[] = curStr.split(".");
                        String action = curSplitStr[1].split("\\(")[0];

                        String param = curSplitStr[1].split("\\(")[1];

                        param = param.substring(0, param.length() - 1);

                        this.switchActionforGraph(action, param, graphJson);
                    }
                }

                break;
        }

        return graphJson;
    }

    private void switchActionforGraph(String input, String param, JSONObject jsonObject) throws JSONException {
        switch(input) {
            case "forward":
                jsonObject.put(CAR_FORWARD + "", param);
                break;

            case "backward":
                jsonObject.put(CAR_BACKWARD + "", param);
                break;

            case "right":
                jsonObject.put(CAR_RIGHT + "", param);
                break;

            case "left":
                jsonObject.put(CAR_LEFT + "", param);
                break;

            case "sing":
                jsonObject.put(CAR_SING + "", param);
                break;

            case "pause":
                jsonObject.put(CAR_PAUSE + "", param);
                break;

        }
    }

    private JSONObject getDragJSON(String result, int qid) throws JSONException {
        JSONObject dragJson = new JSONObject();

        String lowerStr = result.toLowerCase();
        int length = lowerStr.length();

        lowerStr = lowerStr.substring(1, length - 1);
        System.out.println("getDragJSON-lowerStr: " + lowerStr);

        String strs[] = lowerStr.split(",");

        System.out.println("strs length: " + strs.length);

        int num = strs.length;

        for(int cur = 0; cur < num; cur++) {
            String temp = strs[cur];
            String para = "";

            /**
             * 现在只能默认循环之后的所有内容都在循环内orz
             */
            if(temp.contains("循环") == false) {
                dragJson = this.addJSONforDrag(temp, dragJson);
            }
            else {

                int repeatPtr = cur;
                String tempNum = temp.split("循环")[1];
                int repeatTimes = Integer.valueOf(tempNum.substring(0, tempNum.length() - 1));

                JSONObject repeatActionJson = new JSONObject();

                for (int a = repeatPtr + 1; a < num; a++) {
                    String tempInRepeat = strs[a];

                    System.out.println("tempInRepeat: " + tempInRepeat);

                    repeatActionJson = this.addJSONforDrag(tempInRepeat, repeatActionJson);
                }

                System.out.println("RepeatActionJson: " + repeatActionJson);

                JSONObject repeatFinalJson = new JSONObject();
                repeatFinalJson.put("" + repeatTimes, repeatActionJson.toString());

                dragJson.put(CAR_REPEAT + "", repeatFinalJson.toString());

                break;
            }

        }
        System.out.println("in dragJSON: " + dragJson.toString());

        return dragJson;
    }


    private JSONObject addJSONforDrag(String temp, JSONObject baseJson) throws JSONException {
        String para;
        if (temp.contains("前进")) {
            String tempNum = temp.split("前进")[1];
            para = tempNum.substring(0, tempNum.length() - 1);

            baseJson.put(CAR_FORWARD + "", para);
        } else if (temp.contains("后退")) {
            String tempNum = temp.split("后退")[1];
            para = tempNum.substring(0, tempNum.length() - 1);

            baseJson.put(CAR_BACKWARD + "", para);
        } else if (temp.contains("左转")) {
            String tempNum = temp.split("左转")[1];
            para = tempNum.substring(0, tempNum.length() - 1);

            baseJson.put(CAR_LEFT + "", para);
        } else if (temp.contains("右转")) {
            String tempNum = temp.split("右转")[1];
            para = tempNum.substring(0, tempNum.length() - 1);

            baseJson.put(CAR_RIGHT + "", para);
        } else if (temp.contains("暂停")) {
            String tempNum = temp.split("暂停")[1];
            para = tempNum.substring(0, tempNum.length() - 1);

            baseJson.put(CAR_PAUSE + "", para);
        } else if (temp.contains("唱歌")) {
            String tempNum = temp.split("唱歌")[1];
            para = tempNum.substring(0, tempNum.length() - 1);

            baseJson.put(CAR_SING + "", para);
        }

        return baseJson;
    }

    private boolean transferMsg(String msg) {
        boolean result = false;
        btSingleton = new BluetoothSocketSingleton();
        btSocket = btSingleton.getInstance();

        try {
            btSocket.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
