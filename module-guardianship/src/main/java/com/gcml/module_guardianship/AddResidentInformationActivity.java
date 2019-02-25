package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/guardianship/add/resident/information")
public class AddResidentInformationActivity extends StateBaseActivity implements View.OnClickListener {
    /**
     * 姓名
     */
    private TextView mTipName;
    /**
     * 请输入居民的姓名
     */
    private EditText mEtName;
    private View mView1;
    /**
     * 手环手机号
     */
    private TextView mTipPhone;
    /**
     * 请输入手环的手机号
     */
    private EditText mEtHandringPhone;
    private View mView2;
    /**
     * 身份证号
     */
    private TextView mTipIdcard;
    /**
     * 请输入居民的身份证号
     */
    private EditText mEtIdcard;
    /**
     * 确认
     */
    private TextView mBtnSure;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_add_resident_information;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("添加居民信息");
        mTipName = (TextView) findViewById(R.id.tip_name);
        mEtName = (EditText) findViewById(R.id.et_name);
        mView1 = (View) findViewById(R.id.view1);
        mTipPhone = (TextView) findViewById(R.id.tip_phone);
        mEtHandringPhone = (EditText) findViewById(R.id.et_handring_phone);
        mView2 = (View) findViewById(R.id.view2);
        mTipIdcard = (TextView) findViewById(R.id.tip_idcard);
        mEtIdcard = (EditText) findViewById(R.id.et_idcard);
        mBtnSure = (TextView) findViewById(R.id.btn_sure);
        mBtnSure.setOnClickListener(this);
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
        if (i == R.id.btn_sure) {
            Routerfit.register(GuardianshipRouterApi.class).skipAddRelationshipActivity();
        } else {
        }
    }
}
