package com.gzq.lib_resource.utils;

import android.app.Activity;

import com.gzq.lib_core.utils.ActivityUtils;
import com.kaopiz.kprogresshud.KProgressHUD;


public class LoadingViewUtils  {
    private static KProgressHUD kProgressHUD;
    private static Activity currentActivity;

    public static void show(String label, boolean canCancelable) {
        if (kProgressHUD == null) {
            currentActivity = ActivityUtils.currentActivity();
            kProgressHUD = KProgressHUD.create(currentActivity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(label)
                    .setCancellable(canCancelable)
                    .setAnimationSpeed(2)
                    .setDimAmount(0f)
                    .show();
        } else {
            if (!kProgressHUD.isShowing()) {
                kProgressHUD.show();
            }
        }
    }

    public static void show(String label, String detailLabel, boolean canCancelable) {
        if (kProgressHUD == null) {
            currentActivity = ActivityUtils.currentActivity();
            kProgressHUD = KProgressHUD.create(currentActivity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(label)
                    .setDetailsLabel(detailLabel)
                    .setCancellable(canCancelable)
                    .setAnimationSpeed(2)
                    .setDimAmount(0f)
                    .show();
        } else {
            if (!kProgressHUD.isShowing()) {
                kProgressHUD.show();
            }
        }
    }

    public static void hide() {
        if (kProgressHUD != null && kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
        kProgressHUD = null;
        currentActivity = null;
    }


    public static void showProgress(String label, int max, int current) {
        if (kProgressHUD == null) {
            currentActivity = ActivityUtils.currentActivity();
            kProgressHUD = KProgressHUD.create(currentActivity)
                    .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                    .setLabel(label)
                    .setCancellable(false)
                    .setMaxProgress(max)
                    .setDimAmount(0f)
                    .show();
            kProgressHUD.setProgress(current);
        } else {
            kProgressHUD.setProgress(current);
            kProgressHUD.show();
        }
    }
}
