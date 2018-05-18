package com.androb.androidrobot.dragMode;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androb.androidrobot.R;
import com.androb.androidrobot.utils.connectionUtil.BluetoothService;
import com.androb.androidrobot.utils.connectionUtil.TransferUtil;
import com.androb.androidrobot.utils.messageUtil.MessageFormatter;
import com.androb.androidrobot.utils.messageUtil.MessageValidator;
import com.androb.androidrobot.utils.questionUtil.QuestionStatusManager;
import com.androb.androidrobot.utils.userUtil.UserManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaki on 2018/03/30.
 */

public class DragModeQuestionActivity extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener, View.OnClickListener {

    @BindView(R.id.starting_point)
    Button btnStart;

    @BindView(R.id.drag_btn_forward)
    Button btnForward;
    @BindView(R.id.drag_btn_backward)
    Button btnBackward;
    @BindView(R.id.drag_btn_pause)
    Button btnPause;
    @BindView(R.id.drag_btn_left)
    Button btnLeft;
    @BindView(R.id.drag_btn_right)
    Button btnRight;
    @BindView(R.id.drag_btn_sing)
    Button btnSing;
    @BindView(R.id.drag_btn_repeat)
    Button btnRepeat;
    @BindView(R.id.drag_btn_end)
    Button btnEnd;

    @BindView(R.id.help_btn)
    ImageButton btnHelp;
    @BindView(R.id.drag_ques_submit_btn)
    Button btnSubmit;
    @BindView(R.id.drag_question_text)
    TextView dragText;
    @BindView(R.id.drag_done)
    Button dragDone;

    @BindView(R.id.answer_layout)
    LinearLayout answerLayout;

    // 屏幕的宽度和高度
    private int screenWidth;
    private int screenHeight;

    private int answerNum = 0;

    float dX = 0;
    float dY = 0;
    int lastAction = 0;

    private int quesId = 0;

    private int forwardChoice = 0;
    private int backwardChoice = 0;
    private int leftChoice = 0;
    private int rightChoice = 0;
    private int pauseChoice = 0;
    private int singChoice = 0;
    private int repeatChoice = 0;

    // 是否要开始缩进
    private int REPEAT_FLAG = 0;

    private List<String> answerStrList = new ArrayList<>();
    private String answerStr;

    private AlertDialog.Builder builder;

//    private JSONReceiver receiver;
    private String jsonResult;
    private boolean isLoggedin;

    // standard left value
    private int standardWidth;
    private int standardHeight;
    private int standardWeight;

    private boolean qStatus = false;

    // check message and BT transfer
    // JSON message
    private MessageFormatter formatter = new MessageFormatter();
    private MessageValidator validator = new MessageValidator();

    private TransferUtil transferUtil = new TransferUtil();
    private Context context;

    private int lessWidth;



    protected void onStart() {
//        qStatus = QuestionStatusManager.getInstance(DragModeQuestionActivity.this).checkQuestionStatus("drag", quesId);
//
//        if(qStatus) {
//            Toast.makeText(DragModeQuestionActivity.this, "你已经做过这道题目了~", Toast.LENGTH_SHORT).show();
//        }
        super.onStart();
    }

    protected void onDestroy() {
//        unregisterReceiver(receiver);

        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_mode_question_layout);

        ButterKnife.bind(this);

        btnHelp.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        context = this.getApplicationContext();
        Intent intent = this.getIntent();
        quesId = Integer.parseInt(intent.getStringExtra("btn_id"));

        initView();

        isLoggedin = UserManager.getInstance(this).isLoggedIn();

