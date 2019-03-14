package com.gcml.devices.base;

import android.bluetooth.BluetoothDevice;

public interface ConnectListener {
    void success(BluetoothDevice device);
    void failed();
    void disConnect(String address);
}
