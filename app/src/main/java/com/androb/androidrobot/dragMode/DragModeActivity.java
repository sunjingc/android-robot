package com.androb.androidrobot.dragMode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androb.androidrobot.R;
import com.androb.androidrobot.dragAndDrop.AnswerRange;
import com.androb.androidrobot.dragAndDrop.DragFillBlankView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/25.
 */

public class DragModeActivity extends AppCompatActivity {

    @BindView(R.id.dragMode_content)
    DragFillBlankView dragModeContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_mode_general_layout);
        ButterKnife.bind(this);

        initData();
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

        dragModeContent.setData(content, optionList, rangeList);
    }

}
