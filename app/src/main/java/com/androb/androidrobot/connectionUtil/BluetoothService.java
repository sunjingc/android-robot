package com.androb.androidrobot.connectionUtil;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by kaki on 2018/04/24.
 */

public class BluetoothService extends Service {

    private String deviceName;
    private BluetoothBean blueDevice;
    private BluetoothDevice chosenDevice;
    private BluetoothSocket socket;       //蓝牙连接socket

    private BluetoothSocketSingleton btSingleton;

    private int connectState = 0;


//    public BluetoothService(BluetoothBean blueDevice) {
//        this.blueDevice = blueDevice;
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("BluetoothService", "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("BluetoothService onCreate invoke");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        blueDevice = new BluetoothBean();

        System.out.println("Bluetooth onStartCommand invoke");

        deviceName = intent.getStringExtra("deviceName");
        BluetoothDevice tempDevice = intent.getParcelableExtra("selectedDevice");
        blueDevice.setDevice(tempDevice);
        System.out.println("print deviceName: " + deviceName);
        System.out.println("print blueDevice name: " + blueDevice.getDevice().getName());

        chosenDevice = blueDevice.getDevice();
        connectState = blueDevice.getDevice().getBondState();
        switch (connectState) {
            // 未配对
            case BluetoothDevice.BOND_NONE:
                // 配对
                try {
                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                    createBondMethod.invoke(chosenDevice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            // 已配对
            case BluetoothDevice.BOND_BONDED:
                try {
                    // 连接
                    connect(chosenDevice);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("BluetoothService", "onDestroy");
        super.onDestroy();
    }


    private void connect(BluetoothDevice device) throws IOException {
        // 固定的UUID
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        System.out.println("UUID: " + SPP_UUID);
        UUID uuid = UUID.fromString(SPP_UUID);
        socket = device.createRfcommSocketToServiceRecord(uuid);
        socket.connect();

        // 设置singleton中的socket
        btSingleton.setSocket(socket);
    }


    public void initSocket() {
        BluetoothSocket temp = null;
        try {
            Method m = blueDevice.getDevice().getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            temp = (BluetoothSocket)  m.invoke(blueDevice.getDevice(), 1);
        }catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        socket = temp;
    }

}
