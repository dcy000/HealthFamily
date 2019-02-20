package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.bean.HealthDataMenu;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/guardianship/resident/detail")
public class ResidentDetailActivity extends StateBaseActivity implements View.OnClickListener {
    private CircleImageView mCvHead;
    /**
     * 李晓大
     */
    private TextView mTvName;
    /**
     * 170cm
     */
    private TextView mTvHeight;
    private LinearLayout mLl1;
    private View mView1;
    /**
     * 56kg
     */
    private TextView mTvWeight;
    private LinearLayout mLl2;
    private View mView2;
    /**
     * A型
     */
    private TextView mTvBloodType;
    private LinearLayout mLl3;
    /**
     * 更多
     */
    private TextView mTvDataMore;
    private RecyclerView mRvHealthData;
    private RecyclerView mRvMenu;
    private ArrayList<String> dataMenu = new ArrayList<String>() {
        {
            add("家庭医生服务报告");
            add("健康管理报告");
            add("报警信息记录");
            add("健康检测记录");
            add("监护圈");
        }
    };
    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private List<HealthDataMenu> healthDataMenus = new ArrayList<>();
    private BaseQuickAdapter<HealthDataMenu, BaseViewHolder> adapter1;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_resident_detail;
    }


    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
//        healthDataMenus.add(new HealthDataMenu("跑步",8000,"步"));
//        healthDataMenus.add(new HealthDataMenu("跳绳",500,"次"));
//        healthDataMenus.add(new HealthDataMenu("高压",120,"mmHg"));
    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("居民详情");
        mCvHead = (CircleImageView) findViewById(R.id.cv_head);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvHeight = (TextView) findViewById(R.id.tv_height);
        mLl1 = (LinearLayout) findViewById(R.id.ll1);
        mView1 = (View) findViewById(R.id.view1);
        mTvWeight = (TextView) findViewById(R.id.tv_weight);
        mLl2 = (LinearLayout) findViewById(R.id.ll2);
        mView2 = (View) findViewById(R.id.view2);
        mTvBloodType = (TextView) findViewById(R.id.tv_blood_type);
        mLl3 = (LinearLayout) findViewById(R.id.ll3);
        mTvDataMore = (TextView) findViewById(R.id.tv_data_more);
        mTvDataMore.setOnClickListener(this);
        mRvHealthData = (RecyclerView) findViewById(R.id.rv_health_data);
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);

        Glide.with(Box.getApp())
                .load(Box.getString(R.string.head_img))
                .into(mCvHead);
        initMenu();
        initHealthDataRv();
    }

    private void initHealthDataRv() {
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvHealthData.setLayoutManager(layout);
        mRvHealthData.addItemDecoration(new LinearLayoutDividerItemDecoration(10, 0));
        mRvHealthData.setAdapter(adapter1 = new BaseQuickAdapter<HealthDataMenu, BaseViewHolder>(R.layout.item_health_data_menu_cards, healthDataMenus) {
            @Override
            protected void convert(BaseViewHolder helper, HealthDataMenu item) {
                helper.setText(R.id.tv_title, item.getProject());
                helper.setText(R.id.tv_project_value, item.getValue() + "");
                helper.setText(R.id.tv_unit, item.getUnit());
            }
        });

//
        Box.getRetrofit(GuardianshipApi.class)
                .getHealthDatas()
                .compose(RxUtils.<List<HealthDataMenu>>httpResponseTransformer())
                .as(RxUtils.<List<HealthDataMenu>>autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<HealthDataMenu>>() {
                    @Override
                    public void onNext(List<HealthDataMenu> healthDataMenus) {
                        ResidentDetailActivity.this.healthDataMenus.addAll(healthDataMenus);
                        adapter1.notifyDataSetChanged();
                    }
                });
    }

    private void initMenu() {
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_health_data_menu, dataMenu) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_title, item);
            }
        };
        mRvMenu.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
            @Override
            public void preData(Object... objects) {

            }

            @Override
            public void refreshData(Object... objects) {

            }

            @Override
            public void loadMoreData(Object... objects) {

            }
        };
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_data_more) {
        } else {
        }
    }
}
