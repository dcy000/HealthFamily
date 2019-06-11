package com.gcml.module_health_record.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.gcml.module_health_record.R;
import com.gcml.module_health_record.RecycleBaseFragment;
import com.gcml.module_health_record.bean.CholesterolHistory;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.network.HealthRecordRepository;
import com.gcml.module_health_record.others.XindianAdapter;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.utils.data.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HealthRecordECGFragment extends RecycleBaseFragment {

    private RecyclerView mXindiantu;

    @Override
    protected int initLayout() {
        return R.layout.health_record_fragment_health_record_ecg;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mXindiantu = view.findViewById(R.id.xindiantu);
        getData();
    }

    private int selectEndYear;
    private int selectEndMonth;
    private int selectEndDay;
    private int selectEndHour;
    private int selectEndMinnute;
    private int selectEndSecond;
    private String endMillisecond;
    private int selectStartYear;
    private int selectStartMonth;
    private int selectStartDay;
    private String startMillisecond;

    private void getData() {
        Calendar calendar = Calendar.getInstance();
        selectEndYear = calendar.get(Calendar.YEAR);
        selectEndMonth = calendar.get(Calendar.MONTH) + 1;
        selectEndDay = calendar.get(Calendar.DATE);
        selectEndHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectEndMinnute = calendar.get(Calendar.MINUTE);
        selectEndSecond = calendar.get(Calendar.SECOND);
        endMillisecond = TimeUtils.string2Milliseconds(selectEndYear + "-" + selectEndMonth + "-" +
                        selectEndDay + "-" + selectEndHour + "-" + selectEndMinnute + "-" + selectEndSecond,
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")) + "";

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        Date weekAgoDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(weekAgoDate);
        String[] date = result.split("-");
        selectStartYear = Integer.parseInt(date[0]);
        selectStartMonth = Integer.parseInt(date[1]);
        selectStartDay = Integer.parseInt(date[2]);
        startMillisecond = TimeUtils.string2Milliseconds(selectStartYear + "-" + selectStartMonth + "-" +
                selectStartDay, new SimpleDateFormat("yyyy-MM-dd")) + "";

        new HealthRecordRepository()
                .getECGHistory(startMillisecond, endMillisecond, "9")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<ECGHistory>>() {
                    @Override
                    public void onNext(List<ECGHistory> ecgHistories) {
                        refreshData(ecgHistories,"9");
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshErrorData("暂无该项数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void refreshData(List<ECGHistory> response, String temp) {
        List<ECGHistory> ecgs=new ArrayList<>();
        for (ECGHistory data : response) {
            if (!TextUtils.isEmpty(data.result) && !data.result.contains("重新测试")) {
               ecgs.add(data);
            }
        }
        mXindiantu.setLayoutManager(new LinearLayoutManager(getContext()));
        if (isAdded()) {
            mXindiantu.setAdapter(new XindianAdapter(R.layout.health_record_item_message, ecgs,
                    getResources().getStringArray(R.array.ecg_measureres)));
        }
    }

    public void refreshErrorData(String message) {
        ToastUtils.showShort(message);
//        mTvEmptyDataTips.setText("啊哦!你还没有测量数据");
//        view.findViewById(R.id.view_empty_data).setVisibility(View.VISIBLE);
    }
}
