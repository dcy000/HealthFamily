package com.gcml.module_login_and_register.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.module_login_and_register.R;
import com.gcml.module_login_and_register.api.LoginRegisterRouterApi;
import com.gcml.module_login_and_register.presenter.ILoginView;
import com.gcml.module_login_and_register.presenter.LoginPresenter;
import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import timber.log.Timber;

@Route(path = "/login/phone")
public class LoginActivity extends StateBaseActivity implements View.OnClickListener, ILoginView {
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
    private LoginPresenter loginPresenter;

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions();
    }

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
        mBtnChangeFaceLogin.setOnClickListener(this);
        showSuccess();
        mToolbar.setVisibility(View.GONE);


    }

    @Override
    public IPresenter obtainPresenter() {
        loginPresenter = new LoginPresenter(this);
        return loginPresenter;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_login) {
            loginPresenter.login(mEtLoginUsername.getText().toString().trim(), mEtLoginPassword.getText().toString().trim());
        } else if (i == R.id.tv_regist_account) {
            Routerfit.register(LoginRegisterRouterApi.class).skipChooseRegisterRoleActivity();
        } else if (i == R.id.tv_forget_password) {
            Routerfit.register(LoginRegisterRouterApi.class).skipForgetPasswordActivity();
        } else if (i == R.id.btn_change_face_login) {
            Routerfit.register(LoginRegisterRouterApi.class).skipFaceBdSignInActivity(false, "", new ActivityCallback() {
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

    private void requestPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.requestEach(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                .subscribe(new CommonObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {

                        } else {
                            ToastUtils.showLong("请同意相关权限后，再次打开应用");
                        }
                    }
                });
    }


    @Override
    public void loginWithGuardianshipSuccess() {
        Routerfit.register(LoginRegisterRouterApi.class).skipMainActivity();
        ToastUtils.showShort("登录成功");
        finish();
    }

    @Override
    public void loginWithGuardianshipError() {

    }
}
