package com.gzq.lib_resource.utils;

import android.util.Log;

import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.BaseObserver;
import com.gzq.lib_core.utils.NetworkUtils;
import com.gzq.lib_core.utils.ToastUtils;


public abstract class LoadingObserver<T> extends BaseObserver<T> {
    private static final String TAG = "LoadingObserver";

    @Override
    public void onStart() {
        if (!NetworkUtils.isNetworkAvailable()) {
            ToastUtils.showShort("当前无网络，请检查网络情况");
            onComplete();

            // 无网络执行complete后取消注册防调用onError
            if (!isDisposed()) {
               dispose();
            }
        } else {
            LoadingViewUtils.show(null,false);
        }
    }
    @Override
    protected void onError(ApiException ex) {
        LoadingViewUtils.hide();
        ToastUtils.showShort(ex.message+":"+ex.code);
        Log.e(TAG, "onError: " + ex.message + "code: " + ex.code);
    }

    @Override
    public abstract void onNext(T t);

    
    @Override
    public void onComplete() {
        LoadingViewUtils.hide();
        Log.d(TAG, "onComplete: ");
    }
}
