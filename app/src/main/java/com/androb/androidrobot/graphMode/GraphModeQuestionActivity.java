package com.androb.androidrobot.graphMode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.messageUtil.MessageService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kaki on 2018/04/25.
 */

public class GraphModeQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.graph_ques_submit_btn)
    Button submitButton;
    @BindView(R.id.graph_ques_cancel_btn)
    Button cancelButton;
    @BindView(R.id.graph_question_text)
    TextView graphQuestionText;

    @BindView(R.id.graph_area)
    RelativeLayout graphArea;

    @BindView(R.id.graph_btn_start)
    AppCompatButton btn_start;
    @BindView(R.id.graph_btn_end)
    AppCompatButton btn_end;
    @BindView(R.id.graph_btn_order)
    AppCompatButton btn_order;
    @BindView(R.id.graph_btn_if)
    AppCompatButton btn_if;

    @BindView(R.id.help_btn)
    ImageButton btn_help;

    private Intent mIntent;
    private int questionId = 0;

    private int orderCount = 0;

    // 记录点击动作 → 生成图形的顺序
    private ArrayList<String> actionHistory = new ArrayList<String>();
    private int ptr = -1;
//    private int etPtr = -1;
    private final int marginSpace = 200;
    private final int lineLength = 100;

    private HashMap<Integer, ImageView> imagesMap = new HashMap<>();
    private HashMap<Integer, EditText> etMap = new HashMap<>();
    private HashMap<Integer, LinearLayout> lineMap = new HashMap<>();
    private LinearLayout[] ifLines = new LinearLayout[3];

    private int ifTopMargin = 0;
    private int endTopMargin = 0;
    private int ifRightSide = 0;
    private int endRightSide = 0;


    private List<String> answerStrList = new ArrayList<>();
    private String answerStr;

    private AlertDialog.Builder builder;

    private String jsonResult;
    private JSONReceiver receiver;


    // for drawing lines
    private Paint paint = new Paint();


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_mode_question_layout);

        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        questionId = Integer.parseInt(intent.getStringExtra("btn_id"));

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_end.setOnClickListener(this);
        btn_if.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        btn_if.setOnClickListener(this);

        btn_help.setOnClickListener(this);

        graphArea = (RelativeLayout) findViewById(R.id.graph_area);

        switch (questionId) {
            case 1:
                graphQuestionText.setText(R.string.g_ques_1);

                break;
            case 2:
                graphQuestionText.setText(R.string.g_ques_2);

                break;
            case 3:
                graphQuestionText.setText(R.string.g_ques_3);

                break;
            case 4:
                graphQuestionText.setText(R.string.g_ques_4);

                break;
            default:
                break;
        }

        receiver = new JSONReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.androb.androidrobot.messageUtil.MessageService");
        registerReceiver(receiver, filter);

    }


    @Override
    public void onClick(View view) {

        ViewGroup.LayoutParams image_params;

        switch (view.getId()){

            case R.id.help_btn:
                this.showHelpDialog(view);
                break;

            case R.id.graph_btn_start:

                int startX = 0;
                int startY = 0;

                if (ptr != -1) {
                    Toast.makeText(GraphModeQuestionActivity.this, "现在不能start了哦", Toast.LENGTH_SHORT).show();
                }
                else {
                    actionHistory.add("start");
                    ptr++;

                    System.out.println("clicked start");

                    ImageView imageView = new ImageView(this);

                    imageView.setLayoutParams(new LinearLayout.LayoutParams(240, 240));  //设置图片宽高
                    imageView.setImageResource(R.drawable.new_start); //图片资源

                    ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(imageView.getLayoutParams());
                    margin.leftMargin = 450;
                    margin.topMargin = 5;
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
                    layoutParams.height = 180;//设置图片的高度
                    layoutParams.width = 180; //设置图片的宽度
                    imageView.setLayoutParams(layoutParams);

                    imageView.setId(R.id.graph_start_img);

                    imagesMap.put(ptr, imageView);

                    graphArea.addView(imageView); //动态添加图片

                    startX = margin.leftMargin + layoutParams.width/2;
                    startY = margin.topMargin + layoutParams.height;

                }

                System.out.println("in start   X: " + startX + ",    Y: " + startY);

                this.drawVerticalLine(startX, startY, lineLength, ptr);

                break;

            case R.id.graph_btn_order:

                int orderX = 0;
                int orderY = 0;

                if (ptr == -1 || orderCount > 2) {
                    Toast.makeText(GraphModeQuestionActivity.this, "现在不能order哦", Toast.LENGTH_SHORT).show();
                }
                else {
                    actionHistory.add("order");
                    ptr++;

                    // 现在最多支持三个order
                    orderCount++;

                    System.out.println("clicked order");

                    ImageView orderView = new ImageView(this);

                    orderView.setLayoutParams(new LinearLayout.LayoutParams(200, 40));  //设置图片宽高
                    orderView.setImageResource(R.drawable.new_order); //图片资源

                    ViewGroup.MarginLayoutParams orderMargin = new ViewGroup.MarginLayoutParams(orderView.getLayoutParams());
                    orderMargin.leftMargin = 290;


                    // EditText
                    EditText et_order = new EditText(GraphModeQuestionActivity.this);
                    ViewGroup.MarginLayoutParams orderEtMargin = new ViewGroup.MarginLayoutParams(orderView.getLayoutParams());
                    orderEtMargin.leftMargin = 280;

                    int tempPtr = ptr - 1;
                    if (actionHistory.get(tempPtr).equals("if")) {
                        System.out.println("这个order前面是if");
                        orderMargin.topMargin = marginSpace * ptr + 70;
                        orderEtMargin.topMargin = marginSpace * ptr + 70;
                    }
                    else {
                        orderMargin.topMargin = marginSpace * ptr + 50;
                        orderEtMargin.topMargin = marginSpace * ptr + 50;
                    }

                    RelativeLayout.LayoutParams orderLayoutParams = new RelativeLayout.LayoutParams(orderMargin);
                    orderLayoutParams.height = 140;//设置图片的高度
                    orderLayoutParams.width = 500; //设置图片的宽度
                    orderView.setLayoutParams(orderLayoutParams);

                    int prePtr = ptr - 1;

                    if(actionHistory.get(prePtr).equals("start") || actionHistory.get(prePtr).equals("if")) {
                        System.out.println("order & start or if");

                        if(actionHistory.get(prePtr).equals("start")) {
                            System.out.println("order & start");
                            switch(ptr) {
                                case 1:
                                    orderView.setId(R.id.graph_order_img_1);
                                    et_order.setId(R.id.graph_order_blank_1);
                                    break;
                                case 2:
                                    orderView.setId(R.id.graph_order_img_2);
                                    et_order.setId(R.id.graph_order_blank_2);
                                    break;
                                case 3:
                                    orderView.setId(R.id.graph_order_img_3);
                                    et_order.setId(R.id.graph_order_blank_3);
                                    break;
                            }
                        }
                        else if(actionHistory.get(prePtr).equals("if")) {
                            System.out.println("order & if");
                            switch(ptr) {
                                case 2:
                                    orderView.setId(R.id.graph_order_img_1);
                                    et_order.setId(R.id.graph_order_blank_1);
                                    break;
                                case 3:
                                    orderView.setId(R.id.graph_order_img_2);
                                    et_order.setId(R.id.graph_order_blank_2);
                                    break;
                                case 4:
                                    orderView.setId(R.id.graph_order_img_3);
                                    et_order.setId(R.id.graph_order_blank_3);
                                    break;
                            }
                        }

                        orderX = orderEtMargin.leftMargin + orderLayoutParams.width/2;
                        orderY = orderEtMargin.topMargin + orderLayoutParams.height;

                    }
                    else {
                        // 前一个也是order
                        int tempOrderId = imagesMap.get(prePtr).getId();
                        System.out.println("前一个orderId: " + tempOrderId);
                        orderView.setId(++tempOrderId);

                        // EditText
                        int tempEtId = etMap.get(prePtr).getId();
                        System.out.println("前一个order的ET的id: " + tempEtId);
                        et_order.setId(++tempEtId);

                        orderX = orderEtMargin.leftMargin + orderLayoutParams.width/2;
                        orderY = orderEtMargin.topMargin + orderLayoutParams.height;
                    }

                    imagesMap.put(ptr, orderView);
                    etMap.put(ptr, et_order);

                    et_order.setLayoutParams(new LinearLayout.LayoutParams(200, 40));  //设置图片宽高

                    RelativeLayout.LayoutParams orderEtLayoutParams = new RelativeLayout.LayoutParams(orderEtMargin);
                    orderEtLayoutParams.height = 140;//设置图片的高度
                    orderEtLayoutParams.width = 500; //设置图片的宽度
                    et_order.setLayoutParams(orderEtLayoutParams);

                    graphArea.addView(orderView); //动态添加图片
                    graphArea.addView(et_order);


//                    int previousPtr = ptr - 1;

//                    ImageView tempImgView = imagesMap.get(previousPtr);

//                    tempImgView.getLayoutParams();

//                    System.out.println("in order    X: " + orderX + ",    Y: " + orderY);

                    this.drawVerticalLine(orderX, orderY, lineLength, ptr);

                }

                break;

            case R.id.graph_btn_if:

                int ifX = 0;
                int ifY = 0;

                if (ptr != 0) {
                    Toast.makeText(GraphModeQuestionActivity.this, "现在if只能在start之后哦", Toast.LENGTH_SHORT).show();
                }
                else {
                    actionHistory.add("if");
                    ptr++;

                    System.out.println("clicked if");

                    ImageView ifView = new ImageView(this);

                    ifView.setLayoutParams(new LinearLayout.LayoutParams(240, 240));  //设置图片宽高
                    ifView.setImageResource(R.drawable.new_if); //图片资源

                    ViewGroup.MarginLayoutParams ifMargin = new ViewGroup.MarginLayoutParams(ifView.getLayoutParams());
                    ifMargin.leftMargin = 330;
                    ifMargin.topMargin = marginSpace * ptr;

                    RelativeLayout.LayoutParams ifLayoutParams = new RelativeLayout.LayoutParams(ifMargin);
                    ifLayoutParams.height = 250;//设置图片的高度
                    ifLayoutParams.width = 400; //设置图片的宽度
                    ifView.setLayoutParams(ifLayoutParams);

                    // 记录if的topMargin
                    ifTopMargin = ifMargin.topMargin + ifLayoutParams.height / 2;
                    ifRightSide = ifMargin.leftMargin + ifLayoutParams.width;

                    ifView.setId(R.id.graph_if_img);

                    imagesMap.put(ptr, ifView);

                    // EditText
                    EditText et_if = new EditText(GraphModeQuestionActivity.this);

                    ViewGroup.MarginLayoutParams ifEtMargin = new ViewGroup.MarginLayoutParams(ifView.getLayoutParams());
                    ifEtMargin.leftMargin = 370;
                    ifEtMargin.topMargin = marginSpace * ptr - 40;

                    RelativeLayout.LayoutParams ifEtLayoutParams = new RelativeLayout.LayoutParams(ifEtMargin);
                    ifEtLayoutParams.width = 300; //设置图片的宽度

                    et_if.setLayoutParams(ifEtLayoutParams);
                    et_if.setId(R.id.graph_if_blank);

                    etMap.put(ptr, et_if);

                    graphArea.addView(ifView); //动态添加图片
                    graphArea.addView(et_if);

                    ifX = ifMargin.leftMargin + ifLayoutParams.width/2;
                    ifY = ifMargin.topMargin + ifLayoutParams.height;

                }

                System.out.println("in if    X: " + ifX + ",    Y: " + ifY);

                this.drawVerticalLine(ifX, ifY, lineLength, ptr);

                break;

            case R.id.graph_btn_end:

                if (ptr < 1) {
                    Toast.makeText(GraphModeQuestionActivity.this, "现在还不能end哦", Toast.LENGTH_SHORT).show();
                }
                else {
                    actionHistory.add("end");
                    ptr++;

                    System.out.println("clicked end");

                    ImageView endView = new ImageView(this);

                    endView.setLayoutParams(new LinearLayout.LayoutParams(240, 240));  //设置图片宽高
                    endView.setImageResource(R.drawable.new_end); //图片资源

                    ViewGroup.MarginLayoutParams endMargin = new ViewGroup.MarginLayoutParams(endView.getLayoutParams());
                    endMargin.leftMargin = 450;
                    endMargin.topMargin = marginSpace * ptr + 40;

                    RelativeLayout.LayoutParams endLayoutParams = new RelativeLayout.LayoutParams(endMargin);
                    endLayoutParams.height = 180;//设置图片的高度
                    endLayoutParams.width = 180; //设置图片的宽度
                    endView.setLayoutParams(endLayoutParams);

                    // 记录if的topMargin
                    endTopMargin = endMargin.topMargin + endLayoutParams.height / 2;
                    endRightSide = endMargin.leftMargin + endLayoutParams.width;

                    endView.setId(R.id.graph_end_img);

                    imagesMap.put(ptr, endView);

                    graphArea.addView(endView); //动态添加图片

                    if(hasIf()) {
                        // int upperLeftMargin, int lowerLeftMargin, int upperTopMargin, int lowerTopMargin, int horizontalWidth
                        this.drawIfLine(ifRightSide, endRightSide, ifTopMargin, endTopMargin, 400);
                    }

                }

                break;

            case R.id.graph_ques_cancel_btn:
                System.out.println("点击撤回一步");

                if(ptr >= 0) {
                    int removePtr = ptr;
                    ImageView tempView = imagesMap.get(removePtr);

                    // EditText
                    EditText tempEt = etMap.get(removePtr);

                    // line
                    LinearLayout tempLine = lineMap.get(removePtr);

                    int tempId = tempView.getId();
                    System.out.println("tempId: " + tempId);

                    System.out.println("这个控件id是否等于graph_order_img_1: " + (tempId == R.id.graph_order_img_1));
                    if(tempId == R.id.graph_order_img_1 || tempId == R.id.graph_order_img_2 || tempId == R.id.graph_order_img_3) {
                        orderCount--;
                        System.out.println("撤回中 orderCount: " + orderCount);
                    }

                    graphArea.removeView(tempView);
                    graphArea.removeView(tempEt);
                    graphArea.removeView(tempLine);

                    etMap.remove(removePtr);
                    imagesMap.remove(removePtr);
                    lineMap.remove(removePtr);

                    ptr--;
                }
                else {
                    Toast.makeText(GraphModeQuestionActivity.this, "没东西删什么啊", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.graph_ques_submit_btn:

                if(isStepsValid()) {
                    answerStrList = new ArrayList<>();
                    answerStr = "";
                    int etCount = etMap.size();
                    System.out.println("etCount: " + etCount);
                    for(int key : etMap.keySet()) {
                        String tempAns = etMap.get(key).getText().toString();
                        System.out.println("tempAns: " + tempAns);
                        answerStrList.add(tempAns);

                    }
                    answerStr = answerStrList.toString();
                    Toast.makeText(getApplicationContext(), answerStr, Toast.LENGTH_SHORT).show();

//                    System.out.println("in GraphMode, checkInput: " + this.checkInput());
                    boolean isInputValid = this.checkInput();
                    boolean isImgValid = this.checkImgValid();

                    if(isInputValid == false) {
                        Toast.makeText(GraphModeQuestionActivity.this, "好像哪里输入有错", Toast.LENGTH_SHORT).show();
                    }
                    else if(isImgValid == false) {
                        Toast.makeText(GraphModeQuestionActivity.this, "好像步骤有错哦", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // answerStr
                        // quesType
                        // quesId
                        Intent msgIntent = new Intent(this, MessageService.class);
                        msgIntent.putExtra("answerStr", answerStr);
                        msgIntent.putExtra("quesType", "graph");
                        msgIntent.putExtra("quesId", questionId + "");
                        startService(msgIntent);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "步骤不太对啊", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    // 检查步骤是否valid
    private boolean isStepsValid() {

        if(this.testStartImg() && this.testEndImg()) {
            return true;
        }
        else {
            return false;
        }

    }

    private boolean testStartImg() {
        if(findViewById(R.id.graph_start_img) != null) {
            System.out.println("graphQuesMode: start exists");
            return true;
        }
        else {
            System.out.println("graphQuesMode: start doesn't exist");
            return false;
        }
    }

    private boolean testEndImg() {
        if(findViewById(R.id.graph_end_img) != null) {
            System.out.println("graphQuesMode: end exists");
            return true;
        }
        else {
            System.out.println("graphQuesMode: end doesn't exist");
            return false;
        }
    }

    private boolean hasIf() {
        if(findViewById(R.id.graph_if_img) != null) {
            System.out.println("graphQuesMode: if exists");
            return true;
        }
        else {
            System.out.println("graphQuesMode: if doesn't exist");
            return false;
        }
    }

    private void showHelpDialog(View view) {
        builder=new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("流程图模式帮助");
        builder.setMessage(R.string.graph_help_msg);

        //监听下方button点击事件
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "那就加油吧", Toast.LENGTH_SHORT).show();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void drawVerticalLine(int leftMargin, int topMargin, int length, int curPtr) {

        System.out.println("in drawVerticalLine");
//        int len = end - start;
        LinearLayout linearLine = new LinearLayout(this);

        linearLine.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(linearLine.getLayoutParams());
        margin.leftMargin = leftMargin;
        margin.topMargin = topMargin;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        layoutParams.height = length;//设置图片的高度
        layoutParams.width = 3; //设置图片的宽度
        linearLine.setLayoutParams(layoutParams);

        linearLine.setBackgroundColor(Color.BLACK);

        lineMap.put(curPtr, linearLine);

        graphArea.addView(linearLine);

    }

    private void drawIfLine(int upperLeftMargin, int lowerLeftMargin, int upperTopMargin, int lowerTopMargin, int horizontalWidth) {

        LinearLayout horizontal1 = new LinearLayout(this);
        LinearLayout horizontal2 = new LinearLayout(this);
        LinearLayout vertical = new LinearLayout(this);

        int verticalLength = lowerTopMargin - upperTopMargin;

        /**
         * horizontal1
         */
        horizontal1.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

        ViewGroup.MarginLayoutParams hori1Margin = new ViewGroup.MarginLayoutParams(horizontal1.getLayoutParams());
        hori1Margin.leftMargin = upperLeftMargin;
        hori1Margin.topMargin = upperTopMargin;
        RelativeLayout.LayoutParams hori1LayoutParams = new RelativeLayout.LayoutParams(hori1Margin);
        hori1LayoutParams.height = 3;//设置图片的高度
        hori1LayoutParams.width = 550 + horizontalWidth - upperLeftMargin; //设置图片的宽度
        horizontal1.setLayoutParams(hori1LayoutParams);

        horizontal1.setBackgroundColor(Color.BLACK);

        /**
         * horizontal2
         */
        horizontal2.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

        ViewGroup.MarginLayoutParams hori2Margin = new ViewGroup.MarginLayoutParams(horizontal2.getLayoutParams());
        hori2Margin.leftMargin = lowerLeftMargin;
        hori2Margin.topMargin = lowerTopMargin;
        RelativeLayout.LayoutParams hori2LayoutParams = new RelativeLayout.LayoutParams(hori2Margin);
        hori2LayoutParams.height = 3;//设置图片的高度
        hori2LayoutParams.width = 550 + horizontalWidth - lowerLeftMargin; //设置图片的宽度
        horizontal2.setLayoutParams(hori2LayoutParams);

        horizontal2.setBackgroundColor(Color.BLACK);

        /**
         * vertical
         */
        vertical.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

        ViewGroup.MarginLayoutParams vertiMargin = new ViewGroup.MarginLayoutParams(vertical.getLayoutParams());
        vertiMargin.leftMargin = 550 + horizontalWidth;
        vertiMargin.topMargin = upperTopMargin;
        RelativeLayout.LayoutParams vertiLayoutParams = new RelativeLayout.LayoutParams(vertiMargin);
        vertiLayoutParams.height = verticalLength;//设置图片的高度
        vertiLayoutParams.width = 3; //设置图片的宽度
        vertical.setLayoutParams(vertiLayoutParams);

        vertical.setBackgroundColor(Color.BLACK);

        ifLines[0] = horizontal1;
        ifLines[1] = horizontal2;
        ifLines[2] = vertical;

        graphArea.addView(horizontal1);
        graphArea.addView(horizontal2);
        graphArea.addView(vertical);
    }


    public class JSONReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            jsonResult = intent.getStringExtra("json");

            System.out.println("in GraphMode, JSONReceiver received jsonResult: " + jsonResult);
        }
    }


    private boolean checkInput() {
        boolean isValid = false;

        Pattern pattern;
        Matcher matcher;

        String regex;
        String regex2;

        for(EditText et : etMap.values()) {
            switch(et.getId()){
                case R.id.graph_order_blank_1:
                case R.id.graph_order_blank_2:
                case R.id.graph_order_blank_3:
                    System.out.println("checkInput, in order blanks");
                    regex = "car.[forward|backward|sing|pause|left|right]*\\([0-9]*\\)";
                    pattern = Pattern.compile(regex);
                    matcher = pattern.matcher(et.getText().toString().toLowerCase());
                    isValid = matcher.matches();
                    break;

                case R.id.graph_if_blank:
                    System.out.println("checkInput, in if blank");
                    regex = "if time < [0-9]+";
                    regex2 = "if\\(today == [1-7]\\)";
                    pattern = Pattern.compile(regex);
                    matcher = pattern.matcher(et.getText().toString().toLowerCase());
                    isValid = matcher.matches();
                    break;

            }

        }

        return isValid;
    }

    private boolean checkImgValid() {
        boolean isImgValid = true;

        switch(questionId) {
            case 1:
            case 2:
                if(findViewById(R.id.graph_if_img) != null)
                    isImgValid = false;
                break;
            case 3:
            case 4:
                if(findViewById(R.id.graph_if_img) == null)
                    isImgValid = false;
                break;
        }

        return  isImgValid;
    }

}
