package com.gcml.module_health_measure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.gcml.module_health_measure.presenter.ChoodeDevicePresenter;
import com.gzq.lib_resource.constants.BluetoothParams;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/health/measure/choose/device")
public class ChooseDeviceActivity extends StateBaseActivity {
    private RecyclerView mRvMenu;
    private int type;
    private ChoodeDevicePresenter choodeDevicePresenter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_choose_device;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        type = intentArgument.getIntExtra(BluetoothParams.PARAM_MEASURE_TYPE, -1);
    }

    @Override
    public void initView() {
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        initType();
    }

    private void initType() {
    }

    @Override
    public IPresenter obtainPresenter() {
        choodeDevicePresenter = new ChoodeDevicePresenter(this);
        return choodeDevicePresenter;
    }

}
