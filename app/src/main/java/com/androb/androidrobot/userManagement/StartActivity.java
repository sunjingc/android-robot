package com.androb.androidrobot.userManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androb.androidrobot.CollegeStuMainActivity;
import com.androb.androidrobot.R;
import com.androb.androidrobot.codeMode.CodeModeStartActivity;
import com.androb.androidrobot.connectionUtil.BluetoothDiscoverActivity;
import com.androb.androidrobot.dragMode.DragModeStartActivity;
import com.androb.androidrobot.graphMode.GraphModeStartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/05/09.
 */

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.start_login_btn)
    Button _loginButton;
    @BindView(R.id.start_register_btn)
    Button _registerButton;
    @BindView(R.id.guest_link)
    TextView _guestLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(this);
        _registerButton.setOnClickListener(this);
        _guestLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.start_login_btn:
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.start_register_btn:
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.guest_link:
                Intent guestIntent = new Intent(getApplicationContext(), CollegeStuMainActivity.class);
                startActivity(guestIntent);
                break;
        }

    }
}
