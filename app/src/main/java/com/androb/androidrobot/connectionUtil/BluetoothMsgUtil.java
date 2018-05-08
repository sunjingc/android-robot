package com.androb.androidrobot.connectionUtil;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by kaki on 2018/05/08.
 */

public class BluetoothMsgUtil {

    private BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = BluetoothDeviceSingleton.getInstance();

    public BluetoothMsgUtil() {
        this.connectSocket();
    }

    private void connectSocket() {
        try {
            mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
            mmSocket.connect();
            BluetoothSocketSingleton.setSocket(mmSocket);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {

        BluetoothSocket socket = BluetoothSocketSingleton.getInstance();

        if (!socket.isConnected()) {
            try {
                socket.connect(); // this!!!
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        msg += "\n";
        try {
            OutputStream mmOutputStream = socket.getOutputStream();

            mmOutputStream.write(msg.getBytes());
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
