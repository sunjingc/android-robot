package com.androb.androidrobot.codeMode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.dragMode.DragModeQuestionActivity;
import com.androb.androidrobot.messageUtil.MessageService;
import com.androb.androidrobot.userManagement.SharedUserManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/28.
 */

public class CodeModeQuestionActivity extends AppCompatActivity implements View.OnClickListener,ReplaceSpan.OnClickListener {

    @BindView(R.id.code_ques_submit_btn)
    Button submitButton;
    @BindView(R.id.code_question_text)
    TextView codeQuestionText;
    @BindView(R.id.code_input_text)
    TextView codeInputText;
    @BindView(R.id.et_input)
    EditText etInput;

    @BindView(R.id.code_help_btn)
    ImageButton helpButton;

    private int questionId = 0;
    private String mTestStr;

    private String answerString;

    private String quesType = "code";

    private SpansManager mSpansManager;

    private AlertDialog.Builder builder;

    private String jsonResult;
    private JSONReceiver receiver;
    private boolean isLoggedin;

    // 处理输入格式
//    private MessageService msgService;


    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("in CodeQues onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_mode_question_layout);

        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        questionId = Integer.parseInt(intent.getStringExtra("btn_id"));

        System.out.println("Question ID: " + questionId);

        submitButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);

        switch (questionId) {
            case 1:
                codeQuestionText.setText(R.string.c_ques_1);
                codeInputText.setText(R.string.c_code_1);
                break;
            case 2:
                codeQuestionText.setText(R.string.c_ques_2);
                codeInputText.setText(R.string.c_code_2);
                break;
            case 3:
                codeQuestionText.setText(R.string.c_ques_3);
                codeInputText.setText(R.string.c_code_3);
                break;
            case 4:
                codeQuestionText.setText(R.string.c_ques_4);
                codeInputText.setText(R.string.c_code_4);
                break;
            default:
                break;
        }
        mTestStr = (String) codeInputText.getText();
        codeInputText.setText("");
        mSpansManager = new SpansManager(this, codeInputText, etInput);
        mSpansManager.doFillBlank(mTestStr);

        isLoggedin = SharedUserManager.getInstance(this).isLoggedIn();

