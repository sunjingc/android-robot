package com.androb.androidrobot.dragMode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.dragAndDrop.AnswerRange;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/25.
 */

public class DragModeStartActivity extends AppCompatActivity {

//    @BindView(R.id.dragMode_content)
//    DragFillBlankView dragModeContent;
    private TitleBar mTitleBar;
    private Select_1Blank select_1btn;

    private Spinner sp;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_mode_general_layout);

        mTitleBar = (TitleBar) this.findViewById(R.id.title);

        select_1btn = (Select_1Blank) this.findViewById(R.id.select_1btn_1);

        list = new ArrayList<String>();
        list.add("1s");
        list.add("2s");
        list.add("3s");
        list.add("4s");
        list.add("5s");
        sp = (Spinner) findViewById(R.id.select_1_blank_spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);


        mTitleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DragModeStartActivity.this, "点击左键", Toast.LENGTH_SHORT).show();
            }
        });

        mTitleBar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DragModeStartActivity.this, "点击右键", Toast.LENGTH_SHORT).show();
            }
        });

        ButterKnife.bind(this);

        System.out.println("in onCreate");

//        initData();
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
