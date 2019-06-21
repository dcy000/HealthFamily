package com.example.module_call;

import android.app.Activity;
import android.content.Context;

import com.example.module_call.ui.NimAccountHelper;
import com.example.module_call.ui.NimCallActivity;
import com.gzq.lib_resource.api.ICallService;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/call/video/activity")
public class CallServiceImp implements ICallService {
    @Override
    public void launchNoCheckWithCallFamily(Activity context, String phone) {
        NimCallActivity.launchNoCheck(context, phone);
    }

    @Override
    public void loginWY(String phone, String password) {
        NimAccountHelper.getInstance().loginWithPassword(phone, password, null);
    }

    @Override
    public void logoutWY() {
        NimAccountHelper.getInstance().logout();
    }
}
