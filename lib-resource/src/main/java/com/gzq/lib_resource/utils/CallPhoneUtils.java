package com.gzq.lib_resource.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.PermissionUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.Permission;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CallPhoneUtils {
    private static CallPhoneUtils callPhoneUtils;

    public static CallPhoneUtils instance() {
        if (callPhoneUtils == null) {
            callPhoneUtils = new CallPhoneUtils();
        }
        return callPhoneUtils;
    }

    public void callPhone(final Activity mActivity, final String phoneNum) {
        PermissionUtils.requestEach(mActivity,
                Manifest.permission.CALL_PHONE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        String message = e.getMessage();
                        if (!TextUtils.isEmpty(message)) {
                            ToastUtils.showLong(message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + phoneNum);
                        intent.setData(data);
                        mActivity.startActivity(intent);
                    }
                });
    }
}
