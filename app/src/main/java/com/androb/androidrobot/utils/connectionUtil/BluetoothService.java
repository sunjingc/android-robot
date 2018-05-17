package com.androb.androidrobot.utils.connectionUtil;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kaki on 2018/05/15.
 */

public class BluetoothService extends Service {

    BluetoothSocket socket;
    String msg;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("BluetoothService onCreate invoke");
        socket = BluetoothSocketSingleton.getInstance();
        super.onCreate();
    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand invoke");

        msg = intent.getStringExtra("msg");

        if(socket == null) {
            Log.d("BluetoothService", "socket is null, trying again");
            socket = BluetoothSocketSingleton.getInstance();
        }

        if (!socket.isConnected()) {
            Log.d("BluetoothService", "socket not connected");
            try {
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        msg += "\n";
        try {
            OutputStream mmOutputStream = socket.getOutputStream();
            mmOutputStream.write(msg.getBytes());

            Log.d("BluetoothService", "msg sent");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
//        socket.close();
        System.out.println("BluetoothService onDestroy invoke");
        super.onDestroy();
    }
}
