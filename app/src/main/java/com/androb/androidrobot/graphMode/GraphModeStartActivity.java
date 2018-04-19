package com.androb.androidrobot.graphMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.androb.androidrobot.R;
import com.androb.androidrobot.codeMode.CodeModeQuestionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/28.
 */

public class GraphModeStartActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.graph_ques_1)
    Button _graphQues1;
    @BindView(R.id.graph_ques_2)
    Button _graphQues2;

    private Intent mIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_mode_start_layout);

        ButterKnife.bind(this);

        mIntent = new Intent(getApplicationContext(), GraphModeQuestionActivity.class);

        _graphQues1.setOnClickListener(this);
        _graphQues2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.graph_ques_1:
                // 请求码的值随便设置，但必须>=0
                mIntent.putExtra("btn_id", "1");
                System.out.println("clicked 1 in Start");
                startActivity(mIntent);
                break;
            case R.id.graph_ques_2:
                mIntent.putExtra("btn_id", "2");
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }
}
