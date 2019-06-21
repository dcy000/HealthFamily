package com.gcml.call;

import android.Manifest;
import android.app.Activity;

import com.gzq.lib_core.http.observer.CommonObserver;
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
        RxPermissions permissions = new RxPermissions(activity);
        permissions.requestEach(
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new CommonObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            CallHelper.outgoingCall(activity, phone);
                        } else {
                            ToastUtils.showLong("请在应用设置中打开相关权限");
                        }
                    }
                });
    }
}
