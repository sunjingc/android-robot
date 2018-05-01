package com.androb.androidrobot.connectionUtil;

import android.bluetooth.BluetoothDevice;

/**
 * Created by kaki on 2018/04/28.
 */

public class BluetoothBean {

    private String name;
    private String address;
    private BluetoothDevice device;
    private String status="unconnected";


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

}
