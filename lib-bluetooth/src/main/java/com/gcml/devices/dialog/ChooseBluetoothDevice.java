package com.gcml.devices.dialog;

import android.bluetooth.BluetoothDevice;

public interface ChooseBluetoothDevice {
    void choosed(BluetoothDevice device);
    void autoConnect();
    void cancelSearch();
}
