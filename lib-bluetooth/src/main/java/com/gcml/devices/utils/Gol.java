package com.gcml.devices.utils;

import android.util.Log;

import com.gcml.devices.BluetoothStore;

public class Gol {
    private static final String TAG = "BluetoothDeviceLog";

    public static void i(String msg) {
        if (BluetoothStore.isLog()) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (BluetoothStore.isLog()) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (BluetoothStore.isLog()) {
            Log.e(TAG, msg);
        }
    }
}
