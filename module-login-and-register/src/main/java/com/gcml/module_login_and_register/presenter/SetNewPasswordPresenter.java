package com.gcml.module_login_and_register.presenter;

import android.text.TextUtils;

import com.gcml.module_login_and_register.api.LoginApi;
import com.gcml.module_login_and_register.ui.ForgetPasswordActivity;
import com.gcml.module_login_and_register.ui.SetupNewPasswordActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;

public class SetNewPasswordPresenter extends BasePresenter {
    public SetNewPasswordPresenter(IView view) {
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

    public void gotoNext(String newpassword) {
        if (TextUtils.isEmpty(newpassword)) {
            ToastUtils.showShort("新密码不能为空");
            return;
        }
        String phone = (String) KVUtils.get(KVConstants.KEY_FORGET_PASSWORD_PHONE, "");
        Box.getRetrofit(LoginApi.class)
                .setNewPassword(phone, newpassword)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("修改成功");
                        ActivityUtils.finishActivity();
                    }
                });
    }
}
