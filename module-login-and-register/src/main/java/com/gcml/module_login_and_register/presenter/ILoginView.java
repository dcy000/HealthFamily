package com.gcml.module_login_and_register.presenter;

import com.gzq.lib_resource.mvp.base.IView;

public interface ILoginView extends IView {
    void loginWithGuardianshipSuccess();

    void loginWithGuardianshipError();
}
