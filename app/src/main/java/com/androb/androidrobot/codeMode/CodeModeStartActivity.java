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

    private static final int QUES_1 = 1;
    private static final int QUES_2 = 2;

    private Intent mIntent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_mode_start_layout);

        ButterKnife.bind(this);

        mIntent = new Intent(getApplicationContext(), CodeModeQuestionActivity.class);

        _codeQues1.setOnClickListener(this);
        _codeQues2.setOnClickListener(this);

        System.out.println("in CodeMode onCreate");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_ques_1:
                // 请求码的值随便设置，但必须>=0
                mIntent.putExtra("btn_id", "1");
                System.out.println("clicked 1 in Start");
                startActivity(mIntent);
                break;
            case R.id.code_ques_2:
                mIntent.putExtra("btn_id", "2");
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

}
