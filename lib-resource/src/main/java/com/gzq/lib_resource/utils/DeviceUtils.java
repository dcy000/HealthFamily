package com.gzq.lib_resource.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import com.gzq.lib_core.base.App;
import com.gzq.lib_core.utils.FileUtils;
import com.gzq.lib_core.utils.ToastUtils;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 设备工具类
 *
 * @author vondear
 * @date 2016/1/24
 */

public class DeviceUtils {

    private static Application getApplication(){
        return App.getApp();
    }

    /**
     * IMEI （唯一标识序列号）
     * <p>需与{@link #isPhone(Context)}一起使用</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @return IMEI
     */
    public static String getIMEI() {
        String deviceId;
        if (isPhone(getApplication())) {
            deviceId = getDeviceIdIMEI(getApplication());
        } else {
            deviceId = getAndroidId();
        }
        return deviceId;
    }
    /**
     * 判断设备是否是手机
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取设备的IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceIdIMEI(Context context) {
        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showShort("请先获取读取手机设备权限");
            return null;
        }
        if (mTelephony.getDeviceId() != null) {
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }
    /**
     * 获取ANDROID ID
     *
     * @return
     */
    public static String getAndroidId() {
        return Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
