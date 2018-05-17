package com.androb.androidrobot.utils.messageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kaki on 2018/05/17.
 */

public class CodeInputFormatter {

    private final int CAR_FORWARD = 1;
    private final int CAR_BACKWARD = 0;
    private final int CAR_RIGHT = 2;
    private final int CAR_LEFT = 3;
    private final int CAR_PAUSE = 4;
    private final int CAR_SING = 5;
    private final int CAR_REPEAT = 10;


    public JSONObject code2JSON(String[] lines) {
        JSONObject result = new JSONObject();

        for(int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if(line.contains(".")) {  // is action
                line = line.substring(0, line.length() - 1);
                System.out.println("in code2JSON action str: " + line);
                String curSplitStr[] = line.split("\\.");
                String action = curSplitStr[1].split("\\(")[0];
                String param = curSplitStr[1].split("\\(")[1];
                param = param.substring(0, param.length() - 1);
                System.out.println("action: " + action);
                try {
                    this.switchAction(action, param, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(!line.equals("}")){ // is for
                String newLine = line;
                line = line.substring(4, line.length() - 2);
                System.out.println("for line: " + line);
                String[] forSplit = line.split(";");
                int start = Integer.valueOf(forSplit[0].split("=")[1]);
                int end = Integer.valueOf(forSplit[1].split("<")[1]);
                // TODO: check if end > start
                int time = end - start + 1;
                System.out.println("for line, repeat " + time + " times");

                // TODO: get two ptrs from the start of repeat to end, parse params to another function
                ArrayList<String> strList = new ArrayList<String>();
                strList.addAll(Arrays.asList(lines));
                int repeatStart = strList.indexOf(newLine);
                int repeatEnd = strList.indexOf("}");
//                int repeatStart = Arrays.binarySearch(lines, line);
//                int repeatEnd = Arrays.binarySearch(lines, "}");
                System.out.println("repeat start at: " + repeatStart);
                System.out.println("repeat end at: " + repeatEnd);
                String[] repeatLines = Arrays.copyOfRange(lines, repeatStart + 1, repeatEnd);

                JSONObject repeatActions = this.getRepeatActions(repeatLines);

                System.out.println("repeatActions JSON: " + repeatActions.toString());

                JSONObject outerRepeat = new JSONObject();
                try {
                    outerRepeat.put("" + time, repeatActions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    result.put(CAR_REPEAT + "", outerRepeat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                i = repeatEnd;
            }
        }

        System.out.println("CodeFormat final result: " + result.toString());
        return result;
    }

    private JSONObject getRepeatActions(String[] repeatLines) {
        JSONObject repeatActions = new JSONObject();
        for(String line : repeatLines) {
            line = line.substring(0, line.length() - 1);
            System.out.println("in getRepeatActions str: " + line);
            String curSplitStr[] = line.split("\\.");
            String action = curSplitStr[1].split("\\(")[0];
            String param = curSplitStr[1].split("\\(")[1];
            param = param.substring(0, param.length() - 1);
            System.out.println("in getRepeatActions action: " + action);
            try {
                this.switchAction(action, param, repeatActions);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return repeatActions;
    }

    private void switchAction(String input, String param, JSONObject jsonObject) throws JSONException {
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

}
