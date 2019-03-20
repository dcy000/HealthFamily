package com.gcml.devices.ecg;

import android.arch.lifecycle.MutableLiveData;
import android.support.v4.app.FragmentActivity;

import com.gcml.devices.base.BaseBluetooth;
import com.gcml.devices.base.BluetoothType;
import com.gcml.devices.base.BluetoothBean;
import com.gcml.devices.base.IBluetoothView;

import java.util.HashMap;

public class ECGPresenter extends BaseBluetooth {
    public MutableLiveData<String> ecgBrand = new MutableLiveData<>();
    private BoShengUserBean userBean;

    public ECGPresenter(IBluetoothView owner, BluetoothBean brandMenu) {
        super(owner, brandMenu);
        //开始搜索
        start(BluetoothType.BLUETOOTH_TYPE_BLE, brandMenu.getBluetoothAddress(), brandMenu.getBluetoothName());
    }

    public ECGPresenter(IBluetoothView owner, BoShengUserBean userBean, BluetoothBean brandMenu) {
        super(owner, brandMenu);
        this.userBean = userBean;
        //开始搜索
        start(BluetoothType.BLUETOOTH_TYPE_BLE, brandMenu.getBluetoothAddress(), brandMenu.getBluetoothName());
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        if (name.startsWith("A12-B")) {
            ecgBrand.postValue("A12-B");
            new ChaosiECGPresenter(getActivity(), baseView, name, address);
            return;
        }
        baseView.updateState("未兼容该设备:" + name + ":::" + address);
    }

    @Override
    protected boolean isSelfConnect(String name, String address) {
        if (name.startsWith("WeCardio")) {
            ecgBrand.postValue("WeCardio");
            if (userBean == null) {
                throw new IllegalArgumentException("BoShengBean must not null");
            }
            new BoShengECGPresenter((FragmentActivity) getActivity(), baseView, name, address, userBean);
            return true;
        }
        return super.isSelfConnect(name, address);
    }

    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected(String address) {

    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return null;
    }
}
