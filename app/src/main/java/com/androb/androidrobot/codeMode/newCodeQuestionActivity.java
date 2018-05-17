package com.androb.androidrobot.codeMode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.utils.connectionUtil.BluetoothService;
import com.androb.androidrobot.utils.messageUtil.CodeInputFormatter;
import com.androb.androidrobot.utils.messageUtil.InputTextUtil;
import com.androb.androidrobot.utils.messageUtil.MessageFormatter;
import com.androb.androidrobot.utils.messageUtil.MessageValidator;
import com.androb.androidrobot.utils.questionUtil.QuestionStatusManager;
import com.androb.androidrobot.utils.userUtil.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/05/17.
 */

public class newCodeQuestionActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.code_ques_submit_btn)
    Button submitButton;
    @BindView(R.id.code_question_text)
    TextView codeQuestionText;
    @BindView(R.id.code_prompt_text)
    TextView codePromptText;
    @BindView(R.id.code_input_et)
    EditText etInput;
    @BindView(R.id.code_done)
    Button codeDone;
    @BindView(R.id.code_help_btn)
    ImageButton helpButton;

    private int questionId;
    private boolean isLoggedin;
    private boolean qStatus = false;
    private AlertDialog.Builder builder;
    private InputTextUtil inputUtil = new InputTextUtil();
    private CodeInputFormatter formatter= new CodeInputFormatter();
    private String inputStr;
    private JSONObject checkResult;
    private String jsonResult;
    private String codeString = "";

    private MessageValidator validator = new MessageValidator();

    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_code_mode_question_layout);

        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        questionId = Integer.parseInt(intent.getStringExtra("btn_id"));

        submitButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);

        switch (questionId) {
            case 1:
                codeQuestionText.setText(R.string.c_ques_1);
                codePromptText.setText(R.string.c_prompt_1);
                break;
            case 2:
                codeQuestionText.setText(R.string.c_ques_2);
                codePromptText.setText(R.string.c_prompt_2);
                break;
            case 3:
                codeQuestionText.setText(R.string.c_ques_3);
                codePromptText.setText(R.string.c_prompt_3);
                break;
            case 4:
                codeQuestionText.setText(R.string.c_ques_4);
                codePromptText.setText(R.string.c_prompt_4);
                break;
            default:
                break;
        }

        isLoggedin = UserManager.getInstance(this).isLoggedIn();

        codeDone.setVisibility(View.INVISIBLE);
        if(isLoggedin) {
            SharedPreferences sharedPreferences = this.getApplication().getSharedPreferences("sharedUserPref", Context.MODE_PRIVATE);
            qStatus = (sharedPreferences.getString("codeString", null).indexOf(questionId + "") == -1);
            if(!qStatus) {
                codeDone.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.code_ques_submit_btn:
                inputStr = etInput.getText().toString().toLowerCase().trim();
                Log.d("UserInput: ", inputStr);
                checkResult = inputUtil.checkInput(inputStr, inputStr.contains("for"));
                try {
                    if(checkResult.getBoolean("valid") == true){
                        Toast.makeText(this.getApplicationContext(), "语法正确", Toast.LENGTH_SHORT).show();

                        // into JSON
                        jsonResult = formatter.code2JSON(inputStr.split("\n")).toString();
                        Intent intent = new Intent(this, BluetoothService.class);
                        intent.putExtra("msg", jsonResult);
                        startService(intent);

                        if(validator.checkMessage(jsonResult, "code", questionId + "")) {
                            Toast.makeText(getApplicationContext(), "回答正确", Toast.LENGTH_SHORT).show();
                            System.out.println("in codeQues: correct" );

                            if(isLoggedin) {
                                UserManager.getInstance(getApplicationContext()).updateScore();
                                QuestionStatusManager.getInstance(getApplicationContext()).updateStatus("code", questionId);
                            }
                        }

                    }
                    else {
                        if(checkResult.getBoolean("EOL") != true)
                            Toast.makeText(this.getApplicationContext(), "是不是没写分号", Toast.LENGTH_SHORT).show();
                        if(checkResult.getBoolean("action") != true)
                            Toast.makeText(this.getApplicationContext(), "动作不太对", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.code_help_btn:
                this.showHelpDialog(view);
                break;

        }
    }

    private void showHelpDialog(View view) {
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("编程模式帮助");
        builder.setMessage(R.string.code_help_msg);

        //监听下方button点击事件
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "那就加油吧", Toast.LENGTH_SHORT).show();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
