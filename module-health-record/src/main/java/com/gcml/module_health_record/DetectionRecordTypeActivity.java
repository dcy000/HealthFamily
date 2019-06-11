package com.gcml.module_health_record;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.devices.base.IBluetoothPresenter;
import com.gcml.module_health_record.bean.ChooseDetectionTypeBean;
import com.gcml.module_health_record.bean.LatestDetecBean;
import com.gcml.module_health_record.network.HealthRecordServer;
import com.gcml.module_health_record.utils.Time2Utils;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/health/record/detection")
public class DetectionRecordTypeActivity extends StateBaseActivity {
    private RecyclerView mRvMenu;
    private ArrayList<ChooseDetectionTypeBean> types = new ArrayList<>();

    {
        types.add(new ChooseDetectionTypeBean(0, "血压", "", "", "mmHg"));
        types.add(new ChooseDetectionTypeBean(0, "血糖", "", "", "mmol/L"));
        types.add(new ChooseDetectionTypeBean(0, "体温", "", "", "℃"));
        types.add(new ChooseDetectionTypeBean(0, "体重", "", "", "Kg"));
        types.add(new ChooseDetectionTypeBean(0, "心电", "", "", ""));
        types.add(new ChooseDetectionTypeBean(0, "血氧", "", "", "%"));
        types.add(new ChooseDetectionTypeBean(0, "胆固醇", "", "", "mmol/L"));
        types.add(new ChooseDetectionTypeBean(0, "血尿酸", "", "", "mmol/L"));
    }

    private BaseQuickAdapter<ChooseDetectionTypeBean, BaseViewHolder> adapter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_detection_record_type;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
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
        getData();
    }

    private void getData() {
        Box.getRetrofit(HealthRecordServer.class)
                .getLatestDetectionData(KVUtils.get(KVConstants.KEY_PATIENTID, 0) + "")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<LatestDetecBean>>() {
                    @Override
                    public void onNext(List<LatestDetecBean> latestDetecBeans) {
                        for (LatestDetecBean latest : latestDetecBeans) {
                            //检测数据类型 -1低血压 0高血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸
                            String type = latest.getType();
                            switch (type) {
                                case "-1":
                                    types.get(0).setResult("/" + String.format("%.0f", latest.getValue()));
                                    types.get(0).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    break;
                                case "0":
                                    types.get(0).setResult(new StringBuffer(types.get(0).getResult()).insert(0, String.format("%.0f", latest.getValue())).toString());
                                    types.get(0).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "1":
                                    types.get(1).setResult(latest.getValue() + "");
                                    types.get(1).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(1).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "2":
                                    types.get(4).setResult(TextUtils.equals(latest.getStatus(), "0") ? "正常" : "异常");
                                    types.get(4).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(4).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "3":
                                    types.get(3).setResult(latest.getValue() + "");
                                    types.get(3).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(3).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "4":
                                    types.get(2).setResult(latest.getValue() + "");
                                    types.get(2).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(2).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "6":
                                    types.get(5).setResult(String.format("%.0f", latest.getValue()));
                                    types.get(5).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(5).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "7":
                                    types.get(6).setResult(latest.getValue() + "");
                                    types.get(6).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(6).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                                case "8":
                                    types.get(7).setResult(latest.getValue() + "");
                                    types.get(7).setDate(Time2Utils.getFriendlyTimeSpanByNow(latest.getDate()));
                                    types.get(7).setNormal(TextUtils.equals(latest.getStatus(), "0"));
                                    break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void setAdapter() {
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        mRvMenu.addItemDecoration(new LinearLayoutDividerItemDecoration(0,24));
        adapter = new BaseQuickAdapter<ChooseDetectionTypeBean, BaseViewHolder>(R.layout.item_detection_data_menu, types) {
            @Override
            protected void convert(BaseViewHolder helper, ChooseDetectionTypeBean item) {
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_unit, item.getUnit());
                if (!TextUtils.isEmpty(item.getDate()))
                    helper.setText(R.id.tv_time, item.getDate());

                if (!TextUtils.isEmpty(item.getResult())) {
                    helper.setText(R.id.tv_result, item.getResult());
                }
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
