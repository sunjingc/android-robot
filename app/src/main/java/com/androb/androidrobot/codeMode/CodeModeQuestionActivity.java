package com.androb.androidrobot.codeMode;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.RegisterActivity;

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

    private int questionId = 0;
    private String mTestStr;

    private SpansManager mSpansManager;


    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("in CodeQues onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_mode_question_layout);

        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        questionId = Integer.parseInt(intent.getStringExtra("btn_id"));

        System.out.println("Question ID: " + questionId);

        switch (questionId) {
            case 1:
                codeQuestionText.setText(R.string.c_ques_1);
                codeInputText.setText(R.string.c_code_1);
                break;
            case 2:
                codeQuestionText.setText(R.string.c_ques_2);
                codeInputText.setText(R.string.c_code_2);
                break;
            default:
                break;
        }
        mTestStr = (String) codeInputText.getText();
        codeInputText.setText("");
        mSpansManager = new SpansManager(this, codeInputText, etInput);
        mSpansManager.doFillBlank(mTestStr);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.code_ques_submit_btn:
                System.out.println("clicked submit");   // wrong here, no repsonse
                Toast.makeText(this, getMyAnswerStr(), Toast.LENGTH_LONG).show();
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
        System.out.println("in GetMYAnswerStr");
        mSpansManager.setLastCheckedSpanText(etInput.getText().toString());
        return mSpansManager.getMyAnswer().toString();
    }


}
