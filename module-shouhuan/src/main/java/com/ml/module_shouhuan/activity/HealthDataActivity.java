package com.ml.module_shouhuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;

public class HealthDataActivity extends StateBaseActivity implements View.OnClickListener{
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return 0;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
