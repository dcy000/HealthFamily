package com.gzq.lib_resource.mvp.base;

import android.app.Activity;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yokeyword.fragmentation.SupportFragment;


public abstract class BaseFragment<V extends IView, P extends IPresenter>
        extends SupportFragment implements IView {
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    protected P mPresenter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = layoutId(savedInstanceState);
        if (layoutId <= 0) {
            throw new IllegalArgumentException("layout is null");
        }
        if (mView == null) {
            mView = inflater.inflate(layoutId, container, false);
            initParams(getArguments());
            mPresenter = obtainPresenter();
//            if (mPresenter == null || !(mPresenter instanceof LifecycleObserver)) {
//                throw new IllegalArgumentException("obtain a wrong presenter");
//            }
            if (mPresenter!=null){
                getLifecycle().addObserver(mPresenter);
            }
            initView(mView);

        }
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mView != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
            mView = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView = null;
        mActivity = null;
        mContext = null;
        mPresenter = null;
    }

    public abstract int layoutId(Bundle savedInstanceState);

    public abstract void initParams(Bundle bundle);

    public abstract void initView(View view);

    public abstract P obtainPresenter();

}