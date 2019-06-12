package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.bean.HandDataBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

@Route(path = "/hand/data/list/activity")
public class HandDataListActivity extends StateBaseActivity {

    private String imei;
    private RecyclerView mRvHand;
    private List<HandDataBean> dataBeans = new ArrayList<>();
    private BaseQuickAdapter<HandDataBean, BaseViewHolder> adapter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_hand_data_list;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        imei = intentArgument.getStringExtra("imei");
    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("手环测量数据");
        mRvHand = findViewById(R.id.rv_hand);
        setAdapter();
        getData();
    }

    private void setAdapter() {
        mRvHand.setLayoutManager(new LinearLayoutManager(this));
        mRvHand.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvHand.setAdapter(adapter = new BaseQuickAdapter<HandDataBean, BaseViewHolder>(R.layout.item_layout_hand, dataBeans) {
            @Override
            protected void convert(BaseViewHolder helper, HandDataBean item) {
                helper.setText(R.id.tv_time, item.getCreatedTime());
                helper.setText(R.id.tv_project_value1, item.getSdp() + "");
                helper.setText(R.id.tv_project_value2, item.getDbp() + "");
                helper.setText(R.id.tv_project_value3, item.getHeartRate() + "");
            }
        });
    }

    private void getData() {
        Box.getRetrofit(GuardianshipApi.class)
                .getHandDatas(imei, "1", "50")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<List<HandDataBean>>() {
                    @Override
                    public void accept(List<HandDataBean> objects) throws Exception {
                        dataBeans.clear();
                        dataBeans.addAll(objects);
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
