package com.gcml.module_health_measure.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

import com.gcml.module_health_measure.R;
import com.gzq.lib_resource.mvp.base.BaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.widget.WaveView;

public class BluetoothSearchingFragment extends BaseFragment {

    private WaveView mWaveView;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.fragment_bluetooth_searching;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        mWaveView = (WaveView) view.findViewById(R.id.wave_view);

        mWaveView.setDuration(5000);
        mWaveView.setSpeed(800);
        mWaveView.setStyle(Paint.Style.FILL);
        mWaveView.setColor(Color.parseColor("#3F86FC"));
        mWaveView.setInterpolator(new LinearOutSlowInInterpolator());
    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveView.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWaveView.stop();
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void loadDataSuccess(Object... objects) {

    }

    @Override
    public void loadDataError(Object... objects) {

    }

    @Override
    public void loadDataEmpty() {

    }

    @Override
    public void onNetworkError() {

    }
}
