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

//        mTitleBar = (TitleBar) this.findViewById(R.id.title);
//
//        select_1btn = (Select_1Blank) this.findViewById(R.id.select_1btn_1);
//
//        list = new ArrayList<String>();
//        list.add("1s");
//        list.add("2s");
//        list.add("3s");
//        list.add("4s");
//        list.add("5s");
//        sp = (Spinner) findViewById(R.id.select_1_blank_spinner);
//
//
//        // TODO: 动态生成自定义控件？？id怎么办
////        list_test = new ArrayList<String>();
////        list_test.add("15°");
////        list_test.add("30°");
////        list_test.add("45°");
////        list_test.add("60°");
////        list_test.add("75°");
////        list_test.add("90°");
////        list_test.add("105°");
////        list_test.add("120°");
////        list_test.add("135°");
////        list_test.add("150°");
////        list_test.add("165°");
////        list_test.add("180°");
////        sp = (Spinner) findViewById(R.id.select_1btn_test_spinner);
//
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp.setAdapter(adapter);
//
//
//        mTitleBar.setLeftListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DragModeStartActivity.this, "点击左键", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mTitleBar.setRightListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DragModeStartActivity.this, "点击右键", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        ButterKnife.bind(this);
//
//        System.out.println("in onCreate");

//        initData();
    }

    @Override
    public void onClick(View v) {
        System.out.println("in DragStart onClick");
        switch (v.getId()) {
            case R.id.drag_ques_1:
                // 请求码的值随便设置，但必须>=0
                mIntent.putExtra("btn_id", "1");
                System.out.println("clicked 1 in DragStart");
                startActivity(mIntent);
                break;
            case R.id.drag_ques_2:
                mIntent.putExtra("btn_id", "2");
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
