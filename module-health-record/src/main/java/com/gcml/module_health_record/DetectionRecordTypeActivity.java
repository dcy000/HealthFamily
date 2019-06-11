package com.gcml.module_health_record;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.devices.base.IBluetoothPresenter;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;

@Route(path = "/health/record/detection")
public class DetectionRecordTypeActivity extends StateBaseActivity {
    private RecyclerView mRvMenu;
    private ArrayList<String> dataMenu = new ArrayList<String>() {
        {
            add("血压记录");
            add("血糖记录");
            add("体温记录");
            add("体重记录");
            add("心电记录");
            add("血氧记录");
            add("胆固醇记录");
            add("血尿酸记录");
        }
    };
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_detection_record_type;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return super.isBackgroundF8F8F8();
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("测量记录");
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        setAdapter();

    }

    private void setAdapter() {
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_detection_data_menu, dataMenu) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_title, item);
            }
        };
        mRvMenu.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        //血压
                        startHealthRecord(IBluetoothPresenter.MEASURE_BLOOD_PRESSURE);
                        break;
                    case 1:
                        //血糖
                        startHealthRecord(IBluetoothPresenter.MEASURE_BLOOD_SUGAR);
                        break;
                    case 2:
                        //体温
                        startHealthRecord(IBluetoothPresenter.MEASURE_TEMPERATURE);
                        break;
                    case 3:
                        //体重
                        startHealthRecord(IBluetoothPresenter.MEASURE_WEIGHT);
                        break;
                    case 4:
                        //心电
                        startHealthRecord(IBluetoothPresenter.MEASURE_ECG);
                        break;
                    case 5:
                        //血氧
                        startHealthRecord(IBluetoothPresenter.MEASURE_BLOOD_OXYGEN);
                        break;
                    case 6:
                        //胆固醇
                        startHealthRecord(IBluetoothPresenter.MEASURE_CHOLESTEROL);
                        break;
                    case 7:
                        //血尿酸
                        startHealthRecord(IBluetoothPresenter.MEASURE_UAC);
                        break;
                }
            }
        });
    }

    private void startHealthRecord(int type) {
        startActivity(new Intent(this, HealthRecordActivity.class).putExtra("type", type));
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

}
