package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.REUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/guardianship/add/relationship")
public class AddRelationshipActivity extends StateBaseActivity {
    /**
     * 15181679808
     */
    private EditText mTvPhone;
    /**
     * 确认
     */
    private TextView mBtnSure;
    private WatchInformationBean watchInfo;
    private String watchCode;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_add_relationship;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        watchInfo = intentArgument.getParcelableExtra("watchInfo");
        watchCode = intentArgument.getStringExtra("watchCode");
    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("添加居民信息");
        mTvPhone = (EditText) findViewById(R.id.tv_phone);
        mBtnSure = (TextView) findViewById(R.id.btn_sure);
        mBtnSure.setOnClickListener(this);
        fillData();
    }

    private void fillData() {
        if (watchInfo != null) {
            mTvPhone.setText(watchInfo.getDeviceMobileNo());
        } else {
            ToastUtils.showShort("程序异常");
        }
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.btn_sure) {
            String phone = mTvPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone) || !REUtils.isMobile(phone) || !watchInfo.getDeviceMobileNo().equals(phone)) {
                ToastUtils.showShort("手机号码不正确");
                return;
            }
            if (watchInfo != null) {
                addResident();
            } else {
                Routerfit.register(GuardianshipRouterApi.class).skipAddResidentInformationActivity(watchCode, phone);
            }
        }
    }

    private void addResident() {
        UserEntity user = Box.getSessionManager().getUser();
        Box.getRetrofit(GuardianshipApi.class)
                .addResident(watchInfo.getUserid() + "", user.getPhone())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("添加成功");
                        ActivityUtils.finishActivity(QrCodeScanActivity.class);
                        ActivityUtils.finishActivity(AddRelationshipActivity.class);
                    }
                });
    }
}
