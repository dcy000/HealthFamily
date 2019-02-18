package com.gcml.module_login_and_register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.module_login_and_register.api.LoginRegisterRouterApi;
import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/login/phone")
public class LoginActivity extends StateBaseActivity implements View.OnClickListener {
    private ImageView mIvLoginLogo;
    /**  */
    private EditText mEtLoginUsername;
    /**  */
    private EditText mEtLoginPassword;
    /**
     * 登录
     */
    private TextView mBtnLogin;
    /**
     * 注册账号
     */
    private TextView mTvRegistAccount;
    /**
     * 忘记密码
     */
    private TextView mTvForgetPassword;
    /**
     * 切换人脸登录
     */
    private TextView mBtnChangeFaceLogin;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.login_activity_login;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, Box.getColor(R.color.white));
        mIvLoginLogo = (ImageView) findViewById(R.id.iv_login_logo);
        mEtLoginUsername = (EditText) findViewById(R.id.et_login_username);
        mEtLoginPassword = (EditText) findViewById(R.id.et_login_password);
        mBtnLogin = (TextView) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mTvRegistAccount = (TextView) findViewById(R.id.tv_regist_account);
        mTvRegistAccount.setOnClickListener(this);
        mTvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        mTvForgetPassword.setOnClickListener(this);
        mBtnChangeFaceLogin = (TextView) findViewById(R.id.btn_change_face_login);
        showSuccess();
        mToolbar.setVisibility(View.GONE);


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
        if (i == R.id.btn_login) {
            Routerfit.register(LoginRegisterRouterApi.class).skipMainActivity();
        } else if (i == R.id.tv_regist_account) {
            Routerfit.register(LoginRegisterRouterApi.class).skipRegisterMessageCode();
        } else if (i == R.id.tv_forget_password) {
        } else {
        }
    }
}
