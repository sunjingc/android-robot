package com.androb.androidrobot.userManagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.models.User;
import com.androb.androidrobot.utils.userUtil.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/05/10.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.profile_name_text)
    TextView nameText;
    @BindView(R.id.profile_score_text)
    TextView scoreText;
    @BindView(R.id.profile_level_text)
    TextView levelText;
    @BindView(R.id.profile_logout_btn)
    Button logoutBtn;
    @BindView(R.id.star_1)
    ImageView star1;
    @BindView(R.id.star_2)
    ImageView star2;
    @BindView(R.id.star_3)
    ImageView star3;

    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        ButterKnife.bind(this);

        star1.setVisibility(View.INVISIBLE);
        star2.setVisibility(View.INVISIBLE);
        star3.setVisibility(View.INVISIBLE);

        user = UserManager.getInstance(this).getUser();

        SharedPreferences sharedPref = this.getSharedPreferences("sharedUserPref", Context.MODE_PRIVATE);
        int score = Integer.valueOf(sharedPref.getString("score", null));

        nameText.setText(sharedPref.getString("username", null));
        scoreText.setText(score + "");

        if(score > 100) {
            levelText.setText("Level 3");
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.VISIBLE);
        }
        else if(score > 50 && score <= 100) {
            levelText.setText("Level 2");
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
        }
        else if(score >= 0 && score <= 50) {
            levelText.setText("Level 1");
            star2.setVisibility(View.VISIBLE);
        }

        logoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.profile_logout_btn:
                UserManager.getInstance(this).logout();
                Toast.makeText(this, "logged out", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
