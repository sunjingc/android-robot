package com.androb.androidrobot.dragMode;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androb.androidrobot.R;
import com.androb.androidrobot.dragAndDrop.AnswerRange;
import com.androb.androidrobot.dragAndDrop.DragFillBlankView;
import com.androb.androidrobot.dragAndDrop.TouchLinkMovementMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaki on 2018/03/25.
 */

public class DragModeView extends RelativeLayout implements View.OnDragListener,
        View.OnLongClickListener {

    private TextView startTextContent;
    private ListView dragOptionList;

    // 初始数据
    private String originContent;
    // 初始答案范围集合
    private List<AnswerRange> originAnswerRangeList;
    // 填空题内容
    private SpannableStringBuilder content;
    // 选项列表
    private List<String> optionList;
    // 答案范围集合
    private List<AnswerRange> answerRangeList;
    // 答案集合
    private List<String> answerList;
    // 选项位置
    private int optionPosition;
    // 一次拖拽填空是否完成
    private boolean isFillBlank;



    public DragModeView(Context context) {
        super(context);
    }

    private void initView() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.drag_mode_detailed_layout, this);

        startTextContent = (TextView) findViewById(R.id.drag_start_text);
        dragOptionList = (ListView) findViewById(R.id.drag_option_list);
    }


    /**
     * 设置数据
     *
     * @param originContent   源数据
     * @param optionList      选项列表
     * @param answerRangeList 答案范围集合
     */
    public void setData(String originContent, List<String> optionList, List<AnswerRange> answerRangeList) {

        System.out.println("in DragMode setData");

        if (TextUtils.isEmpty(originContent) || optionList == null || optionList.isEmpty()
                || answerRangeList == null || answerRangeList.isEmpty()) {
            return;
        }

        // 初始数据
        this.originContent = originContent;
        // 初始答案范围集合
        this.originAnswerRangeList = new ArrayList<>();
        this.originAnswerRangeList.addAll(answerRangeList);
        // 获取课文内容
        this.content = new SpannableStringBuilder(originContent);
        // 选项列表
        this.optionList = optionList;
        // 答案范围集合
        this.answerRangeList = answerRangeList;

        // 避免重复创建拖拽选项
        if (dragOptionList.getChildCount() < 1) {
            // 拖拽选项列表
            List<Button> itemList = new ArrayList<>();
            for (String option : optionList) {
                Button btnAnswer = new Button(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, dp2px(10), 0);
                btnAnswer.setLayoutParams(params);
//                btnAnswer.setBackgroundColor(Color.parseColor("#4DB6AC"));
                btnAnswer.setBackgroundColor(Color.parseColor("#e67300"));
                btnAnswer.setTextColor(Color.WHITE);
                btnAnswer.setText(option);
                btnAnswer.setOnLongClickListener(this);
                itemList.add(btnAnswer);
            }

            // 显示拖拽选项
            for (int i = 0; i < itemList.size(); i++) {
                dragOptionList.addView(itemList.get(i));
            }
        } else {
            // 不显示已经填空的选项
            for (int i = 0; i < dragOptionList.getChildCount(); i++) {
                Button button = (Button) dragOptionList.getChildAt(i);
                String option = button.getText().toString();
                if (!answerList.isEmpty() && answerList.contains(option)) {
                    button.setVisibility(INVISIBLE);
                } else {
                    button.setVisibility(VISIBLE);
                }
            }
        }

        // 设置下划线颜色
        for (AnswerRange range : this.answerRangeList) {

            System.out.println("range start here: " + range.start + ", end: " + range.end);

            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#e67300"));
            content.setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 答案集合
        answerList = new ArrayList<>();
        for (int i = 0; i < answerRangeList.size(); i++) {
            answerList.add("");
        }

        // 设置填空处点击事件
//        for (int i = 0; i < this.answerRangeList.size(); i++) {
//            AnswerRange range = this.answerRangeList.get(i);
//            DragFillBlankView.BlankClickableSpan blankClickableSpan = new DragFillBlankView.BlankClickableSpan(i);
//            content.setSpan(blankClickableSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }

        // 填空处设置触摸事件
        startTextContent.setMovementMethod(new TouchLinkMovementMethod());
        startTextContent.setText(content);
        startTextContent.setOnDragListener(this);
    }



    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
