package com.gzq.lib_core.utils;


import android.support.annotation.StringRes;
import android.widget.Toast;

import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.Box;

public class ToastUtils {
    public static void showShort(String message) {
        Toast.makeText(App.getApp(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(@StringRes int message) {
        Toast.makeText(App.getApp(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String message) {
        Toast.makeText(App.getApp(), message, Toast.LENGTH_LONG).show();
    }

    public static void showLong(@StringRes int message) {
        Toast.makeText(App.getApp(), message, Toast.LENGTH_LONG).show();
    }
}
