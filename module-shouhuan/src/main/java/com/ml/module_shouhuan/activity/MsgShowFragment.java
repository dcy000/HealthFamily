package com.ml.module_shouhuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.ml.module_shouhuan.R;
import com.ml.module_shouhuan.adapter.PageFragmentAdapter;
import com.ml.module_shouhuan.api.AppRouterApi;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

@Route(path = "/sosdeal/main")
public class MsgShowFragment extends StateBaseFragment {
    private TabLayout mTitleTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> msgFragments = new ArrayList<>();
    private String[] titleString;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_msg_show;
    }

    @Override
    public void initParams(Bundle bundle) {
        msgFragments.add(Routerfit.register(AppRouterApi.class).getMsgAlreadyDoneFragment());
        msgFragments.add(Routerfit.register(AppRouterApi.class).getMsgToDoFragment());
        msgFragments.add(Routerfit.register(AppRouterApi.class).getMsgSystemFragment());
        titleString = getResources().getStringArray(R.array.title_msg);
    }

    @Override
    public void initView(View view) {
        mViewPager = view.findViewById(R.id.vp_msg);
        mTitleTabLayout = view.findViewById(R.id.layout_tab);
        mViewPager.setAdapter(new PageFragmentAdapter(getFragmentManager(), msgFragments, titleString));
        mTitleTabLayout.setupWithViewPager(mViewPager);
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
