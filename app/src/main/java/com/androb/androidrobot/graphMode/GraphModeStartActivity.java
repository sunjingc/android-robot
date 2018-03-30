package com.androb.androidrobot.graphMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androb.androidrobot.R;
import com.androb.androidrobot.codeMode.CodeModeQuestionActivity;

import butterknife.ButterKnife;

/**
 * Created by kaki on 2017/03/28.
 */

public class GraphModeStartActivity extends AppCompatActivity {

    private Intent mIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_mode_start_layout);

        ButterKnife.bind(this);

        System.out.println("in CodeMode onCreate");

    }

}
