package com.gzq.lib_core.utils;


import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.Box;

public class ToastUtils {
    public static void showShort(final String message) {
        if (isMainThread()) {
            Toast.makeText(App.getApp(), message, Toast.LENGTH_SHORT).show();
        } else {
            Box.getHandler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(App.getApp(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void showShort(@StringRes final int message) {
        if (isMainThread()) {
            Toast.makeText(App.getApp(), message, Toast.LENGTH_SHORT).show();
        } else {
            Box.getHandler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(App.getApp(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void showLong(final String message) {
        if (isMainThread()) {
            Toast.makeText(App.getApp(), message, Toast.LENGTH_LONG).show();
        } else {
            Box.getHandler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(App.getApp(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static void showLong(@StringRes final int message) {
        if (isMainThread()) {
            Toast.makeText(App.getApp(), message, Toast.LENGTH_LONG).show();
        } else {
            Box.getHandler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(App.getApp(), message, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
