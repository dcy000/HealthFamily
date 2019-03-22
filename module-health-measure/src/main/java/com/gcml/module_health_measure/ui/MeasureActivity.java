package com.gcml.module_health_measure.ui;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.gcml.devices.base.BaseBluetooth;
import com.gcml.devices.base.BluetoothBean;
import com.gcml.devices.base.IBluetoothView;
import com.gcml.devices.bloodpressure.BloodPressurePresenter;
import com.gcml.module_health_measure.R;
import com.gcml.module_health_measure.api.BluetoothUpdateData;
import com.gzq.lib_core.bean.BluetoothParams;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/health/measure/measure/activity")
public class MeasureActivity extends StateBaseActivity implements IBluetoothView {

    private BluetoothBean data;
    private int deviceType;
    private BaseBluetooth basePresenter;
    private BaseFragment baseFragment;
    private BluetoothSearchingFragment searchingFragment;
    private BluetoothUpdateData updateData;

    public void setUpdateDataListener(BluetoothUpdateData updateData) {
        this.updateData = updateData;
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_measure;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        data = intentArgument.getParcelableExtra("data");
        if (data == null) throw new NullPointerException();
        deviceType = data.getDeviceType();
    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("蓝牙搜索");
        getRightView().setVisibility(View.VISIBLE);
        getRightTextView().setText("历史记录");
        getRightTextView().setTextColor(Color.parseColor("#3F86FC"));
        if (findFragment(BluetoothSearchingFragment.class) == null) {
            searchingFragment = new BluetoothSearchingFragment();
            loadRootFragment(R.id.fl_containter, searchingFragment);
        }
    }

    @Override
    public IPresenter obtainPresenter() {
        switch (deviceType) {
            case BluetoothParams.TYPE_BLOODPRESSURE:
                basePresenter = new BloodPressurePresenter(this, data);
                baseFragment = new BloodpressureFragment();
                break;
        }
        return null;
    }

    @Override
    public void updateData(String... datas) {
        if (updateData != null) {
            updateData.data(datas);
        }
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
        String connected = getResources().getString(R.string.bluetooth_device_connected);
        String disconnected = getResources().getString(R.string.bluetooth_device_disconnected);
        if (connected.equals(state)) {
            loadRootFragment(R.id.fl_containter, baseFragment);
            getTitleTextView().setText("血压检测");
        } else if (disconnected.equals(state)) {
            loadRootFragment(R.id.fl_containter, searchingFragment);
            getTitleTextView().setText("蓝牙搜索");
        }
    }

    @Override
    public void discoveryNewDevice(BluetoothDevice device) {

    }

    @Override
    protected void clickToolbarRight() {
        ToastUtils.showShort("历史记录");
    }
}
