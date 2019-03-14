package com.gcml.devices.base;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.inuker.bluetooth.library.BluetoothClient;

public class BluetoothStore implements AppLifecycle {
    private static BluetoothClient client;
    public static MutableLiveData<BindDeviceBean> bindDevice = new MutableLiveData<>();


    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        client = new BluetoothClient(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    public static BluetoothClient getClient() {
        return client;
    }

}
