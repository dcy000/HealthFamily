package com.gcml.call;

import android.app.Activity;

import com.gzq.lib_resource.api.ICallService;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/call/video/activity")
public class CallServiceImp implements ICallService {
    @Override
    public void launchNoCheckWithCallFamily(Activity context, String phone) {
        CallHelper.launch(context, phone);
    }

    @Override
    public void loginWY(String phone, String password) {
        CallAuthHelper.getInstance().login(phone, password, null);
    }

    @Override
    public void logoutWY() {
        CallAuthHelper.getInstance().logout();
    }
}
