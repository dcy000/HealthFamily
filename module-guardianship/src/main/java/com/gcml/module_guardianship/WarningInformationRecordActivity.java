package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzq.lib_resource.bean.WarningInfoRecordBean;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;

@Route(path = "/warning/information/record")
public class WarningInformationRecordActivity extends StateBaseActivity {
    private LinearLayout mLlTitle;
    private RecyclerView mRvContent;
    private ArrayList<WarningInfoRecordBean> warningInfoRecordBeans = new ArrayList<WarningInfoRecordBean>() {
        {
            add(new WarningInfoRecordBean("2019.01.24", "一键SOS", "王晓"));
            add(new WarningInfoRecordBean("2019.01.06", "自动报警", "王笑"));
            add(new WarningInfoRecordBean("2019.01.24", "一键SOS", "王晓"));
            add(new WarningInfoRecordBean("2019.01.24", "自动报警", "李小大"));
            add(new WarningInfoRecordBean("2019.01.24", "自动报警", "王晓"));
            add(new WarningInfoRecordBean("2019.01.24", "一键SOS", "李小大"));
        }
    };

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_warning_information_record;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("预警信息记录");
        mLlTitle = (LinearLayout) findViewById(R.id.ll_title);
        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        initRv();
    }

    private void initRv() {
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.setAdapter(new BaseQuickAdapter<WarningInfoRecordBean, BaseViewHolder>(R.layout.item_layout_warning_info_record, warningInfoRecordBeans) {
            @Override
            protected void convert(BaseViewHolder helper, WarningInfoRecordBean item) {
                helper.setText(R.id.tv_time, item.getWarningTime());
                helper.setText(R.id.tv_type, item.getWarningType());
                helper.setText(R.id.tv_person, item.getWarningDealPerson());
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
