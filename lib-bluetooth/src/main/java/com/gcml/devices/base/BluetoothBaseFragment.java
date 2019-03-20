package com.gcml.devices.base;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.devices.dialog.ChooseBluetoothDevice;

public abstract class BluetoothBaseFragment extends Fragment implements IBluetoothView, ChooseBluetoothDevice {
    protected View view = null;
    protected boolean isMeasureFinishedOfThisTime = false;
    protected Context mContext;
    protected Activity mActivity;
    protected BaseBluetooth basePresenter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //全局控制上下文 子类统一使用该引用，避免异步任务结束后getActivity引起的空指针异常，但同时这种做法有内存泄漏的风险
        this.mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(initLayout(), container, false);
            initView(view, getArguments());
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        basePresenter = obtainPresenter();
    }

    @Override
    public void discoveryNewDevice(BluetoothDevice device) {

    }

    @Override
    public void choosed(BluetoothDevice device) {
        if (basePresenter != null) {
            basePresenter.startConnect(device);
        }
    }

    @Override
    public void autoConnect() {
        if (basePresenter != null) {
            basePresenter.startDiscovery(null);
        }
    }

    @Override
    public void cancelSearch() {
        if (basePresenter != null) {
            basePresenter.stopDiscovery();
        }
    }

    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(String state) {

    }

    protected BaseBluetooth obtainPresenter() {
        return null;
    }

    protected abstract int initLayout();

    protected abstract void initView(View view, Bundle bundle);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
            view = null;
        }
        isMeasureFinishedOfThisTime = false;
        dealVoiceAndJump = null;
        fragmentChanged = null;
    }

    protected DealVoiceAndJump dealVoiceAndJump;

    public void setOnDealVoiceAndJumpListener(DealVoiceAndJump dealVoiceAndJump) {
        this.dealVoiceAndJump = dealVoiceAndJump;
    }

    protected FragmentChanged fragmentChanged;

    public void setOnFragmentChangedListener(FragmentChanged fragmentChanged) {
        this.fragmentChanged = fragmentChanged;
    }

    protected ThisFragmentDatas fragmentDatas;

    public void setOnThisFragmentDataChangedListener(ThisFragmentDatas fragmentDatas) {
        this.fragmentDatas = fragmentDatas;
    }

    protected void clickHealthHistory(View view) {
    }

    protected void clickVideoDemo(View view) {
    }

    protected void onMeasureFinished(String... results) {
    }
}
