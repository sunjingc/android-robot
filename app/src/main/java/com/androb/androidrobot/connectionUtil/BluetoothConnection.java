package com.androb.androidrobot.connectionUtil;

import android.app.Application;

import java.io.OutputStream;

/**
 * Created by kaki on 2018/04/24.
 */

public class BluetoothConnection extends Application {

    private OutputStream os;

    public void setOutputStream(OutputStream os) {
        this.os = os;
    }

    public OutputStream getOutputStream() {
        return this.os;
    }
}
