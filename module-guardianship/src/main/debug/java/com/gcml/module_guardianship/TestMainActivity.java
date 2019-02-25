package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_resource.mvp.base.BaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;

/**
 * Created by gzq on 19-2-6.
 */

public class TestMainActivity extends BaseActivity {

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_test;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, new MainGuardianshipFragment())
                .commit();
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
    public void loadDataSuccess(Object... objects) {

    }

    @Override
    public void loadDataError(Object... objects) {

    }

    @Override
    public void loadDataEmpty() {

    }

    @Override
    public void onNetworkError() {

    }
}
