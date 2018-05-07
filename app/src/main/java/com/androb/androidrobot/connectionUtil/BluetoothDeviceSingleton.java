package com.androb.androidrobot.connectionUtil;

import android.bluetooth.BluetoothDevice;

/**
 * Created by kaki on 2018/05/07.
 */

public class BluetoothDeviceSingleton {

    private static BluetoothDevice device = null;

    public static void setDevice(BluetoothDevice chosenDevice) {
        BluetoothDeviceSingleton.device = chosenDevice;
    }

    public static synchronized BluetoothDevice getInstance() {
        return BluetoothDeviceSingleton.device;
    }
}
