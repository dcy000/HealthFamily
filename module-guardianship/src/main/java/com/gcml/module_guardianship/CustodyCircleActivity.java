package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gcml.module_guardianship.bean.FamilyBean;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.presenter.ResidentLocationPresenter;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.CallPhoneUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.jessyan.autosize.utils.AutoSizeUtils;

@Route(path = "/custody/circle/me")
public class CustodyCircleActivity extends StateBaseActivity {
    private RecyclerView mRvContent;
    private List<FamilyBean> familyBeans = new ArrayList<>();
    private ResidentLocationPresenter residentLocationPresenter;
    private GuardianshipBean guardianshipBean;
    private BaseQuickAdapter<FamilyBean, BaseViewHolder> adapter;
    private LinearLayout mLlAddCustody;


    @Override
    protected void onResume() {
        super.onResume();
        residentLocationPresenter.preData(guardianshipBean.getBid());
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_custody_circle;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        guardianshipBean = intentArgument.getParcelableExtra("data");
    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("监护圈");
        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        mLlAddCustody = findViewById(R.id.ll_add_custody);
        mLlAddCustody.setOnClickListener(this);
        initRv();

    }

    private void initRv() {
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.setAdapter(adapter = new BaseQuickAdapter<FamilyBean, BaseViewHolder>(R.layout.item_layout_custody_circle, familyBeans) {
            @Override
            protected void convert(BaseViewHolder helper, FamilyBean item) {
                Glide.with(Box.getApp())
                        .load(item.getGuardianPhoto())
                        .into(((CircleImageView) helper.getView(R.id.civ_head)));
                helper.setText(R.id.tv_name, item.getGuardianName());
                helper.setText(R.id.tv_label, item.getGuardianType());

                helper.getView(R.id.iv_call).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPhoneTipsDialog(item.getGuardianName(), item.getMobileNum());
                    }
                });
            }
        });
    }

    private void showPhoneTipsDialog(String name, String phone) {
        FDialog.build()
                .setSupportFM(getSupportFragmentManager())
                .setLayoutId(R.layout.dialog_layout_phone_tips)
                .setWidth(AutoSizeUtils.pt2px(this, 540))
                .setOutCancel(false)
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        holder.setText(R.id.tv_title, name + "的电话号码");
                        holder.setText(R.id.tv_message, phone);
                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallPhoneUtils.instance().callPhone(CustodyCircleActivity.this, phone);
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show();
    }

    @Override
    public IPresenter obtainPresenter() {
        residentLocationPresenter = new ResidentLocationPresenter(this);
        return residentLocationPresenter;
    }

    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
        List<FamilyBean> object = (List<FamilyBean>) objects[0];
        familyBeans.clear();
        familyBeans.addAll(object);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.ll_add_custody) {
            Routerfit.register(GuardianshipRouterApi.class).skipAddCustodyActivity(guardianshipBean.getBid() + "");
        }
    }
}
