package com.gcml.module_login_and_register.presenter;

import android.text.TextUtils;

import com.gcml.module_login_and_register.api.LoginApi;
import com.gcml.module_login_and_register.api.LoginRegisterRouterApi;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;
import com.gzq.lib_resource.utils.REUtils;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

public class LoginPresenter extends BasePresenter {
    public LoginPresenter(IView view) {
        super(view);
    }

    public void login(String userName, String password) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            ToastUtils.showLong("账号或者密码不能为空");
            return;
        }
        if (!REUtils.isMobile(userName)) {
            ToastUtils.showLong("请输入正确的手机号码");
            return;
        }

        Box.getRetrofit(LoginApi.class)
                .loginWithGuardianship(userName, password)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        //更新用户系统信息
                        Box.getSessionManager().setUser(userEntity);
                        ((ILoginView) mView).loginWithGuardianshipSuccess();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.showShort("手机号或密码错误");
                    }

                    @Override
                    protected void onNetError() {
                        super.onNetError();
                        mView.onNetworkError();
                    }
                });
    }
}
