package com.gcml.module_health_measure.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.module_health_measure.R;
import com.gcml.module_health_measure.api.BluetoothUpdateData;
import com.gzq.lib_resource.mvp.base.BaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.data.TimeUtils;

public class BloodpressureFragment extends BaseFragment implements BluetoothUpdateData {
    /**
     * 血压
     */
    private TextView mTvXueya;
    /**
     * 2019.01.15 14:23
     */
    private TextView mTvBloodpressureTime;
    /**
     * 23
     */
    private TextView mTvBloodpressureResult;
    /**
     * 脉搏
     */
    private TextView mTvMaibo;
    /**
     * 2019.01.15 14:23
     */
    private TextView mTvPulseTime;
    /**
     * 23
     */
    private TextView mTvPulseResult;
    private boolean isMeasureFinishedOfThisTime;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.fragment_bloodpressure;
    }

    @Override
    public void initParams(Bundle bundle) {
        ((MeasureActivity) getActivity()).setUpdateDataListener(this);
    }

    @Override
    public void initView(View view) {

        mTvXueya = (TextView) view.findViewById(R.id.tv_xueya);
        mTvBloodpressureTime = (TextView) view.findViewById(R.id.tv_bloodpressure_time);
        mTvBloodpressureResult = (TextView) view.findViewById(R.id.tv_bloodpressure_result);
        mTvMaibo = (TextView) view.findViewById(R.id.tv_maibo);
        mTvPulseTime = (TextView) view.findViewById(R.id.tv_pulse_time);
        mTvPulseResult = (TextView) view.findViewById(R.id.tv_pulse_result);
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

    @Override
    public void data(String... datas) {
        if (datas.length == 1) {
            mTvBloodpressureResult.setText(datas[0]);
            isMeasureFinishedOfThisTime = false;
        } else if (datas.length == 3) {
            mTvBloodpressureResult.setText(datas[0] + "/" + datas[1]);
            mTvPulseResult.setText(datas[2]);
            String curTimeString = TimeUtils.getCurTimeString();
            mTvBloodpressureTime.setText(curTimeString);
            mTvPulseTime.setText(curTimeString);
            if (!isMeasureFinishedOfThisTime && Float.parseFloat(datas[0]) != 0) {
                isMeasureFinishedOfThisTime = true;
            }
        } else {
        }
    }

}
