package com.gcml.module_login_and_register.presenter;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import com.gcml.module_login_and_register.api.LoginApi;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;
import com.gzq.lib_resource.utils.REUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ForgetPasswordPresenter extends BasePresenter {
    private Disposable countDownDisposable = Disposables.empty();
    private String mCode;
    private String mPhone;

    public ForgetPasswordPresenter(IView view) {
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

    public void checkPhone(String phone, boolean isCheck) {
        if (TextUtils.isEmpty(phone) || !REUtils.isMobile(phone)) {
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }
        //检查手机号码是否是注册过的
        Box.getRetrofit(LoginApi.class)
                .getMsgCode(phone, isCheck)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        if (isCheck) {
                            ToastUtils.showShort("该手机号码未注册，请输入正确的手机号码");
                        } else {
                            mCode = s;
                            mPhone = phone;
                            startTimer();
                            ToastUtils.showShort("获取验证码成功");
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        if (ex.code == 1035) {
                            //该手机号码是注册过的，可以找回密码
                            checkPhone(phone, false);
                        }
                    }
                });

    }

    private void startTimer() {
        countDownDisposable = RxUtils.rxCountDown(1, 60)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        ((IMessageCodeView) mView).setButtonGetMessageCodeStatus(false, "60s");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        ((IMessageCodeView) mView).setButtonGetMessageCodeStatus(true, "获取验证码");
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        //60s已过
                        mCode = null;
                        ((IMessageCodeView) mView).setButtonGetMessageCodeStatus(true, "获取验证码");
                    }
                })
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribeWith(new CommonObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        if (integer == 1) {
                            //60s已过
                            mCode = null;
                        }
                        ((IMessageCodeView) mView).setButtonGetMessageCodeStatus(false, integer + "s");
                    }
                });
    }

    public void gotoNextPage(String code) {
        if (TextUtils.isEmpty(mCode)) {
            ToastUtils.showShort("请点击按钮获取验证码");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort("请输入正确的验证码");
            return;
        }

        if (code.equals(mCode)) {
            ToastUtils.showShort("验证通过");
            KVUtils.put(KVConstants.KEY_FORGET_PASSWORD_PHONE, mPhone);
            ((IMessageCodeView) mView).vertifyCodeSuccess();
        }
    }
}
