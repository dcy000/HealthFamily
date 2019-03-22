package com.gcml.module_login_and_register.presenter;

import android.text.TextUtils;

import com.gcml.module_login_and_register.api.LoginApi;
import com.gcml.module_login_and_register.bean.RegisterPutBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class SocialInputNamePresenter extends BasePresenter {

    public SocialInputNamePresenter(IView view) {
        super(view);
    }

    public void vertifyInfo(String userName, String community) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(community)) {
            ToastUtils.showShort("请完善信息");
            return;
        }
        KVUtils.put(KVConstants.KEY_REGISTER_NAME, userName);
        KVUtils.put(KVConstants.KEY_REGISTER_COMMUNITY, community);
        register();
    }

    private void register() {
        RegisterPutBean registerPutBean = new RegisterPutBean();
        registerPutBean.setAccountType((String) KVUtils.get(KVConstants.KEY_ACCOUNT_TYPE, ""));
        registerPutBean.setCommunity((String) KVUtils.get(KVConstants.KEY_REGISTER_COMMUNITY, ""));
        registerPutBean.setGuardianName((String) KVUtils.get(KVConstants.KEY_REGISTER_NAME, ""));
        String mobileNum = (String) KVUtils.get(KVConstants.KEY_REGISTER_PHONE, "");
        registerPutBean.setMobileNum(mobileNum);
        String password = (String) KVUtils.get(KVConstants.KEY_REGISTER_PASSWORD, "");
        registerPutBean.setPassword(password);
        Timber.i("Register Bean>>>>>>" + registerPutBean.toString());
        Box.getRetrofit(LoginApi.class)
                .register(registerPutBean)
                .compose(RxUtils.httpResponseTransformer(false))
                .flatMap(new Function<Object, ObservableSource<UserEntity>>() {
                    @Override
                    public ObservableSource<UserEntity> apply(Object o) throws Exception {
                        return Box.getRetrofit(LoginApi.class)
                                .loginWithGuardianship(mobileNum, password)
                                .compose(RxUtils.httpResponseTransformer());
                    }
                })
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                        //更新用户系统数据
                        Box.getSessionManager().setUser(userEntity);
                        ToastUtils.showShort("注册成功");
                        ((IRegisterEditInfomation) mView).registerSuccess();
                    }
                });
    }
}
