package com.androb.androidrobot.utils.messageUtil;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kaki on 2018/05/17.
 */

public class InputTextUtil {

    private boolean valid = false;
    private boolean eolCheck;
    private boolean actionCheck;
    private boolean repeatCheck;

    JSONObject checkReport;

    private String actionRegex;
    private String repeatRegex;
    private Pattern pattern;
    private Matcher matcher;

    public JSONObject checkInput(String input, boolean hasRepeat) {
        checkReport  = new JSONObject();

        String[] lines = input.split("\n");
        Log.d("CheckUtil", lines.length + "");

        eolCheck = this.checkEndOfLine(lines);
        actionCheck = this.checkActionFormat(lines);

        Log.d("CheckUtil eolCheck", eolCheck + "");
        Log.d("CheckUtil actionCheck", actionCheck + "");

        if(hasRepeat == true) {
            repeatCheck = this.checkRepeatFormat(lines);
            Log.d("CheckUtil repeatCheck", repeatCheck + "");

            if(eolCheck == true && actionCheck == true && repeatCheck == true) {
                valid = true;
                try {
                    checkReport.put("valid", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                valid = false;
                try {
                    checkReport.put("valid", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            if(eolCheck == true && actionCheck == true){
                valid = true;
                try {
                    checkReport.put("valid", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                valid = false;
                try {
                    checkReport.put("valid", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        Log.d("CheckUtil totalValid", valid + "");

        System.out.println("in check JSON result: " + checkReport.toString());
        return checkReport;
    }


    private boolean checkEndOfLine(String[] lines){
        for(String line : lines) {
            if(!line.contains("for") && !line.equals("}")) {
                System.out.println("EOL line: " + line);
                if (!line.endsWith(";")) {
                    try {
                        checkReport.put("EOL", false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }
        }
        try {
            checkReport.put("EOL", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkActionFormat(String[] lines){
        // TODO: 前面有空格的情况？？？？时对时错蜜汁玄学【用虚拟机打字真的惨
        actionRegex = "car.forward\\([0-9]*\\)|car.backward\\([0-9]*\\)|car.right\\([0-9]*\\)|car.left\\([0-9]*\\)|car.pause\\([0-9]*\\)|car.sing\\([0-9]*\\)";
        pattern = Pattern.compile(actionRegex);

        for(String line : lines) {
            if(!line.contains("for") && !line.equals("}")) {
                line = line.substring(0, line.length() - 1);
                System.out.println("line in checkAction: " + line);
                matcher = pattern.matcher(line);
                if(matcher.matches() == false) {
                    try {
                        checkReport.put("action", false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }
        }
        try {
            checkReport.put("action", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }


    private boolean checkRepeatFormat(String[] lines) {
        repeatRegex = "for\\([a-z]+=[0-9]+;[a-z]<([1-9][0-9]+|[1-9]+);([a-z]+\\+\\+)\\)\\{";
        pattern = Pattern.compile(repeatRegex);

        for(String line : lines) {
            if(line.contains("for(")) {
                System.out.println("line in repeatCheck: " + line);
                matcher = pattern.matcher(line);
                if(matcher.matches() == false) {
                    try {
                        checkReport.put("repeat", false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }
        }
        try {
            checkReport.put("repeat", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }


    public JSONObject getActionAndParams(String input) {
        JSONObject result = new JSONObject();



        return result;
    }
}
