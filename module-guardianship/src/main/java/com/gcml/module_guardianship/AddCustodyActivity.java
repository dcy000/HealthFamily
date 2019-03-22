package com.gcml.module_guardianship;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_guardianship.presenter.AddCustodyPresenter;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/add/custody/activity")
public class AddCustodyActivity extends StateBaseActivity<AddCustodyPresenter> implements View.OnClickListener {
    /**
     * 请输入监护人的姓名
     */
    private EditText mEtName;
    /**
     * 请输入监护人的手机号
     */
    private EditText mEtPhone;
    /**
     * 家属
     */
    private TextView mFlagFamily;
    /**
     * 社工
     */
    private TextView mFlagSocial;
    /**
     * 保存
     */
    private TextView mBtnSave;
    private String userId;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_add_custody;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        userId = intentArgument.getStringExtra("userId");
    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("新增监护人");
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mFlagFamily = (TextView) findViewById(R.id.flag_family);
        mFlagFamily.setOnClickListener(this);
        mFlagSocial = (TextView) findViewById(R.id.flag_social);
        mFlagSocial.setOnClickListener(this);
        mBtnSave = (TextView) findViewById(R.id.btn_save);
        mBtnSave.setOnClickListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new AddCustodyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.flag_family) {
            mFlagFamily.setSelected(true);
            mFlagSocial.setSelected(false);
            mFlagFamily.setTextColor(Box.getColor(R.color.colorAccent));
            mFlagSocial.setTextColor(Color.parseColor("#C0C4CC"));
        } else if (i == R.id.flag_social) {
            mFlagFamily.setSelected(false);
            mFlagSocial.setSelected(true);
            mFlagFamily.setTextColor(Color.parseColor("#C0C4CC"));
            mFlagSocial.setTextColor(Box.getColor(R.color.colorAccent));
        } else if (i == R.id.btn_save) {
            getP().saveCustody(mEtName.getText().toString().trim(),
                    mEtPhone.getText().toString().trim(),
                    mFlagFamily.isSelected(), mFlagSocial.isSelected(), userId);
        } else {
        }
    }
}
