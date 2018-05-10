package com.androb.androidrobot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androb.androidrobot.connectionUtil.BluetoothDiscoverActivity;


import com.androb.androidrobot.dragMode.DragModeStartActivity;
import com.androb.androidrobot.graphMode.GraphModeStartActivity;
import com.androb.androidrobot.codeMode.CodeModeStartActivity;
import com.androb.androidrobot.userManagement.ProfileActivity;
import com.androb.androidrobot.userManagement.SharedUserManager;
import com.androb.androidrobot.userManagement.StartActivity;
import com.androb.androidrobot.userManagement.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/20.
 */

public class CollegeStuMainActivity extends AppCompatActivity {

    @BindView(R.id.codeMode_btn)
    Button _codeModeButton;
    @BindView(R.id.graphMode_btn)
    Button _graphModeButton;
    @BindView(R.id.dragMode_btn)
    Button _dragModeButton;
    @BindView(R.id.bluetooth_btn)
    Button _bluetoothButton;
    @BindView(R.id.profile_link)
    TextView _profileLink;
    @BindView(R.id.login_link)
    TextView _loginLink;

    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collegestumain_layout);

        ButterKnife.bind(this);

        _dragModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DragModeStartActivity.class);
//                Intent intent = new Intent(getApplicationContext(), DragTestActivity.class);
                startActivity(intent);
            }
        });
        _graphModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GraphModeStartActivity.class);
                startActivity(intent);
            }
        });
        _codeModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CodeModeStartActivity.class);
                startActivity(intent);
            }
        });

        _bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BluetoothDiscoverActivity.class);
                startActivity(intent);
            }
        });

        _profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
            }
        });

        if(SharedUserManager.getInstance(this).isLoggedIn()) {
            curUser = SharedUserManager.getInstance(this).getUser();
        }

        if(curUser == null) {
            _profileLink.setVisibility(View.INVISIBLE);
            _loginLink.setVisibility(View.VISIBLE);
        }
        else {
            _profileLink.setVisibility(View.VISIBLE);
            _loginLink.setVisibility(View.INVISIBLE);
        }

    }

}