        dragDone.setVisibility(View.INVISIBLE);
//        if(isLoggedin) {
//            SharedPreferences sharedPreferences = this.getApplication().getSharedPreferences("sharedUserPref", Context.MODE_PRIVATE);
//            qStatus = (sharedPreferences.getString("dragString", null).indexOf(quesId + "") == -1);
//            if(!qStatus) {
//                dragDone.setVisibility(View.VISIBLE);
//            }
//        }

//        btSocket = btSingleton.getInstance();

//        mmDevice = BluetoothDeviceSingleton.getInstance();
//        Toast.makeText(getApplicationContext(), "BTDevice name: " + mmDevice.getName(), Toast.LENGTH_SHORT).show();

//        Toast.makeText(getApplicationContext(), btSocket.getClass().toString(), Toast.LENGTH_SHORT).show();

        standardHeight = btnForward.getLayoutParams().height;
        standardWidth = btnForward.getLayoutParams().width;

        isLoggedin = UserManager.getInstance(this).isLoggedIn();

        lessWidth = btnForward.getLayoutParams().width - 120;

//        receiver = new JSONReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.androb.androidrobot.messageUtil.MessageService");
//        registerReceiver(receiver, filter);
    }

    private void initView() {

        // question text
        switch(quesId) {
            case 1:
                dragText.setText(R.string.d_ques_1);
                break;
            case 2:
                dragText.setText(R.string.d_ques_2);
                break;
            case 3:
                dragText.setText(R.string.d_ques_3);
                break;
            case 4:
                dragText.setText(R.string.d_ques_4);
                break;
        }


        // 获取屏幕的宽度和高度
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();

        System.out.println("screen width: " + screenWidth);
        System.out.println("screen height: " + screenHeight);

        btnForward.setTag("DRAGGABLE FORWARD");
        btnForward.setOnLongClickListener(this);
        btnBackward.setTag("DRAGGABLE BACKWARD");
        btnBackward.setOnLongClickListener(this);
        btnPause.setTag("DRAGGABLE PAUSE");
        btnPause.setOnLongClickListener(this);
        btnLeft.setTag("DRAGGABLE LEFT");
        btnLeft.setOnLongClickListener(this);
        btnRight.setTag("DRAGGABLE RIGHT");
        btnRight.setOnLongClickListener(this);
        btnSing.setTag("DRAGGABLE SING");
        btnSing.setOnLongClickListener(this);
        btnRepeat.setTag("DRAGGABLE REPEAT");
        btnRepeat.setOnLongClickListener(this);
        btnEnd.setTag("DRAGGABLE END");
        btnEnd.setOnLongClickListener(this);

        btnForward.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnSing.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        btnEnd.setOnClickListener(this);

        findViewById(R.id.btns_layout).setOnDragListener(this);
        findViewById(R.id.answer_layout).setOnDragListener(this);

    }

    private void showHelpDialog(View view) {
        builder=new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("拖拽模式帮助");
        builder.setMessage(R.string.drag_help_msg);

        //监听下方button点击事件
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "那就加油吧", Toast.LENGTH_SHORT).show();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onDrag(View view, DragEvent event) {
        final int action = event.getAction();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED: // 拖拽开始
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true;
                }
                return false;

            case DragEvent.ACTION_DRAG_ENTERED: // 被拖拽View进入目标区域
                view.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION: // 被拖拽View在目标区域移动
                return true;

            case DragEvent.ACTION_DRAG_EXITED: // 被拖拽View离开目标区域
                // Invalidate the view to force a redraw in the new tint
                view.invalidate();
                return true;

            case DragEvent.ACTION_DROP: // 放开被拖拽View

                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);

                // Gets the text data from the item.
                String dragData = item.getText().toString();
                // Displays a message containing the dragged data.
//                Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();
                // Invalidates the view to force a redraw
                view.invalidate();

                View vw = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) vw.getParent();
                owner.removeView(vw); //remove the dragged view

