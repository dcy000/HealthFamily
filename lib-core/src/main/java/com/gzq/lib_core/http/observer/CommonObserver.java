package com.gzq.lib_core.http.observer;

import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.utils.ToastUtils;


public abstract class CommonObserver<T> extends BaseObserver<T> {
    private static final String TAG = "CommonObserver";

    @Override
    protected void onError(ApiException ex) {
        ToastUtils.showShort(ex.message + ":" + ex.code);
        switch (ex.code) {
            case 9001:
            case 9002:
                onEmptyData();
                break;
        }
    }

    @Override
    protected void onNetError() {
        ToastUtils.showShort("当前无网络，请检查网络情况");
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onComplete() {

    }

    protected void onEmptyData() {

    }
}
