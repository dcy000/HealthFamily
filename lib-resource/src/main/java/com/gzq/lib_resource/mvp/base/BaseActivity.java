package com.gzq.lib_resource.mvp.base;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity<V extends IView, P extends IPresenter>
        extends SupportActivity implements IView {
    protected P mPresenter;
    protected View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = layoutId(savedInstanceState);
        if (layoutId <= 0) {
            throw new IllegalArgumentException("layout is null");
        }
        //设置布局
        setContentView(layoutId);
        //初始化状态页
        initStateView();
        //初始化基本参数
        initParams(getIntent(), getIntent().getExtras());
        //初始化Presenter
        mPresenter = obtainPresenter();
        if (mPresenter == null || !(mPresenter instanceof LifecycleObserver)) {
            throw new IllegalArgumentException("obtain a wrong presenter");
        }
        getLifecycle().addObserver(mPresenter);
        //初始化控件id
        initView();

    }

    /**
     * 该方法是为了扩展其他基础类而增加的，普通继承类可不重写该方法。
     */
    protected void initStateView() {
    }

    /**
     * 布局文件资源ID
     *
     * @param savedInstanceState
     * @return
     */
    public abstract int layoutId(Bundle savedInstanceState);

    /**
     * 初始化需要参数
     *
     * @param intentArgument 跳转Intent
     * @param bundleArgument 跳转传入的Bundle
     */
    public abstract void initParams(Intent intentArgument, Bundle bundleArgument);

    /**
     * 初始化布局文件
     */
    public abstract void initView();

    /**
     * 初始化Presenter
     *
     * @return
     */
    public abstract P obtainPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }
}
