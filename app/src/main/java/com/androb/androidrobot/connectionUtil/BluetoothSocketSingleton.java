package com.androb.androidrobot.connectionUtil;

import android.bluetooth.BluetoothSocket;

/**
 * Created by kaki on 2018/05/03.
 */

public class BluetoothSocketSingleton {


    private static BluetoothSocket socket;

    public static void setSocket(BluetoothSocket socketpass) {
        BluetoothSocketSingleton.socket = socketpass;
    }

    public static synchronized BluetoothSocket getInstance() {
        return BluetoothSocketSingleton.socket;
    }

}
