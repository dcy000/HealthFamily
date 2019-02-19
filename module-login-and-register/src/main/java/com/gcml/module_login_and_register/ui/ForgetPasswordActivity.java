package com.gcml.module_login_and_register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_login_and_register.R;
import com.gcml.module_login_and_register.api.LoginRegisterRouterApi;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/login/forgetPassword")
public class ForgetPasswordActivity extends StateBaseActivity implements View.OnClickListener {
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
        mTvTitle.setText("忘记密码");
        mEtRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        mEtRegisterCode = (EditText) findViewById(R.id.et_register_code);
        mBtnGetMessageCode = (TextView) findViewById(R.id.btn_get_message_code);
        mBtnGetMessageCode.setOnClickListener(this);
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
        if (i == R.id.btn_get_message_code) {
        } else if (i == R.id.goto_next) {
            Routerfit.register(LoginRegisterRouterApi.class).skipSetupNewPasswordActivity();
        } else {
        }
    }
}
