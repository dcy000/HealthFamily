package com.gcml.module_login_and_register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_login_and_register.R;
import com.gcml.module_login_and_register.api.LoginRegisterRouterApi;
import com.gcml.module_login_and_register.presenter.IMessageCodeView;
import com.gcml.module_login_and_register.presenter.MessageCodePresenter;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/register/messagecode")
public class SocialRegisterMessageCodeActivity extends StateBaseActivity<MessageCodePresenter> implements View.OnClickListener, IMessageCodeView {
    /**  */
    private EditText mEtRegisterUsername;
    /**  */
    private EditText mEtRegisterCode;
    /**
     * 获取验证码
     */
    private TextView mBtnGetMessageCode;
    /**
     * 下一步
     */
    private TextView mGotoNext;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_register_message_code;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("社工注册");
        mEtRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        mEtRegisterCode = (EditText) findViewById(R.id.et_register_code);
        mBtnGetMessageCode = (TextView) findViewById(R.id.btn_get_message_code);
        mBtnGetMessageCode.setOnClickListener(this);
        mGotoNext = (TextView) findViewById(R.id.goto_next);
        mGotoNext.setOnClickListener(this);

    }

    @Override
    public IPresenter obtainPresenter() {
        return new MessageCodePresenter(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_get_message_code) {
            getP().getMessageCode(mEtRegisterUsername.getText().toString().trim());
        } else if (i == R.id.goto_next) {
            getP().gotoNextPage(mEtRegisterCode.getText().toString().trim());
        } else {
        }
    }

    private void setBtnMessageCodeStatus(boolean enable, String btnText) {
        mBtnGetMessageCode.setEnabled(enable);
        mBtnGetMessageCode.setText(btnText);
        if (enable) {
            mBtnGetMessageCode.setBackgroundResource(R.drawable.selector_btn_blue);
        } else {
            mBtnGetMessageCode.setBackgroundResource(R.drawable.selector_btn_gray);
        }
    }

    @Override
    public void setButtonGetMessageCodeStatus(boolean status, String btnText) {
        setBtnMessageCodeStatus(status, btnText);
    }

    @Override
    public void vertifyCodeSuccess() {
        Routerfit.register(LoginRegisterRouterApi.class).skipSocialInputPasswordActivity();
    }

}
