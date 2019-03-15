package com.gcml.devices;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.gcml.devices.base.BindDeviceBean;
import com.inuker.bluetooth.library.BluetoothClient;

import java.lang.reflect.InvocationTargetException;

public class BluetoothStore {

    private static Application mApplication;
    private static boolean mLog;

    public static Application getApp() {
        if (mApplication != null) {
            return mApplication;
        }
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object at = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(at);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return mApplication;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    public static String getString(@StringRes int id) {
        return getApp().getResources().getString(id);
    }

    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getApp(), id);
    }

    public static void init(Application app) {
        mApplication = app;
        client = new BluetoothClient(app);
    }

    public static void init(Application app, boolean isLog) {
        mApplication = app;
        mLog = isLog;
        client = new BluetoothClient(app);
    }

    private static BluetoothClient client;
    public static MutableLiveData<BindDeviceBean> bindDevice = new MutableLiveData<>();


    public static BluetoothClient getClient() {
        return client;
    }

    public static boolean isLog() {
        return mLog;
    }
}
