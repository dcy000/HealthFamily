package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/guardianship/add/relationship")
public class AddRelationshipActivity extends StateBaseActivity {
    /**
     * 15181679808
     */
    private TextView mTvPhone;
    /**
     * 确认
     */
    private TextView mBtnSure;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_add_relationship;
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
        mTvTitle.setText("添加居民信息");
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mBtnSure = (TextView) findViewById(R.id.btn_sure);
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
