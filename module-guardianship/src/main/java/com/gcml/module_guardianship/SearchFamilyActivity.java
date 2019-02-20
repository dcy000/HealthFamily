package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/guardianship/search/family")
public class SearchFamilyActivity extends StateBaseActivity implements View.OnClickListener {
    /**
     * 请输入联系人手机号码
     */
    private EditText mEtPhone;
    /**
     * 取消
     */
    private TextView mTvCancel;
    private RecyclerView mRvFamily;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_search_family;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mToolbar.setVisibility(View.GONE);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(this);
        mRvFamily = (RecyclerView) findViewById(R.id.rv_family);
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
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            finish();
        } else {
        }
    }
}
