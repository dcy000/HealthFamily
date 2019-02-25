package com.gcml.module_login_and_register.presenter;

import com.gzq.lib_resource.mvp.base.IView;

public interface IMessageCodeView extends IView {
    void setButtonGetMessageCodeStatus(boolean status,String btnText);
    void vertifyCodeSuccess();
}
