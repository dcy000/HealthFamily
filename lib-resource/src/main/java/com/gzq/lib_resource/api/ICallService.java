package com.gzq.lib_resource.api;

import android.app.Activity;
import android.content.Context;

public interface ICallService {
    /**
     * 拨打视频电话
     *
     * @param context
     * @param phone
     */
    void launchNoCheckWithCallFamily(Activity context, String phone);

    /**
     * 登录网易
     *
     * @param phone
     * @param password
     */
    void loginWY(String phone, String password);

    /**
     * 退出网易账号
     */
    void logoutWY();
}
