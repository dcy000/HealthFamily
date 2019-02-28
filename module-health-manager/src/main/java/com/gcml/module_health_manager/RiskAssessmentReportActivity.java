package com.gcml.module_health_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.bean.HealthManagerReportBean;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/healthmanager/risk/assessment/report")
public class RiskAssessmentReportActivity extends StateBaseActivity {
    private RecyclerView mRv;
    private ArrayList<HealthManagerReportBean> healthManagerReportBeans = new ArrayList<HealthManagerReportBean>() {{
        add(new HealthManagerReportBean("测试一", "2019.1.1", "2019.2.1"));
        add(new HealthManagerReportBean("测试二", "2019.1.1", "2019.2.1"));
        add(new HealthManagerReportBean("测试三", "2019.1.1", "2019.2.1"));
        add(new HealthManagerReportBean("测试四", "2019.1.1", "2019.2.1"));
        add(new HealthManagerReportBean("测试五", "2019.1.1", "2019.2.1"));
        add(new HealthManagerReportBean("测试六", "2019.1.1", "2019.2.1"));
        add(new HealthManagerReportBean("测试七", "2019.1.1", "2019.2.1"));
        add(new HealthManagerReportBean("测试八", "2019.1.1", "2019.2.1"));
        add(new HealthManagerReportBean("测试九", "2019.1.1", "2019.2.1"));
    }};

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_health_manager_programme_report;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("风险评估报告");
        mRv = (RecyclerView) findViewById(R.id.rv);
        initRv();
    }

    private void initRv() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new BaseQuickAdapter<HealthManagerReportBean, BaseViewHolder>(R.layout.item_layout_health_manager_report, healthManagerReportBeans) {
            @Override
            protected void convert(BaseViewHolder helper, HealthManagerReportBean item) {
                Glide.with(Box.getApp())
                        .load(Box.getString(R.string.head_img))
                        .into(((CircleImageView) helper.getView(R.id.civ_head)));
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_commit_time, "提交时间：" + item.getCommitTime());
                helper.setText(R.id.tv_audit_time, "审核时间：" + item.getAuditTime());
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
