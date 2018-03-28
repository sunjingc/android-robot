package com.androb.androidrobot.codeMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.androb.androidrobot.R;
import com.androb.androidrobot.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/28.
 */

public class CodeModeQuestionActivity extends AppCompatActivity {

    @BindView(R.id.code_ques_submit_btn)
    Button _submitButton;

    private int resultCode = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_mode_question_layout);

        ButterKnife.bind(this);


        // this part not working & idk why...
        // TODO: Look up more on onActivityResult & startActivityForResult
        Intent mIntent = new Intent();
        mIntent.putExtra("change01", "1000");
        mIntent.putExtra("change02", "2000");
        // 设置结果，并进行传送
        this.setResult(resultCode, mIntent);


        System.out.println("in CodeQues onCreate");

    }


}
