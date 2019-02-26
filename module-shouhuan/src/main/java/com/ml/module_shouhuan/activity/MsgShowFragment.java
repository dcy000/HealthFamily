package com.ml.module_shouhuan.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.ScreenUtils;
import com.ml.module_shouhuan.R;
import com.ml.module_shouhuan.adapter.PageFragmentAdapter;
import com.ml.module_shouhuan.api.ShouhuanRouterApi;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

@Route(path = "/sosdeal/main")
public class MsgShowFragment extends StateBaseFragment {
    private TabLayout mTitleTabLayout;
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
        msgFragments.add(Routerfit.register(ShouhuanRouterApi.class).getMsgToDoFragment());
        msgFragments.add(Routerfit.register(ShouhuanRouterApi.class).getMsgAlreadyDoneFragment());
        msgFragments.add(Routerfit.register(ShouhuanRouterApi.class).getMsgSystemFragment());
        titleString = getResources().getStringArray(R.array.title_msg);
    }

    @Override
    public void initView(View view) {
        mViewPager = view.findViewById(R.id.vp_msg);
        mTitleTabLayout = view.findViewById(R.id.layout_tab);
        mViewPager.setAdapter(new PageFragmentAdapter(getFragmentManager(), msgFragments, titleString));
        mTitleTabLayout.setupWithViewPager(mViewPager);
        mRl = view.findViewById(R.id.rl);
        mLlContainer = view.findViewById(R.id.ll_container);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mRl.getLayoutParams());
        layoutParams.topMargin = ScreenUtils.getStatusBarHeight(mContext);
        mRl.setLayoutParams(layoutParams);
    }


    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
            @Override
            public void preData(Object... objects) {

            }

            @Override
            public void refreshData(Object... objects) {

            }

            @Override
            public void loadMoreData(Object... objects) {

            }
        };
    }
}
