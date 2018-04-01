package com.androb.androidrobot.graphMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androb.androidrobot.R;

import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/30.
 */

public class GraphModeQuestionActivity extends AppCompatActivity {

    private Intent mIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_mode_question_layout);

        ButterKnife.bind(this);

    }

}
