package com.gcml.module_login_and_register.presenter;

import android.arch.lifecycle.LifecycleOwner;
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

public class MessageCodePresenter extends BasePresenter {
    private String mCode;
    private String mPhone;

    public MessageCodePresenter(IView view) {
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

    public void getMessageCode(String phone) {
        if (TextUtils.isEmpty(phone) || !REUtils.isMobile(phone)) {
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }
        mPhone = phone;
        Box.getRetrofit(LoginApi.class)
                .getMsgCode(phone, true)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        startTimer();
                        ToastUtils.showShort("获取验证码成功");
                        mCode = s;
                    }
                });

    }

    private Disposable countDownDisposable = Disposables.empty();

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
                        ((IMessageCodeView) mView).setButtonGetMessageCodeStatus(true, "获取验证码");
                    }
                })
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribeWith(new CommonObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        ((IMessageCodeView) mView).setButtonGetMessageCodeStatus(false, integer + "s");
                    }
                });
    }

    public void gotoNextPage(String inputCode) {
        if (TextUtils.isEmpty(mCode)) {
            ToastUtils.showShort("请点击按钮获取验证码");
            return;
        }
        if (TextUtils.isEmpty(inputCode)) {
            ToastUtils.showShort("请输入正确的验证码");
            return;
        }

        if (inputCode.equals(mCode)) {
            ToastUtils.showShort("验证通过");
            KVUtils.put(KVConstants.KEY_REGISTER_PHONE, mPhone);
            ((IMessageCodeView) mView).vertifyCodeSuccess();
        }
    }


    @Override
    public void onDestroy(LifecycleOwner owner) {
        super.onDestroy(owner);
        countDownDisposable.dispose();
    }

}
