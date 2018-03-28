package com.androb.androidrobot.dragMode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androb.androidrobot.R;

/**
 * Created by kaki on 2017/03/27.
 */

public class Select_1Blank extends RelativeLayout {

    private int defaultBgColor= Color.BLUE;
    private int textColor = Color.WHITE;

    private RelativeLayout select_1_blank_rootlayout;
    private TextView select_item_text;
    private Spinner select_spinner;
    private String titlename;


    public Select_1Blank(Context context) {
        super(context);
        initView(context);
    }

    public Select_1Blank(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        initView(context);
    }

    public Select_1Blank(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        initView(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.Select_1Blank);
        defaultBgColor = mTypedArray.getColor(R.styleable.Select_1Blank_bg_color, Color.BLUE);
        textColor = mTypedArray.getColor(R.styleable.Select_1Blank_text_color, Color.WHITE);
        titlename = mTypedArray.getString(R.styleable.Select_1Blank_select_text);
        //获取资源后要及时回收
        mTypedArray.recycle();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.select_1_blank, this, true);
        select_item_text = (TextView) findViewById(R.id.select_1_blank_text);
        select_spinner = (Spinner) findViewById(R.id.select_1_blank_spinner);
        select_1_blank_rootlayout = (RelativeLayout) findViewById(R.id.select_1_blank_rootlayout);
        //设置背景颜色
        select_1_blank_rootlayout.setBackgroundColor(defaultBgColor);
        //设置标题文字颜色
        select_item_text.setTextColor(textColor);
//        select_spinner.set
        setTitle(titlename);
    }

    public void setTitle(String titlename) {
        if (!TextUtils.isEmpty(titlename)) {
            select_item_text.setText(titlename);
        }
    }

}
