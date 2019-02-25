package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;

public class ResidentLocationDetailActivity extends StateBaseActivity {

    private View mMapView;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_resident_location_details;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mMapView = findViewById(R.id.mapview);
        mMapView.setOnClickListener(this);
        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
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
        if (v.getId()==R.id.mapview){
            ToastUtils.showShort("点击了mapview");
        }
    }
}
