package com.gcml.module_mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.AppUtils;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/mine/about/us")
public class AboutUsActivity extends StateBaseActivity implements View.OnClickListener {
    /**
     * Version 1.0.0
     */
    private TextView mTvVersionCode;
    private LinearLayout mLlGoScore;
    private LinearLayout mLlOfficialWebsite;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_about_us;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("关于我们");
        mTvVersionCode = (TextView) findViewById(R.id.tv_version_code);
        mLlGoScore = (LinearLayout) findViewById(R.id.ll_go_score);
        mLlGoScore.setOnClickListener(this);
        mLlOfficialWebsite = (LinearLayout) findViewById(R.id.ll_official_website);
        mLlOfficialWebsite.setOnClickListener(this);
        mTvVersionCode.setText("Version " + AppUtils.getAppInfo().getVersionName());
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.ll_go_score) {
            ToastUtils.showShort("该功能暂未开放");
        } else if (i == R.id.ll_official_website) {
            ToastUtils.showShort("该功能暂未开放");
        } else {
        }
    }
}
