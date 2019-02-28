package com.gcml.module_health_manager;

import android.content.Intent;
import android.os.Bundle;

import com.gcml.module_health_manager.bean.FamilyDoctorServiceBean;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;

@Route(path = "/healthmanager/health/measure/detail")
public class HealthMeasureDetailActivity extends StateBaseActivity {
    private ArrayList<FamilyDoctorServiceBean> familyDoctorServiceBeans;
    private String position;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_health_measure_detail;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        familyDoctorServiceBeans = intentArgument.getParcelableArrayListExtra("data");
        position = intentArgument.getStringExtra("posi");
    }

    @Override
    public void initView() {
        showDeveloping();
        mTvTitle.setText(familyDoctorServiceBeans.get(Integer.parseInt(position)).getTitle());
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
