package com.gcml.module_login_and_register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_login_and_register.R;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/register/inputName")
public class SocialInputNameActivity extends StateBaseActivity implements View.OnClickListener {
    private EditText mEtRegisterUsername;
    /**  */
    private EditText mEtRegisterCommunity;
    /**
     * 下一步
     */
    private TextView mGotoNext;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_register_input_name;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("完善信息");
        mEtRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        mEtRegisterCommunity = (EditText) findViewById(R.id.et_register_community);
        mGotoNext = (TextView) findViewById(R.id.goto_next);
        mGotoNext.setOnClickListener(this);
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
        if (i == R.id.goto_next) {

        } else {
        }
    }
}
