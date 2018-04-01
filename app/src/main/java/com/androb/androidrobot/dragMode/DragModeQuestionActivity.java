package com.androb.androidrobot.dragMode;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.dragAndDrop.AnswerRange;
import com.androb.androidrobot.dragAndDrop.DragFillBlankView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/30.
 */

public class DragModeQuestionActivity extends AppCompatActivity {

    @BindView(R.id.btn_forward)
    Button btnForward;
    @BindView(R.id.btn_backward)
    Button btnBackward;

    // 控件上一次所处的坐标
    private float lastX = 0;
    private float lastY = 0;
    // 屏幕的宽度和高度
    private int screenWidth;
    private int screenHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_mode_question_layout);

        ButterKnife.bind(this);

        initView();

    }

    private void initView() {
        // 获取屏幕的宽度和高度
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();


        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(DragModeQuestionActivity.this, "hello click", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        btnForward.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //获取点击时x y 轴的数据
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("test", "up" + event.getX() + ":" + event.getY());
                        //触摸弹起的时候来一个小动画
                        startAnimation();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //在move中直接把得到的坐标设置为控件的坐标..果然天真单纯
                        // v.setX(event.getX());
                        // v.setY(event.getY());


                        //获得x y轴的偏移量
                        int dx = (int) (event.getRawX() - lastX);
                        int dy = (int) (event.getRawY() - lastY);

                        //获得控件上下左右的位置信息,加上我们的偏移量,新得到的位置就是我们
                        //控件将要出现的位置
                        int l = v.getLeft() + dx;
                        int b = v.getBottom() + dy;
                        int r = v.getRight() + dx;
                        int t = v.getTop() + dy;

                        //判断四个实际位置,如果有一边已经划出屏幕,那就把这边位置设置为0
                        //然后相反的边的位置就设置成控件的高度或者宽度
                        if (l < 0) {
                            l = 0;
                            r = l + v.getWidth();
                        }

                        if (t < 0) {
                            t = 0;
                            b = t + v.getHeight();

                        }

                        if (r > screenWidth) {
                            r = screenWidth;
                            l = r - v.getWidth();
                        }

                        if (b > screenHeight) {
                            b = screenHeight;
                            l = b - v.getHeight();
                        }
                        //然后使用我们view的layout重新在布局中把我们的控件画出来
                        v.layout(l, t, r, b);
                        //并把现在的x y设置给lastx lasty
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        v.postInvalidate();//绘画

                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    public void startAnimation() {
        //先获取当前控件的x坐标
        //然后向右五次,向左一次回来
        float x = btnForward.getX();
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(btnForward,
                "X", x-100,x+100);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(btnForward,
                "X", x+100,x);
        objectAnimator1.setRepeatCount(4);
        objectAnimator2.setRepeatCount(0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator2).after(objectAnimator1);
        animatorSet.setDuration(100);
        animatorSet.start();
    }

}