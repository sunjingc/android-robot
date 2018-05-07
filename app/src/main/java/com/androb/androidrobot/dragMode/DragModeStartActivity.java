package com.androb.androidrobot.dragMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.dragAndDrop.AnswerRange;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/25.
 */

public class DragModeStartActivity extends AppCompatActivity implements View.OnClickListener {

//    @BindView(R.id.dragMode_content)
//    DragFillBlankView dragModeContent;

    @BindView(R.id.drag_ques_1)
    Button _dragQues1;
    @BindView(R.id.drag_ques_2)
    Button _dragQues2;
    @BindView(R.id.drag_ques_3)
    Button _dragQues3;
    @BindView(R.id.drag_ques_4)
    Button _dragQues4;

//    private TitleBar mTitleBar;
//    private Select_1Blank select_1btn;

    private Spinner sp;
    private List<String> list, list_test;

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drag_mode_start_layout);

        ButterKnife.bind(this);

        mIntent = new Intent(getApplicationContext(), DragModeQuestionActivity.class);

        _dragQues1.setOnClickListener(this);
        _dragQues2.setOnClickListener(this);
        _dragQues3.setOnClickListener(this);
        _dragQues4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        System.out.println("in DragStart onClick");
        switch (v.getId()) {
            case R.id.drag_ques_1:
                // 请求码的值随便设置，但必须>=0
                mIntent.putExtra("btn_id", "1");
                startActivity(mIntent);
                break;
            case R.id.drag_ques_2:
                mIntent.putExtra("btn_id", "2");
                startActivity(mIntent);
                break;
            case R.id.drag_ques_3:
                mIntent.putExtra("btn_id", "3");
                startActivity(mIntent);
                break;
            case R.id.drag_ques_4:
                mIntent.putExtra("btn_id", "4");
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }


    private void initData() {
        String content = "Starting point";

        // 选项集合
        List<String> optionList = new ArrayList<>();
        optionList.add("go");
        optionList.add("loop");
        optionList.add("repeat");
        optionList.add("sing");
        optionList.add("turn left");
        optionList.add("turn right");

        // 答案范围集合
        List<AnswerRange> rangeList = new ArrayList<>();

        // word count
        rangeList.add(new AnswerRange(5, 13));
        rangeList.add(new AnswerRange(23, 31));
        rangeList.add(new AnswerRange(38, 46));

//        dragModeContent.setData(content, optionList, rangeList);
    }

}
