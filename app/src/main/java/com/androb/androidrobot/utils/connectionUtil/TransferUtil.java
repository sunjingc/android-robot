package com.androb.androidrobot.utils.connectionUtil;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kaki on 2018/05/14.
 */

public class TransferUtil {

    private BluetoothSocket socket;

    public void sendMsg(String msg) {
//        BluetoothSocket socket = socket = BluetoothSocketSingleton.getInstance();
        socket = BluetoothSocketSingleton.getInstance();

        if (!socket.isConnected()) {
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
//            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
