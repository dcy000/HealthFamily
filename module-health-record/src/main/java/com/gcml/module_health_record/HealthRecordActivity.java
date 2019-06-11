package com.gcml.module_health_record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.devices.base.IBluetoothPresenter;
import com.gcml.module_health_record.fragments.HealthRecordBUAFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodoxygenFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodpressureFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodsugarFragment;
import com.gcml.module_health_record.fragments.HealthRecordCholesterolFragment;
import com.gcml.module_health_record.fragments.HealthRecordECGFragment;
import com.gcml.module_health_record.fragments.HealthRecordTemperatureFragment;
import com.gcml.module_health_record.fragments.HealthRecordWeightFragment;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;

public class HealthRecordActivity extends StateBaseActivity implements View.OnClickListener {
    private int type;
    private RecycleBaseFragment baseFragment;
    private ImageView mHealthIvLeft;
    private TextView mHealthTvLeft;
    private LinearLayout mHealthLlLeft;
    private TextView mHealthTvTitle;
    private TextView mHealthTvRight;
    private ImageView mHealthIvRight;
    private LinearLayout mHealthLlRight;
    private RelativeLayout mHealthToolbar;
    private FrameLayout mFlContain;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activty_health_record;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        type = intentArgument.getIntExtra("type", 22);
    }

    @Override
    public void initView() {
        showSuccess();
        getToolbar().setVisibility(View.GONE);
        mHealthIvLeft = (ImageView) findViewById(R.id.health_iv_left);
        mHealthTvLeft = (TextView) findViewById(R.id.health_tv_left);
        mHealthLlLeft = (LinearLayout) findViewById(R.id.health_ll_left);
        mHealthLlLeft.setOnClickListener(this);
        mHealthTvTitle = (TextView) findViewById(R.id.health_tv_title);
        mHealthTvRight = (TextView) findViewById(R.id.health_tv_right);
        mHealthIvRight = (ImageView) findViewById(R.id.health_iv_right);
        mHealthLlRight = (LinearLayout) findViewById(R.id.health_ll_right);
        mHealthLlRight.setOnClickListener(this);
        mHealthToolbar = (RelativeLayout) findViewById(R.id.health_toolbar);
        mFlContain = (FrameLayout) findViewById(R.id.fl_contain);

        switch (type) {
            case IBluetoothPresenter.MEASURE_BLOOD_PRESSURE:
                mHealthTvTitle.setText("血压记录");
                baseFragment = new HealthRecordBloodpressureFragment();
                break;
            case IBluetoothPresenter.MEASURE_BLOOD_SUGAR:
                mHealthTvTitle.setText("血糖记录");
                baseFragment = new HealthRecordBloodsugarFragment();
                break;
            case IBluetoothPresenter.MEASURE_TEMPERATURE:
                mHealthTvTitle.setText("体温记录");
                baseFragment = new HealthRecordTemperatureFragment();
                break;
            case IBluetoothPresenter.MEASURE_WEIGHT:
                mHealthTvTitle.setText("体重记录");
                baseFragment = new HealthRecordWeightFragment();
                break;
            case IBluetoothPresenter.MEASURE_ECG:
                mHealthTvTitle.setText("心电记录");
                baseFragment = new HealthRecordECGFragment();
                break;
            case IBluetoothPresenter.MEASURE_BLOOD_OXYGEN:
                mHealthTvTitle.setText("血氧记录");
                baseFragment = new HealthRecordBloodoxygenFragment();
                break;
            case IBluetoothPresenter.MEASURE_CHOLESTEROL:
                mHealthTvTitle.setText("胆固醇记录");
                baseFragment = new HealthRecordCholesterolFragment();
                break;
            case IBluetoothPresenter.MEASURE_UAC:
                mHealthTvTitle.setText("血尿酸记录");
                baseFragment = new HealthRecordBUAFragment();
                break;
        }
        if (baseFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_contain, baseFragment).commitAllowingStateLoss();
        }

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.health_ll_left) {
            finish();
        } else if (i == R.id.health_ll_right) {

        }
    }
}
