package com.androb.androidrobot.connectionUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androb.androidrobot.R;

import butterknife.ButterKnife;

public class BluetoothDiscoverActivity extends Activity {

    // 获取到蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    // 用来保存搜索到的设备信息
    private List<String> bluetoothDevices = new ArrayList<String>();
    // ListView组件
    private ListView lvDevices;

    private ListView deviceChoices;

    // ListView的字符串数组适配器
    private ArrayAdapter<String> arrayAdapter;
    // UUID，蓝牙建立链接需要的
    private final UUID MY_UUID = UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c27bae3");
    // 为其链接创建一个名称
    private final String NAME = "Bluetooth_Socket";
    // 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
    private BluetoothDevice selectDevice;
    // 获取到选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了

    //用于记录用户选择的变量
    private int selectPosition = -1;

    private String chosenResult;

//    private BluetoothDeviceSingleton btDeviceSingleton = new BluetoothDeviceSingleton();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_discover_layout);
        ButterKnife.bind(this);

        // 获取到蓝牙默认的适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // cannot support bluetooth
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
        }


        lvDevices = (ListView) findViewById(R.id.device_list);
        // 为listview设置字符换数组适配器
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,
                bluetoothDevices);
        // 为listView绑定适配器
        lvDevices.setAdapter(arrayAdapter);
        // 为listView设置item点击事件侦听
//        lvDevices.setOnItemClickListener(this);


        // 用Set集合保持已绑定的设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                // 保存到arrayList集合中
                bluetoothDevices.add(bluetoothDevice.getName() + ":"
                        + bluetoothDevice.getAddress() + "\n");
            }
        }

        // 因为蓝牙搜索到设备和完成搜索都是通过广播来告诉其他应用的
        // 这里注册找到设备和完成搜索广播
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        deviceChoices = (ListView) findViewById(R.id.device_list);

        final SingleChoiceAdapter myAdapter = new SingleChoiceAdapter(this, bluetoothDevices);
        deviceChoices.setAdapter(myAdapter);
        deviceChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                selectPosition = position;
                myAdapter.notifyDataSetChanged();
                chosenResult = bluetoothDevices.get(position);
                Toast.makeText(BluetoothDiscoverActivity.this, "已经选择该设备" + chosenResult, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void onClick_Search(View view) {
        setTitle("正在扫描...");
        // 点击搜索周边设备，如果正在搜索，则暂停搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }


    public void onClick_ChooseDevice(View view) {

        Intent btIntent = new Intent(this, BluetoothService.class);

        Toast.makeText(BluetoothDiscoverActivity.this, "in ChooseDevice", Toast.LENGTH_SHORT).show();
        System.out.println("in ChooseDevice pre");

        // 获取到这个设备的信息
        String s = arrayAdapter.getItem(selectPosition);
        // 对其进行分割，获取到这个设备的地址
        String address = s.substring(s.indexOf(":") + 1).trim();
        // 判断当前是否还是正在搜索周边设备，如果是则暂停搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // 如果选择设备为空则代表还没有选择设备
        if (selectDevice == null) {
            //通过地址获取到该设备
            selectDevice = mBluetoothAdapter.getRemoteDevice(address);
            System.out.println("selectDevice name: " + selectDevice.getName());
            System.out.println("selectDevice address: " + selectDevice.getAddress());
        }

        BluetoothDeviceSingleton.setDevice(selectDevice);

//        btIntent.putExtra("deviceName", s);
//        btIntent.putExtra("selectedDevice", selectDevice);
////        btIntent.putExtra("actionType", "connect");
//        startService(btIntent);

        Toast.makeText(BluetoothDiscoverActivity.this, "End of ChooseDevice", Toast.LENGTH_SHORT).show();
    }


    // 注册广播接收者
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // 获取到广播的action
            String action = intent.getAction();
            // 判断广播是搜索到设备还是搜索完成
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                // 找到设备后获取其设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 判断这个设备是否是之前已经绑定过了，如果是则不需要添加，在程序初始化的时候已经添加了
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 设备没有绑定过，则将其保持到arrayList集合中
                    bluetoothDevices.add(device.getName() + ":" + device.getAddress() + "\n");
                    // 更新字符串数组适配器，将内容显示在listView中
                    arrayAdapter.notifyDataSetChanged();
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setTitle("搜索完成");
            }
        }
    };


    // 创建handler，因为我们接收是采用线程来接收的，在线程中无法操作UI，所以需要handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 通过msg传递过来的信息，吐司一下收到的信息
            Toast.makeText(BluetoothDiscoverActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
    };



    public class SingleChoiceAdapter extends BaseAdapter {

        Context context;
        List<String> deviceList;
        LayoutInflater mInflater;

        public SingleChoiceAdapter(Context context, List<String> mList){
            this.context = context;
            this.deviceList = mList;
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return deviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.single_choice, parent,false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView)convertView.findViewById(R.id.id_name);
                viewHolder.select = (RadioButton)convertView.findViewById(R.id.id_select);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            viewHolder.name.setText(deviceList.get(position));
            if (selectPosition == position) {
                viewHolder.select.setChecked(true);
            }
            else {
                viewHolder.select.setChecked(false);
            }

            return convertView;
        }
    }


    public class ViewHolder {
        TextView name;
        RadioButton select;
    }

}
