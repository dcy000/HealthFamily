package com.gcml.devices.base;


import android.bluetooth.BluetoothDevice;

public interface IBluetoothView {
    void updateData(String... datas);
    void updateState(String state);
    void discoveryNewDevice(BluetoothDevice device);
}
