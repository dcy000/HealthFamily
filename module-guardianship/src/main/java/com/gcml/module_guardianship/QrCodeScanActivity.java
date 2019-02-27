package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

@Route(path = "/guardianship/qrcode/scan")
public class QrCodeScanActivity extends StateBaseActivity implements View.OnClickListener, QRCodeView.Delegate {
    private ZBarView mZxingview;
    private ImageView mFinishPage;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_qr_code_scan;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mToolbar.setVisibility(View.GONE);
        mZxingview = (ZBarView) findViewById(R.id.zxingview);
        mFinishPage = (ImageView) findViewById(R.id.finish_page);
        mFinishPage.setOnClickListener(this);
        mZxingview.setDelegate(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.finish_page) {
            finish();
//            Routerfit.register(GuardianshipRouterApi.class).skipAddResidentInformationActivity();
        } else {
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        //查新用户信息
        Box.getRetrofit(GuardianshipApi.class)
                .getWatchInfo(result)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<WatchInformationBean>() {
                    @Override
                    public void onNext(WatchInformationBean watchInformationBean) {
                        //查到了用户信息
                        Routerfit.register(GuardianshipRouterApi.class).skipAddRelationshipActivity(watchInformationBean);
                    }
                });
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtils.showShort("扫描失败");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZxingview.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mZxingview.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mZxingview.onDestroy();
    }
}
