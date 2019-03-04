package com.gzq.lib_resource.api;

import android.content.Intent;

import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Flags;
import com.sjtu.yifei.annotation.Go;
import com.sjtu.yifei.route.ActivityCallback;

public interface CommonRouterApi {
    @Go("/call/video/activity")
    ICallService getCallServiceImp();

    @Go("/main/mainactivity")
    boolean skipMainActivity();

    @Flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
    @Go("/main/mainactivity")
    boolean skipMainActivityNewTask();

    /**
     * 1. 注册人脸
     * 2. 更新人脸
     */
    @Go("/face/auth/signup")
    boolean skipFaceBdSignUpActivity(@Extra ActivityCallback callback);

    @Flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
    @Go("/login/phone")
    boolean skipLoginActivityWithNewTask();
}