//                Toast.makeText(this, "Id??  " + vw.getId(), Toast.LENGTH_SHORT).show();

                int tempId = vw.getId();

                //caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                LinearLayout container = (LinearLayout) view;
                container.addView(vw);//Add the dragged view

                switch(tempId) {
                    case R.id.drag_btn_end:
                        if(owner.getId() == R.id.btns_layout) {
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(btnStart.getLayoutParams().width, btnStart.getLayoutParams().height);
                            param.gravity = Gravity.CENTER;
                            vw.setLayoutParams(param);
                        }
                        else {
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(standardWidth, standardHeight);
                            param.gravity = Gravity.CENTER;
                            vw.setLayoutParams(param);
                        }
                        break;
                    case R.id.drag_btn_repeat:
                        if(owner.getId() == R.id.btns_layout) {
                            REPEAT_FLAG = 1;
                        }
                        else {
                            REPEAT_FLAG = 0;
                        }

                        break;
                    default:
                        System.out.println("REPEAT_FLAG:  " + REPEAT_FLAG);
                        if(owner.getId() == R.id.btns_layout) {
                            if(REPEAT_FLAG == 1) {
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(lessWidth, standardHeight);
                                param.gravity = Gravity.CENTER;
                                vw.setLayoutParams(param);
                            }
                        }
                        else {
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(standardWidth, standardHeight);
                            param.gravity = Gravity.CENTER;
                            vw.setLayoutParams(param);
                        }
                        break;

                }

                vw.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                // Returns true. DragEvent.getResult() will return true.
                return true;

            case DragEvent.ACTION_DRAG_ENDED: // 拖拽完成

                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

        // Instantiates the drag shadow builder.
        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(view);

        // Starts the drag
        view.startDrag(data        // data to be dragged
                , dragshadow   // drag shadow builder
                , view           // local data about the drag and drop operation
                , 0          // flags (not currently used, set to 0)
        );
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.help_btn:
                this.showHelpDialog(view);
                break;

            case R.id.drag_btn_forward:

                btnForward.setText("前进");

                builder=new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("前进几米？");

                /**
                 * 设置内容区域为单选列表项
                 */
                final String[] items={"1米","2米","3米","4米","5米","6米","7米","8米","9米","10米"};
                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getApplicationContext(), "You clicked "+items[i], Toast.LENGTH_SHORT).show();
                        forwardChoice = i + 1;
                    }
                });

                //监听下方button点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp = (String) btnForward.getText();
                        temp += forwardChoice;
                        temp += "米";
                        btnForward.setText(temp);
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialog=builder.create();
                dialog.show();

                break;

            case R.id.drag_btn_backward:

