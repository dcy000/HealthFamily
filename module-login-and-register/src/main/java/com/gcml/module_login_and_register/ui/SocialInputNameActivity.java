package com.gcml.module_login_and_register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_login_and_register.R;
import com.gcml.module_login_and_register.api.LoginRegisterRouterApi;
import com.gcml.module_login_and_register.presenter.IRegisterEditInfomation;
import com.gcml.module_login_and_register.presenter.SocialInputNamePresenter;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.click.UnFastClickListener;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/register/inputName")
public class SocialInputNameActivity extends StateBaseActivity implements IRegisterEditInfomation {
    private EditText mEtRegisterUsername;
    /**  */
    private EditText mEtRegisterCommunity;
    /**
     * 下一步
     */
    private TextView mGotoNext;
    private SocialInputNamePresenter socialInputNamePresenter;

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
        mGotoNext.setOnClickListener(new UnFastClickListener(2000) {
            @Override
            public void onSingleClick(View v) {
                socialInputNamePresenter.vertifyInfo(
                        mEtRegisterUsername.getText().toString().trim(),
                        mEtRegisterCommunity.getText().toString().trim());
            }

            @Override
            public void onFastClick(View v, long interval) {

            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        socialInputNamePresenter = new SocialInputNamePresenter(this);
        return socialInputNamePresenter;
    }


    @Override
    public void registerSuccess() {
        Routerfit.register(CommonRouterApi.class).skipFaceBdSignUpActivity(new ActivityCallback() {
            @Override
            public void onActivityResult(int result, Object data) {
                if (data.toString().equals("success")) {
                    Routerfit.register(LoginRegisterRouterApi.class).skipMainActivity();
                } else {
                    ToastUtils.showShort(data.toString());
                }
            }
        });
    }

}
