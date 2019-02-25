package com.gzq.lib_resource.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class CallPhoneUtils {
    private static CallPhoneUtils callPhoneUtils;

    public static CallPhoneUtils instance() {
        if (callPhoneUtils == null) {
            callPhoneUtils = new CallPhoneUtils();
        }
        return callPhoneUtils;
    }

    public void callPhone(final Activity mActivity, final String phoneNum) {
        RxPermissions permissions = new RxPermissions(mActivity);
        permissions.requestEachCombined(Manifest.permission.CALL_PHONE)
                .subscribe(new CommonObserver<Permission>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + phoneNum);
                            intent.setData(data);
                            mActivity.startActivity(intent);
                        } else {
                            ToastUtils.showLong("拨打电话，需要您同意相关权限");
                        }
                    }
                });
    }
}
