package com.gcml.module_login_and_register.presenter;

import android.text.TextUtils;

import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;

public class RegisterPassword extends BasePresenter {

    public RegisterPassword(IView view) {
        super(view);
    }

    @Override
    public void preData(Object... objects) {

    }

    @Override
    public void refreshData(Object... objects) {

    }

    @Override
    public void loadMoreData(Object... objects) {

    }

    public void vertifyPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码");
            return;
        }
        KVUtils.put(KVConstants.KEY_REGISTER_PASSWORD, password);
        ((IRegisterPassword) mView).vertifyPasswordSuccess();
    }
}
