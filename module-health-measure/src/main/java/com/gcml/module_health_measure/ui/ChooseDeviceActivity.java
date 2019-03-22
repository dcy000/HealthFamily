package com.gcml.module_health_measure.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_health_measure.DevicesHelper;
import com.gcml.module_health_measure.R;
import com.gcml.module_health_measure.api.HealthMeasureRouterApi;
import com.gcml.devices.base.BluetoothBean;
import com.gcml.module_health_measure.presenter.ChoodeDevicePresenter;
import com.gzq.lib_core.bean.BluetoothParams;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.List;

@Route(path = "/health/measure/choose/device")
public class ChooseDeviceActivity extends StateBaseActivity {
    private RecyclerView mRvMenu;
    private int type;
    private List<BluetoothBean> brandMenus;
    private BaseQuickAdapter<BluetoothBean, BaseViewHolder> adapter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_choose_device;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        type = intentArgument.getIntExtra(BluetoothParams.PARAM_MEASURE_TYPE, -1);
    }

    @Override
    public void initView() {
        showSuccess();
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        initType();
        setAdapter();
    }

    private void setAdapter() {
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        mRvMenu.setAdapter(adapter = new BaseQuickAdapter<BluetoothBean, BaseViewHolder>(R.layout.item_layout_device_brand, brandMenus) {
            @Override
            protected void convert(BaseViewHolder helper, BluetoothBean item) {
                ((ImageView) helper.getView(R.id.iv_image)).setImageResource(item.getImage());
                helper.setText(R.id.tv_title, item.isBand() ? item.getName() + "(已绑定)" : item.getName());
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BluetoothBean brandMenu = brandMenus.get(position);
                if (!brandMenu.isBand()) {
                    brandMenu.setBluetoothAddress(null);
                }
                Routerfit.register(HealthMeasureRouterApi.class).skipMeasureActivity(brandMenu);
            }
        });
    }

    private void initType() {
        switch (type) {
            case BluetoothParams.TYPE_TEMPERATURE:
                break;
            case BluetoothParams.TYPE_BLOODPRESSURE:
                getTitleTextView().setText("请选择血压计");
                brandMenus = DevicesHelper.bloodpressure();
                break;
            case BluetoothParams.TYPE_BLOODOXYGEN:
                break;
            case BluetoothParams.TYPE_WEIGHT:
                break;
            case BluetoothParams.TYPE_THREE:
                break;
            case BluetoothParams.TYPE_ECG:
                break;
        }
    }

    @Override
    public IPresenter obtainPresenter() {
        return new ChoodeDevicePresenter(this);
    }

}