        receiver = new JSONReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.androb.androidrobot.messageUtil.MessageService");
        registerReceiver(receiver, filter);

    }

    @Override
    public void onClick(View view) {

        Toast.makeText(this, getMyAnswerStr(), Toast.LENGTH_LONG).show();
        answerString = getMyAnswerStr();

        switch (view.getId()){
            case R.id.code_ques_submit_btn:

                boolean isValid = false;
                switch(questionId) {
                    case 1:
                        isValid = this.checkQues1(answerString);
                        break;

                    case 2:
                        isValid = this.checkQues2(answerString);
                        break;

                    case 3:
                        isValid = this.checkQues3(answerString);
                        break;

                    case 4:
                        isValid = this.checkQues4(answerString);
                        break;
                }

                if(isValid) {
                    Intent submitIntent = new Intent(this, MessageService.class);

                    submitIntent.putExtra("answerStr", answerString);
                    submitIntent.putExtra("quesType", quesType);
                    submitIntent.putExtra("quesId", questionId + "");
                    startService(submitIntent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "哪里有点问题啊", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.code_help_btn:

                this.showHelpDialog(view);

                break;
        }
    }

    //填空题点击响应事件
    @Override
    public void OnClick(TextView v, int id, ReplaceSpan span) {
        mSpansManager.setData(etInput.getText().toString(),null, mSpansManager.mOldSpan);
        mSpansManager.mOldSpan = id;
        //如果当前span身上有值，先赋值给et身上
        etInput.setText(TextUtils.isEmpty(span.mText)?"":span.mText);
        etInput.setSelection(span.mText.length());
        span.mText = "";
        //通过rf计算出et当前应该显示的位置
        RectF rf = mSpansManager.drawSpanRect(span);
        //设置EditText填空题中的相对位置
        mSpansManager.setEtXY(rf);
        mSpansManager.setSpanChecked(id);
    }

    private String getMyAnswerStr(){
//        System.out.println("in GetMYAnswerStr");
        mSpansManager.setLastCheckedSpanText(etInput.getText().toString());
        return mSpansManager.getMyAnswer().toString();
    }

    private void showHelpDialog(View view) {
        builder=new AlertDialog.Builder(this);
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

    private boolean checkQues1(String answerStr) {
        boolean valid = true;
        int len = answerStr.length();
        answerStr = answerStr.substring(1, len - 1);
        String sub[] = answerStr.split(", ");

        if(!sub[0].equals("forward") || !this.isInteger(sub[1]) || !this.isInteger(sub[3])
                || !this.isInteger(sub[4]) || !sub[2].equals("right") ) {
            return false;
        }
        return valid;
    }

    private boolean checkQues2(String answerStr) {
        boolean valid = true;
        int len = answerStr.length();
        answerStr = answerStr.substring(1, len - 1);
        String sub[] = answerStr.split(", ");

        if(!sub[1].equals("pause") || !this.isInteger(sub[0]) || !this.isInteger(sub[2])
                || !this.isInteger(sub[4]) || !sub[3].equals("sing") ) {
            return false;
        }
        return valid;
    }

    private boolean checkQues3(String answerStr) {
        boolean valid = true;
        int len = answerStr.length();
        answerStr = answerStr.substring(1, len - 1);
        String sub[] = answerStr.split(", ");

        if(!this.isInteger(sub[0]) || !this.isInteger(sub[1]) || !this.isInteger(sub[2]) ) {
            return false;
        }
        return valid;
    }

    private boolean checkQues4(String answerStr) {
        boolean valid = true;
        int len = answerStr.length();
        answerStr = answerStr.substring(1, len - 1);
        String sub[] = answerStr.split(", ");

        if(!this.isInteger(sub[0]) || !this.isInteger(sub[1])
                || !this.isInteger(sub[2]) || !this.isInteger(sub[4]) ) {
            valid = false;
        }

        if(!sub[3].equals("right") && !sub[3].equals("left")) {
            valid = false;
        }
        return valid;
    }


    private boolean isInteger(String input){
        boolean result = false;
        Matcher mer = Pattern.compile("^[0-9]+$").matcher(input);
        result = mer.find();
        System.out.println("in isInt: " + input + "   " + result);
        return result;
    }

    public class JSONReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            jsonResult = intent.getStringExtra("json");

            if(checkAnswer(jsonResult) == true){
                Toast.makeText(CodeModeQuestionActivity.this, "回答正确", Toast.LENGTH_SHORT).show();
                if(isLoggedin) {
                    SharedUserManager.getInstance(context).updateScore();
                }
            }
            else {
                Toast.makeText(CodeModeQuestionActivity.this, "回答不对哦", Toast.LENGTH_SHORT).show();
            }

            System.out.println("in GraphMode, JSONReceiver received jsonResult: " + jsonResult);
        }
    }

    private boolean checkAnswer(String answer) {
        boolean result = false;
        switch(questionId) {
            case 1:
                if (answer.equals("{\"1\":\"4\",\"2\":\"90\",\"0\":\"3\"}")) {
                    System.out.println("true");
                    result = true;
                } else {
                    System.out.println("false");
                    result = false;
                }
                break;
            case 2:
                if (answer.equals("{\"1\":\"2\",\"4\":\"3\",\"5\":\"1\"}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case 3:
                if (answer.equals("{\"10\":{\"5\":{\"1\":\"2\",\"4\":\"3\"}}}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case 4:
                if ( (answer.equals("{\"5\":\"1\",\"10\":{\"4\":{\"1\":\"3\",\"3\":\"90\"}}}"))
                        || (answer.equals("{\"5\":\"1\",\"10\":{\"4\":{\"1\":\"3\",\"2\":\"90\"}}}")) ) {
                    result = true;
                } else {
                    result = false;
                }
                break;
        }
        return result;
    }

}
