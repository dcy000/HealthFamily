package com.gcml.module_login_and_register.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_login_and_register.R;
import com.gcml.module_login_and_register.api.LoginRegisterRouterApi;
import com.gcml.module_login_and_register.presenter.FamilyInputNamePresenter;
import com.gcml.module_login_and_register.presenter.IRegisterEditInfomation;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.PermissionUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.click.UnFastClickListener;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;
import com.tbruyelle.rxpermissions2.Permission;

@Route(path = "/register/FamilyInputName")
public class FamilyInputNameActivity extends StateBaseActivity<FamilyInputNamePresenter> implements IRegisterEditInfomation {

    /**  */
    private EditText mEtRegisterUsername;
    /**  */
    private EditText mEtRegisterPhone;
    /**  */
    private EditText mEtRegisterRelationship;
    /**
     * 下一步
     */
    private TextView mGotoNext;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_register_family_input_name;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("完善信息");
        mEtRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        mEtRegisterPhone = (EditText) findViewById(R.id.et_register_phone);
        mEtRegisterRelationship = (EditText) findViewById(R.id.et_register_relationship);
        mGotoNext = (TextView) findViewById(R.id.goto_next);
        mGotoNext.setOnClickListener(new UnFastClickListener(2000) {
            @Override
            public void onSingleClick(View v) {
                getP().vertifyInformation(mEtRegisterUsername.getText().toString().trim());
            }

            @Override
            public void onFastClick(View v, long interval) {

            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return new FamilyInputNamePresenter(this);
    }


    @Override
    public void registerSuccess() {
        PermissionUtils.requestEach(this,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe(new CommonObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        String message = e.getMessage();
                        if (!TextUtils.isEmpty(message)) {
                            ToastUtils.showLong(message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        registerFace();
                    }
                });

    }

    private void registerFace() {
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
