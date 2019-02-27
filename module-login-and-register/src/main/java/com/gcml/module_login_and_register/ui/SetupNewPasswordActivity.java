package com.gcml.module_login_and_register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_login_and_register.R;
import com.gcml.module_login_and_register.presenter.SetNewPasswordPresenter;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/setup/newPassword")
public class SetupNewPasswordActivity extends StateBaseActivity implements View.OnClickListener {
    /**  */
    private EditText mEtNewPassword;
    /**
     * 下一步
     */
    private TextView mGotoNext;
    private SetNewPasswordPresenter setNewPasswordPresenter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_setup_new_password;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("设置新密码");
        mEtNewPassword = (EditText) findViewById(R.id.et_new_password);
        mGotoNext = (TextView) findViewById(R.id.goto_next);
        mGotoNext.setOnClickListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        setNewPasswordPresenter = new SetNewPasswordPresenter(this);
        return setNewPasswordPresenter;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.goto_next) {
            setNewPasswordPresenter.gotoNext(mEtNewPassword.getText().toString().trim());
        } else {
        }
    }
}
