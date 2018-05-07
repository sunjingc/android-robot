package com.androb.androidrobot.codeMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androb.androidrobot.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/28.
 */

public class CodeModeStartActivity extends AppCompatActivity implements OnClickListener {

    @BindView(R.id.code_ques_1)
    Button _codeQues1;
    @BindView(R.id.code_ques_2)
    Button _codeQues2;
    @BindView(R.id.code_ques_3)
    Button _codeQues3;
    @BindView(R.id.code_ques_4)
    Button _codeQues4;

    private Intent mIntent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_mode_start_layout);

        ButterKnife.bind(this);

        mIntent = new Intent(getApplicationContext(), CodeModeQuestionActivity.class);

        _codeQues1.setOnClickListener(this);
        _codeQues2.setOnClickListener(this);
        _codeQues3.setOnClickListener(this);
        _codeQues4.setOnClickListener(this);

        System.out.println("in CodeMode onCreate");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_ques_1:
                mIntent.putExtra("btn_id", "1");
                startActivity(mIntent);
                break;
            case R.id.code_ques_2:
                mIntent.putExtra("btn_id", "2");
                startActivity(mIntent);
                break;
            case R.id.code_ques_3:
                mIntent.putExtra("btn_id", "3");
                startActivity(mIntent);
                break;
            case R.id.code_ques_4:
                mIntent.putExtra("btn_id", "4");
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

}
