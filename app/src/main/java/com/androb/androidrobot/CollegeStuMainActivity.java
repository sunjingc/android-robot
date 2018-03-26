package com.androb.androidrobot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.androb.androidrobot.dragAndDrop.DragActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/20.
 */

public class CollegeStuMainActivity extends AppCompatActivity {

    @BindView(R.id.progMode_btn)
    Button _progModeButton;
    @BindView(R.id.dragMode_btn)
    Button _dragModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collegestumain_layout);

        ButterKnife.bind(this);

        _dragModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DragActivity.class);
                startActivity(intent);
            }
        });
    }

}
