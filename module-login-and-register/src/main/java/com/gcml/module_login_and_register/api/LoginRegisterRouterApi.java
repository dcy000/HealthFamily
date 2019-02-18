package com.gcml.module_login_and_register.api;

import com.sjtu.yifei.annotation.Go;

public interface LoginRegisterRouterApi {
    /**
     * 跳转到RegisterMessageCodeActivity
     * @return
     */
    @Go("/register/messagecode")
    boolean skipRegisterMessageCode();

    @Go("/main/mainactivity")
    boolean skipMainActivity();
}
