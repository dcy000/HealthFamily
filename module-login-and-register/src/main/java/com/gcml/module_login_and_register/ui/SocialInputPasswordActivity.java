package com.gcml.module_login_and_register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_login_and_register.R;
import com.gcml.module_login_and_register.api.LoginRegisterRouterApi;
import com.gcml.module_login_and_register.presenter.IRegisterPassword;
import com.gcml.module_login_and_register.presenter.RegisterPassword;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/register/inputpassword")
public class SocialInputPasswordActivity extends StateBaseActivity implements View.OnClickListener ,IRegisterPassword{
    /**  */
    private EditText mEtRegisterPassword;
    /**
     * 下一步
     */
    private TextView mGotoNext;
    private RegisterPassword registerPassword;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_register_input_password;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("输入密码");
        mEtRegisterPassword = (EditText) findViewById(R.id.et_register_password);
        mGotoNext = (TextView) findViewById(R.id.goto_next);
        mGotoNext.setOnClickListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        registerPassword = new RegisterPassword(this);
        return registerPassword;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.goto_next) {
            registerPassword.vertifyPassword(mEtRegisterPassword.getText().toString().trim());
        } else {

        }
    }

    @Override
    public void vertifyPasswordSuccess() {
        Routerfit.register(LoginRegisterRouterApi.class).skipSocialInputNameActivity();
    }

}
