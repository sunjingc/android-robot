package com.androb.androidrobot.graphMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androb.androidrobot.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/30.
 */

public class GraphModeQuestionOLDActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.graph_ques_submit_btn)
    Button submitButton;
    @BindView(R.id.graph_question_text)
    TextView graphQuestionText;

    @BindView(R.id.graph_input_1_1)
    EditText g_blank_1_1;
    @BindView(R.id.graph_input_1_2)
    EditText g_blank_1_2;
    @BindView(R.id.graph_input_1_3)
    EditText g_blank_1_3;

    @BindView(R.id.graph_imageView)
    ImageView graph_image;

    private Intent mIntent;
    private int questionId = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_mode_question_layout);

        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        questionId = Integer.parseInt(intent.getStringExtra("btn_id"));

        submitButton.setOnClickListener(this);

        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) g_blank_1_1.getLayoutParams();
        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) g_blank_1_2.getLayoutParams();
        FrameLayout.LayoutParams params3 = (FrameLayout.LayoutParams) g_blank_1_3.getLayoutParams();

        ViewGroup.LayoutParams image_params = (ViewGroup.LayoutParams) graph_image.getLayoutParams();

        switch (questionId) {
            case 1:
                graphQuestionText.setText(R.string.g_ques_1);
                graph_image.setImageResource(R.drawable.g1);

                image_params.width = 800;
                image_params.height = 1050;

                params1.setMargins(50, 250, 0, 0);

                params2.setMargins(50, 470, 0, 0);

                params3.setMargins(50, 690, 0, 0);
                break;
            case 2:
                graphQuestionText.setText(R.string.g_ques_2);
                graph_image.setImageResource(R.drawable.g1);

                image_params.width = 800;
                image_params.height = 1050;

                params1.setMargins(50, 250, 0, 0);

                params2.setMargins(50, 470, 0, 0);

                params3.setMargins(50, 690, 0, 0);
                break;
            case 3:
                System.out.println("in graphodeQuestion 3");
                graphQuestionText.setText(R.string.g_ques_3);
                graph_image.setImageResource(R.drawable.g3);

                image_params.width = 800;
                image_params.height = 1050;

                params1.setMargins(50, 250, 0, 0);

                params2.setMargins(50, 470, 0, 0);

                params3.setMargins(50, 690, 0, 0);
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.graph_ques_submit_btn:
                System.out.println("clicked submit");

                // 检查输入格式？还是不在这里做？？

                break;
        }
    }
}
