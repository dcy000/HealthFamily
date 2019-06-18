package com.ml.module_shouhuan.activity;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.gzq.lib_resource.app.AppStore;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.ScreenUtils;
import com.ml.module_shouhuan.R;
import com.ml.module_shouhuan.adapter.PageFragmentAdapter;
import com.ml.module_shouhuan.api.ShouhuanRouterApi;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

import timber.log.Timber;

@Route(path = "/sosdeal/main")
public class MsgShowFragment extends StateBaseFragment {
    private SlidingTabLayout mTitleTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> msgFragments = new ArrayList<>();
    private String[] titleString;
    private RelativeLayout mRl;
    private LinearLayout mLlContainer;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_msg_show;
    }

    @Override
    public void initParams(Bundle bundle) {
        showSuccess();
        StateBaseFragment msgToDoFragment = Routerfit.register(ShouhuanRouterApi.class).getMsgToDoFragment();
        msgFragments.add(msgToDoFragment);
        StateBaseFragment msgAlreadyDoneFragment = Routerfit.register(ShouhuanRouterApi.class).getMsgAlreadyDoneFragment();
        msgFragments.add(msgAlreadyDoneFragment);
        Timber.e("次消息界面"+msgToDoFragment);
//        msgFragments.add(Routerfit.register(ShouhuanRouterApi.class).getMsgSystemFragment());
        titleString = getResources().getStringArray(R.array.title_msg);
    }

    @Override
    public void initView(View view) {
        mViewPager = view.findViewById(R.id.vp_msg);
        mTitleTabLayout = view.findViewById(R.id.layout_tab);
        mTitleTabLayout.setViewPager(mViewPager,titleString,getActivity(),msgFragments);
        mRl = view.findViewById(R.id.rl);
        mLlContainer = view.findViewById(R.id.ll_container);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mRl.getLayoutParams());
        layoutParams.topMargin = ScreenUtils.getStatusBarHeight(mContext);
        mRl.setLayoutParams(layoutParams);
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
