package com.gzq.lib_resource.mvp;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzq.lib_core.utils.NetworkUtils;
import com.gzq.lib_resource.R;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.mvp.base.BaseFragment;
import com.gzq.lib_resource.state_page.EmptyPage;
import com.gzq.lib_resource.state_page.ErrorPage;
import com.gzq.lib_resource.state_page.LoadingPage;
import com.gzq.lib_resource.state_page.NetErrorPage;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;

/**
 * created on 2018/10/31 10:24
 * created by: gzq
 * description: 二次封装的带状态页面的BaseFragment
 */
public abstract class StateBaseFragment extends BaseFragment {
    protected LoadService mStateView;
    private FDialog progressLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = layoutId(savedInstanceState);
        if (layoutId <= 0) {
            throw new IllegalArgumentException("layout is null");
        }
        if (mView == null) {
            mView = inflater.inflate(layoutId, container, false);
//            //初始化状态页面
//            Object rootView = placeView();
//            if (rootView == null) {
//                rootView = mView;
//            }
//            initStateView(rootView);
            //初始化基本参数
            initParams(getArguments());
            //初始化Presenter
            mPresenter = obtainPresenter();
//            if (mPresenter == null || !(mPresenter instanceof LifecycleObserver)) {
//                throw new IllegalArgumentException("obtain a wrong presenter");
//            }
            if (mPresenter != null) {
                getLifecycle().addObserver(mPresenter);
            }
            //初始化控件id
            initView(mView);
        }
        //判断网络是否可用
        if (!NetworkUtils.isAvailable()) {
            showNetError();
        }
        return mView;
    }

    @CallSuper
    @Override
    public void loadDataSuccess(Object... objects) {
        showSuccess();
    }

    @Override
    public void loadDataError(Object... objects) {
        showError();
    }

    @Override
    public void loadDataEmpty() {
        showEmpty();
    }

    @Override
    public void onNetworkError() {
        showNetError();
    }

    private void initStateView(Object rootView) {
        mStateView = LoadSir.getDefault().register(rootView, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                reloadData(v);
            }
        }).setCallBack(EmptyPage.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                customEmptyPage(context, view);
            }
        }).setCallBack(ErrorPage.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                customErrorPage(context, view);
            }
        }).setCallBack(LoadingPage.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                customLoadingPage(context, view);
            }
        }).setCallBack(NetErrorPage.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                customNetErrorPage(context, view);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissProgressLoading();
    }

    protected View placeView() {
        return null;
    }

    protected void customEmptyPage(Context context, View view) {

    }

    protected void customErrorPage(Context context, View view) {

    }

    protected void customLoadingPage(Context context, View view) {

    }

    protected void customNetErrorPage(Context context, View view) {

    }

    /**
     * 重试
     *
     * @param view
     */
    public void reloadData(View view) {

    }

    public void showLoading() {
        if (mStateView != null) {
            mStateView.showCallback(LoadingPage.class);
        }
    }

    public void showEmpty() {
        if (mStateView != null) {
            mStateView.showCallback(EmptyPage.class);
        }
    }

    public void showError() {
        if (mStateView != null) {
            mStateView.showCallback(ErrorPage.class);
        }
    }

    public void showSuccess() {
        if (mStateView != null) {
            mStateView.showSuccess();
        }
    }

    public void showNetError() {
        if (mStateView != null) {
            mStateView.showCallback(NetErrorPage.class);
        }
    }

    public void showProgressLoading() {
        progressLoadingDialog = FDialog.build()
                .setSupportFM(getFragmentManager())
                .setLayoutId(R.layout.dialog_layout_loading)
                .setDimAmount(1)
                .show();
    }

    public void dismissProgressLoading() {
        if (progressLoadingDialog != null) {
            progressLoadingDialog.dismiss();
        }
        progressLoadingDialog = null;
    }
}
