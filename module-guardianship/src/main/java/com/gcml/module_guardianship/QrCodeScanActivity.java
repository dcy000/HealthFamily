package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import cn.bingoogolapple.qrcode.zxing.ZXingView;

@Route(path = "/guardianship/qrcode/scan")
public class QrCodeScanActivity extends StateBaseActivity implements View.OnClickListener {
    private ZXingView mZxingview;
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
        mZxingview = (ZXingView) findViewById(R.id.zxingview);
        mFinishPage = (ImageView) findViewById(R.id.finish_page);
        mFinishPage.setOnClickListener(this);
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
        if (i == R.id.finish_page) {
//            finish();
            Routerfit.register(GuardianshipRouterApi.class).skipAddResidentInformationActivity();
        } else {
        }
    }
}
