package com.androb.androidrobot.connectionUtil;

import android.bluetooth.BluetoothSocket;

/**
 * Created by kaki on 2018/05/08.
 */

public class BluetoothSocketSingleton {

    private static BluetoothSocket socket = null;

    public static void setSocket(BluetoothSocket chosenSocket) {
        BluetoothSocketSingleton.socket = chosenSocket;
    }

    public static synchronized BluetoothSocket getInstance() {
        return BluetoothSocketSingleton.socket;
    }
}
