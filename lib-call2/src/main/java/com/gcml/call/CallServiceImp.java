package com.gcml.call;

import android.Manifest;
import android.app.Activity;
import android.text.TextUtils;

import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.PermissionUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.ICallService;
import com.sjtu.yifei.annotation.Route;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

@Route(path = "/call/video/activity")
public class CallServiceImp implements ICallService {
    @Override
    public void launchNoCheckWithCallFamily(Activity activity, String phone) {
        requestPermissions(activity, phone);
    }

    @Override
    public void loginWY(String phone, String password) {
        CallAccountHelper.INSTANCE.login(phone, password, null);
    }

    @Override
    public void logoutWY() {
        CallAccountHelper.INSTANCE.logout();
    }

    private void requestPermissions(Activity activity, String phone) {
        PermissionUtils.requestEach(activity,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                PermissionUtils.PERMISSION_SYSTEM_ALERT)
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
                        CallHelper.outgoingCall(activity, phone);
                    }
                });
    }
}
