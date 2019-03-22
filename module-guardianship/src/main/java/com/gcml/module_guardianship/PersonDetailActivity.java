package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.bean.PersonDetailMenuBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.CallPhoneUtils;
import com.jaeger.library.StatusBarUtil;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.jessyan.autosize.utils.AutoSizeUtils;

@Route(path = "/person/detail/")
public class PersonDetailActivity extends StateBaseActivity {
    private ImageView mIvBack;
    private RecyclerView mRvMenu;
    private CircleImageView mCivHead;
    private TextView mTvName;
    private ArrayList<PersonDetailMenuBean> menuBeans = new ArrayList<>();
    private GuardianshipBean guardianshipBean;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_person_detail;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        guardianshipBean = intentArgument.getParcelableExtra("data");
        menuBeans.add(new PersonDetailMenuBean("手机号", guardianshipBean.getTel(), R.drawable.guardianship_ic_call));
        menuBeans.add(new PersonDetailMenuBean("手环IMEI码", guardianshipBean.getWatchCode(), 0));
        menuBeans.add(new PersonDetailMenuBean("现住址", TextUtils.isEmpty(guardianshipBean.getDz())?"未填写":guardianshipBean.getDz(), 0));
        menuBeans.add(new PersonDetailMenuBean("详细地址", "未填写", 0));
    }

    @Override
    public void initView() {
        showSuccess();
        getToolbar().setVisibility(View.GONE);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        mCivHead = (CircleImageView) findViewById(R.id.civ_head);
        mTvName = (TextView) findViewById(R.id.tv_name);
        initMenu();
    }

    private void initMenu() {
        Glide.with(Box.getApp())
                .load(guardianshipBean.getUserPhoto())
                .into(mCivHead);
        mTvName.setText(guardianshipBean.getBname());
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        mRvMenu.setAdapter(new BaseQuickAdapter<PersonDetailMenuBean, BaseViewHolder>(R.layout.item_layout_person_detail_menu, menuBeans) {
            @Override
            protected void convert(BaseViewHolder helper, PersonDetailMenuBean item) {
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_content, item.getContent());
                if (item.getImg() != 0) {
                    ((ImageView) helper.getView(R.id.iv_call)).setImageResource(item.getImg());
                    helper.getView(R.id.iv_call).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPhoneTipsDialog(guardianshipBean);
                        }
                    });
                }
            }
        });
    }

    private void showPhoneTipsDialog(GuardianshipBean item) {
        FDialog.build()
                .setSupportFM(getSupportFragmentManager())
                .setLayoutId(R.layout.dialog_layout_phone_tips)
                .setWidth(AutoSizeUtils.pt2px(this, 540))
                .setOutCancel(false)
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        holder.setText(R.id.tv_title, item.getBname() + "的电话号码");
                        holder.setText(R.id.tv_message, item.getTel());
                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallPhoneUtils.instance().callPhone(PersonDetailActivity.this, item.getTel());
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
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        }
    }
}
