package com.gcml.devices.utils;

import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.gcml.devices.BluetoothStore;
import com.gcml.devices.base.BluetoothHandler;

public class T {
    public static void showShort(final String message) {
        if (isMainThread()) {
            Toast.makeText(BluetoothStore.getApp(), message, Toast.LENGTH_SHORT).show();
        } else {
            new BluetoothHandler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BluetoothStore.getApp(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static void showShort(@StringRes final int message) {
        if (isMainThread()) {
            Toast.makeText(BluetoothStore.getApp(), message, Toast.LENGTH_SHORT).show();
        } else {
            new BluetoothHandler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BluetoothStore.getApp(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static void showLong(final String message) {
        if (isMainThread()) {
            Toast.makeText(BluetoothStore.getApp(), message, Toast.LENGTH_LONG).show();
        } else {
            new BluetoothHandler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BluetoothStore.getApp(), message, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    public static void showLong(@StringRes final int message) {
        if (isMainThread()) {
            Toast.makeText(BluetoothStore.getApp(), message, Toast.LENGTH_LONG).show();
        } else {
            new BluetoothHandler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BluetoothStore.getApp(), message, Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
