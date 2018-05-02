package com.androb.androidrobot.graphMode;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.connectionUtil.BluetoothDiscoverActivity;
import com.androb.androidrobot.connectionUtil.BluetoothService;
import com.androb.androidrobot.messageUtil.MessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.graph_btn_loop)
    AppCompatButton btn_loop;
    @BindView(R.id.graph_btn_order)
    AppCompatButton btn_order;
    @BindView(R.id.graph_btn_if)
    AppCompatButton btn_if;

    @BindView(R.id.help_btn)
    ImageButton btn_help;

    private Intent mIntent;
    private int questionId = 0;

    private int orderCount = 0;
//    private RelativeLayout graphArea;

    // 记录点击动作 → 生成图形的顺序
    private ArrayList<String> actionHistory = new ArrayList<String>();
    private int ptr = -1;
//    private int etPtr = -1;
    private final int marginSpace = 200;

    private HashMap<Integer, ImageView> imagesMap = new HashMap<>();
    private HashMap<Integer, EditText> etMap = new HashMap<>();


    private List<String> answerStrList = new ArrayList<>();
    private String answerStr;

    private AlertDialog.Builder builder;


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

    }


    @Override
    public void onClick(View view) {

        ViewGroup.LayoutParams image_params;

        switch (view.getId()){

            case R.id.help_btn:
                this.showHelpDialog(view);
                break;

            case R.id.graph_btn_start:

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
                }

                break;

            case R.id.graph_btn_order:

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
//                    orderEtMargin.topMargin = 260;


                    int tempPtr = ptr - 1;
                    if (actionHistory.get(tempPtr).equals("if")) {
                        System.out.println("这个order前面是if");
                        orderMargin.topMargin = marginSpace * ptr + 70;
                        orderEtMargin.topMargin = 220 * ptr;
                    }
                    else {
                        orderMargin.topMargin = marginSpace * ptr + 50;
                        orderEtMargin.topMargin = 235 * ptr;
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

                }

                break;

            case R.id.graph_btn_if:

                if (ptr != 0) {
                    Toast.makeText(GraphModeQuestionActivity.this, "现在if只能在start之后哦", Toast.LENGTH_SHORT).show();
                }
                else {
                    actionHistory.add("if");
                    ptr++;

                    System.out.println("clicked if");

                    FrameLayout if_frame = new FrameLayout(GraphModeQuestionActivity.this);

                    ImageView ifView = new ImageView(this);

                    ifView.setLayoutParams(new LinearLayout.LayoutParams(240, 240));  //设置图片宽高
                    ifView.setImageResource(R.drawable.new_if); //图片资源

                    ViewGroup.MarginLayoutParams ifMargin = new ViewGroup.MarginLayoutParams(ifView.getLayoutParams());
                    ifMargin.leftMargin = 340;
                    ifMargin.topMargin = marginSpace * ptr;
                    RelativeLayout.LayoutParams ifLayoutParams = new RelativeLayout.LayoutParams(ifMargin);
                    ifLayoutParams.height = 250;//设置图片的高度
                    ifLayoutParams.width = 400; //设置图片的宽度
                    ifView.setLayoutParams(ifLayoutParams);

                    ifView.setId(R.id.graph_if_img);

                    imagesMap.put(ptr, ifView);

                    // EditText
                    EditText et_if = new EditText(GraphModeQuestionActivity.this);

                    RelativeLayout.LayoutParams etLayoutParams = new RelativeLayout.LayoutParams(ifMargin);
                    etLayoutParams.width = 320; //设置et的宽度

                    et_if.setLayoutParams(etLayoutParams);
                    et_if.setId(R.id.graph_if_blank);

                    etMap.put(ptr, et_if);

                    graphArea.addView(ifView); //动态添加图片
                    graphArea.addView(et_if);

                }

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

                    endView.setId(R.id.graph_end_img);

                    imagesMap.put(ptr, endView);

                    graphArea.addView(endView); //动态添加图片

                }

                break;

            case R.id.graph_ques_cancel_btn:
                System.out.println("点击撤回一步");

                if(ptr >= 0) {
                    int removePtr = ptr;
                    ImageView tempView = imagesMap.get(removePtr);

                    // EditText
                    EditText tempEt = etMap.get(removePtr);

                    int tempId = tempView.getId();
                    System.out.println("tempId: " + tempId);

                    System.out.println("这个控件id是否等于graph_order_img_1: " + (tempId == R.id.graph_order_img_1));
                    if(tempId == R.id.graph_order_img_1 || tempId == R.id.graph_order_img_2 || tempId == R.id.graph_order_img_3) {
                        orderCount--;
                        System.out.println("撤回中 orderCount: " + orderCount);
                    }

                    graphArea.removeView(tempView);
                    graphArea.removeView(tempEt);

                    etMap.remove(ptr);
                    imagesMap.remove(ptr);

                    ptr--;
                }
                else {
                    Toast.makeText(GraphModeQuestionActivity.this, "没东西删什么啊", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.graph_ques_submit_btn:
                answerStrList = new ArrayList<>();
                answerStr = "";
                int etCount = etMap.size();
                System.out.println("etCount: " + etCount);
                for(int key : etMap.keySet()) {
                    String tempAns = etMap.get(key).getText().toString();
                    System.out.println("tempAns: " + tempAns);
                    answerStrList.add(tempAns);
//                    answerStr += tempAns;
//                    answerStr += ", ";
                }
                answerStr = answerStrList.toString();
                System.out.println("answerStr: " + answerStr);

                // answerStr
                // quesType
                // quesId
                Intent msgIntent = new Intent(this, MessageService.class);
                msgIntent.putExtra("answerStr", answerStr);
                msgIntent.putExtra("quesType", "graph");
                msgIntent.putExtra("quesId", questionId + "");
                startService(msgIntent);

                break;
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

    private void showHelpDialog(View view) {
        builder=new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("帮助");
        builder.setMessage(R.string.help_msg);

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


}
