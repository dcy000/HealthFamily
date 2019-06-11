package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.REUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

@Route(path = "/guardianship/add/resident/information")
public class AddResidentInformationActivity extends StateBaseActivity implements View.OnClickListener {
    /**
     * 姓名
     */
    private TextView mTipName;
    /**
     * 请输入居民的姓名
     */
    private TextView mEtName;
    private View mView1;
    /**
     * 手环手机号
     */
    private TextView mTipPhone;
    /**
     * 请输入手环的手机号
     */
    private TextView mEtHandringPhone;
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
    private String watchCode;
    private String phone;
    private String userId;

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
        watchCode = intentArgument.getStringExtra("watchCode");
        phone = intentArgument.getStringExtra("phone");
    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("添加居民信息");
        mTipName = (TextView) findViewById(R.id.tip_name);
        mEtName = (TextView) findViewById(R.id.et_name);
        mEtName.setText(watchCode);
        mView1 = (View) findViewById(R.id.view1);
        mTipPhone = (TextView) findViewById(R.id.tip_phone);
        mEtHandringPhone = (TextView) findViewById(R.id.et_handring_phone);
        mEtHandringPhone.setText(phone);
        mView2 = (View) findViewById(R.id.view2);
        mTipIdcard = (TextView) findViewById(R.id.tip_idcard);
        mEtIdcard = (EditText) findViewById(R.id.et_idcard);
        mBtnSure = (TextView) findViewById(R.id.btn_sure);
        mBtnSure.setOnClickListener(this);
    }


    @Override
    public IPresenter obtainPresenter() {
        return null;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_sure) {
            dealData();

        } else {
        }
    }

    private void dealData() {
        String idCard = mEtIdcard.getText().toString().trim();
        if (TextUtils.isEmpty(idCard) || !REUtils.validateIdCard(idCard)) {
            ToastUtils.showShort("请输入正确的身份证号码");
            return;
        }


        Box.getRetrofit(GuardianshipApi.class)
                .getUserInfoByIdcard(idCard)
                .compose(RxUtils.httpResponseTransformer(false))
                .flatMap((Function<ResidentBean, ObservableSource<Object>>) residentBean -> Box.getRetrofit(GuardianshipApi.class)
                        .bindPatient(watchCode, userId = residentBean.getBid() + "")
                        .compose(RxUtils.httpResponseTransformer()))
                .flatMap((Function<Object, ObservableSource<Object>>) o -> Box.getRetrofit(GuardianshipApi.class)
                        .addResident(userId, phone)
                        .compose(RxUtils.httpResponseTransformer()))
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("添加成功");
                        ActivityUtils.finishActivity();
                    }
                });
    }
}