//                Toast.makeText(getApplicationContext(), "back", Toast.LENGTH_SHORT).show();

                btnBackward.setText("后退");

                builder=new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("后退几米？");

                /**
                 * 设置内容区域为单选列表项
                 */
                final String[] itemsBack={"1米","2米","3米","4米","5米","6米","7米","8米","9米","10米"};
                builder.setSingleChoiceItems(itemsBack, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        backwardChoice = i + 1;
                    }
                });

                //监听下方button点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp = (String) btnBackward.getText();
                        temp += backwardChoice;
                        temp += "米";
                        btnBackward.setText(temp);
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialogBack=builder.create();
                dialogBack.show();

                break;

            case R.id.drag_btn_pause:

                btnPause.setText("暂停");

                builder=new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("暂停几秒？");

                /**
                 * 设置内容区域为单选列表项
                 */
                final String[] itemsPause={"1秒","2秒","3秒","4秒","5秒","6秒","7秒","8秒","9秒","10秒"};
                builder.setSingleChoiceItems(itemsPause, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pauseChoice = Integer.valueOf(itemsPause[i].split("秒")[0]);
                    }
                });

                //监听下方button点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp = (String) btnPause.getText();
                        temp += pauseChoice;
                        temp += "秒";
                        btnPause.setText(temp);
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialogPause=builder.create();
                dialogPause.show();

                break;

            case R.id.drag_btn_left:

                btnLeft.setText("左转");

                builder=new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("左转几度？");

                /**
                 * 设置内容区域为单选列表项
                 */
                final String[] itemsLeft={"15度","30度","45度","60度","75度","90度","105度","120度","135度","150度","165度","180度"};
                builder.setSingleChoiceItems(itemsLeft, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        leftChoice = Integer.valueOf(itemsLeft[i].split("度")[0]);
                    }
                });

                //监听下方button点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp = (String) btnLeft.getText();
                        temp += leftChoice;
                        temp += "度";
                        btnLeft.setText(temp);
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialogLeft=builder.create();
                dialogLeft.show();

                break;

            case R.id.drag_btn_right:

                btnRight.setText("右转");

                builder=new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("右转几度？");

                /**
                 * 设置内容区域为单选列表项
                 */
                final String[] itemsRight={"15度","30度","45度","60度","75度","90度","105度","120度","135度","150度","165度","180度"};
                builder.setSingleChoiceItems(itemsRight, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        rightChoice = Integer.valueOf(itemsRight[i].split("度")[0]);
                    }
                });

                //监听下方button点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp = (String) btnRight.getText();
                        temp += rightChoice;
                        temp += "度";
                        btnRight.setText(temp);
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialogRight=builder.create();
                dialogRight.show();

                break;

            case R.id.drag_btn_sing:

                btnSing.setText("唱歌");

                builder=new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("唱几遍？");

                /**
                 * 设置内容区域为单选列表项
                 */
                final String[] itemsSing={"1遍","2遍","3遍","4遍","5遍"};
                builder.setSingleChoiceItems(itemsSing, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        singChoice = Integer.valueOf(itemsSing[i].split("遍")[0]);
                    }
                });

                //监听下方button点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp = (String) btnSing.getText();
                        temp += singChoice;
                        temp += "遍";
                        btnSing.setText(temp);
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialogSing=builder.create();
                dialogSing.show();

                break;

            case R.id.drag_btn_repeat:

                btnRepeat.setText("循环");

                builder=new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("循环几次？");

                /**
                 * 设置内容区域为单选列表项
                 */
                final String[] itemsRepeat={"2次","3次","4次","5次","6次","7次","8次","9次","10次"};
                builder.setSingleChoiceItems(itemsRepeat, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        repeatChoice = Integer.valueOf(itemsRepeat[i].split("次")[0]);
                    }
                });

                //监听下方button点击事件
                builder.setPositiveButton("循环", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp = (String) btnRepeat.getText();
                        temp += repeatChoice;
                        temp += "次";
                        btnRepeat.setText(temp);
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialogRepeat=builder.create();
                dialogRepeat.show();

                break;

            case R.id.drag_ques_submit_btn:
                boolean valid = this.validate();
                System.out.println(valid);

                if(valid) {
                    answerStrList = new ArrayList<>();

                    this.getAnswerList();

                    answerStr = answerStrList.toString();
                    jsonResult = formatter.format2JSON(answerStr, "drag", quesId + "").toString();

                    // new bluetooth transfer
                    Intent intent = new Intent(this, BluetoothService.class);
                    intent.putExtra("msg", jsonResult);
                    startService(intent);
//                    transferUtil.sendMsg(jsonResult);
                    if(validator.checkMessage(jsonResult, "drag", quesId + "") == true){
                        Toast.makeText(context, "回答正确", Toast.LENGTH_SHORT).show();
                        if(isLoggedin) {
                            UserManager.getInstance(context).updateScore();
                            QuestionStatusManager.getInstance(context).updateStatus("drag", quesId);
                        }
                    }
                    else {
                        Toast.makeText(context, "回答不对哦", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "好像哪里不对", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private boolean validate() {
        boolean result = false;
        answerNum = answerLayout.getChildCount();
        System.out.println("AnswerNum: " + answerNum);
        Button lastChild = (Button) answerLayout.getChildAt(answerNum - 1);
        if(lastChild.getId() == R.id.drag_btn_end) {
            result = true;
        }
        return result;
    }

    private void getAnswerList() {
        for(int i = 1; i < answerNum - 1; i++) {
            answerStrList.add((String) ((Button) answerLayout.getChildAt(i)).getText());
        }
    }

}
