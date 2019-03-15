package com.gcml.devices.bloodoxygen;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.devices.R;
import com.gcml.devices.base.BaseBluetooth;
import com.gcml.devices.base.BluetoothBaseFragment;
import com.gcml.devices.base.IBluetoothPresenter;
import com.gcml.devices.utils.T;

public class BloodOxygenFragment extends BluetoothBaseFragment implements View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_bloodoxygen;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvResult = view.findViewById(R.id.tv_result);
        mTvResult.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    protected BaseBluetooth obtainPresenter() {
        return new BloodOxygenPresenter(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IBluetoothPresenter.MEASURE_BLOOD_OXYGEN);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IBluetoothPresenter.MEASURE_BLOOD_OXYGEN);
            }
            clickHealthHistory(v);
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 3) {
            mTvResult.setText("0");
            isMeasureFinishedOfThisTime = false;
        } else if (datas.length == 2) {
            mTvResult.setText(datas[0]);
            if (!isMeasureFinishedOfThisTime && Float.parseFloat(datas[0]) != 0) {
                isMeasureFinishedOfThisTime = true;
                onMeasureFinished(datas[0], datas[1]);
            }
        }
    }

    @Override
    public void updateState(String state) {
        T.showShort(state);
        if (dealVoiceAndJump != null) {
            dealVoiceAndJump.updateVoice(state);
        }
    }
}
