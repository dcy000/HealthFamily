package com.gcml.module_health_manager;

import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/healthmanager/health/video")
public class HealthVideoActivity extends StateBaseActivity {
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_health_video;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        showDeveloping();
        getTitleTextView().setText("健康宣教");
    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
