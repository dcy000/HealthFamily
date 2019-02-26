package com.ml.module_shouhuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.ml.module_shouhuan.R;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/shouhuan/healthdata")
public class HealthDataActivity extends StateBaseActivity implements View.OnClickListener{
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_health_data_layout;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
