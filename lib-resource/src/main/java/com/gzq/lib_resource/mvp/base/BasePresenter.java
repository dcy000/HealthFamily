package com.gzq.lib_resource.mvp.base;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.CallSuper;

/**
 * Created by afirez on 2017/7/12.
 */

public abstract class BasePresenter<V extends IView>
        implements IPresenter {
    protected V mView;

    protected LifecycleOwner mLifecycleOwner;

    public BasePresenter(V view) {
        this.mView = view;
        if (view instanceof LifecycleOwner) {
            this.mLifecycleOwner = (LifecycleOwner) view;
        }
    }

    @CallSuper
    @Override
    public void onCreate(LifecycleOwner owner) {

    }

    @Override
    public void onStart(LifecycleOwner owner) {

    }

    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {

    }

    @Override
    public void onStop(LifecycleOwner owner) {

    }

    @CallSuper
    @Override
    public void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
        mView = null;
        mLifecycleOwner = null;
    }

}