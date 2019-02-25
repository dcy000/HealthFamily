package com.ml.module_shouhuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.ml.module_shouhuan.R;
import com.ml.module_shouhuan.adapter.PageFragmentAdapter;
import com.ml.module_shouhuan.api.AppRouterApi;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

@Route(path = "/shouhuan/msgShowActivity")
public class MsgShowActivity extends StateBaseActivity {
    private TabLayout mTitleTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> msgFragments = new ArrayList<>();
    private String[] titleString;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_msg_show;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        msgFragments.add(Routerfit.register(AppRouterApi.class).getMsgAlreadyDoneFragment());
        msgFragments.add(Routerfit.register(AppRouterApi.class).getMsgToDoFragment());
        msgFragments.add(Routerfit.register(AppRouterApi.class).getMsgSystemFragment());
        titleString = getResources().getStringArray(R.array.title_msg);
    }

    @Override
    public void initView() {
        mViewPager = findViewById(R.id.vp_msg);
        mTitleTabLayout = findViewById(R.id.layout_tab);
        mViewPager.setAdapter(new PageFragmentAdapter(getSupportFragmentManager(), msgFragments, titleString));
        mTitleTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
