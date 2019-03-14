package com.gcml.devices.ecg;

import android.arch.lifecycle.MutableLiveData;
import android.support.v4.app.FragmentActivity;

import com.gcml.devices.base.BaseBluetooth;
import com.gcml.devices.base.DeviceBrand;
import com.gcml.devices.base.IBluetoothView;
import com.gcml.devices.utils.BluetoothConstants;
import com.gzq.lib_core.utils.SPUtil;

import java.util.HashMap;

public class ECGPresenter extends BaseBluetooth {
    public MutableLiveData<String> ecgBrand = new MutableLiveData<>();
    public ECGPresenter(IBluetoothView owner) {
        super(owner);
        startDiscovery(targetAddress);
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        if (name.startsWith("A12-B")){
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
            new BoShengECGPresenter((FragmentActivity) getActivity(), baseView, name, address);
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
    protected void saveSP(String sp) {
        SPUtil.put(BluetoothConstants.SP.SP_SAVE_ECG, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_ECG, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.ECG;
    }
}
