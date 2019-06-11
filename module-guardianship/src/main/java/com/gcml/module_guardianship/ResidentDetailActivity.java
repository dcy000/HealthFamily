package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gcml.module_guardianship.bean.FamilyBean;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.bean.HandRingHealthDataBena;
import com.gcml.module_guardianship.bean.HealthDataMenu;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.gcml.module_guardianship.presenter.ResidentDetailPresenter;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.CallPhoneUtils;
import com.gzq.lib_resource.utils.GPSUtil;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.RouteRegister;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.jessyan.autosize.utils.AutoSizeUtils;

@Route(path = "/guardianship/resident/detail")
public class ResidentDetailActivity extends StateBaseActivity<ResidentDetailPresenter> implements View.OnClickListener {
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
//            add("家庭医生服务报告");
//            add("健康管理报告");
//            add("预警信息记录");
            add("监护圈");
            add("健康检测记录");
            add("远程监控");
        }
    };
    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private List<HealthDataMenu> healthDataMenus = new ArrayList<>();
    private BaseQuickAdapter<HealthDataMenu, BaseViewHolder> adapter1;
    private GuardianshipBean guardianshipBean;
    private LinearLayout mLlLocation;
    private TextView mTvAddress;

    @Override
    protected void onStart() {
        super.onStart();
        getP().preData(guardianshipBean.getBid());
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_resident_detail;
    }


    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        guardianshipBean = intentArgument.getParcelableExtra("data");
    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("居民详情");
        getRightView().setVisibility(View.VISIBLE);
        getRightTextView().setVisibility(View.GONE);
        getRightImageView().setImageResource(R.drawable.icon_mobile_phone_blue);
        mCvHead = (CircleImageView) findViewById(R.id.cv_head);
        mCvHead.setOnClickListener(this);
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
        mLlLocation = (LinearLayout) findViewById(R.id.ll_location);
        mLlLocation.setOnClickListener(this);
        mTvAddress = findViewById(R.id.tv_address);
        fillData();
        initMenu();
        initHealthDataRv();
    }

    private void fillData() {
        Glide.with(Box.getApp())
                .load(guardianshipBean.getUserPhoto())
                .into(mCvHead);
        mTvName.setText(guardianshipBean.getBname());
        mTvHeight.setText(guardianshipBean.getHeight() == 0 ? "未填写" : guardianshipBean.getHeight() + "cm");
        mTvWeight.setText(TextUtils.isEmpty(guardianshipBean.getWeight()) ? "未填写" : guardianshipBean.getWeight() + "kg");
        mTvBloodType.setText(TextUtils.isEmpty(guardianshipBean.getBloodType()) ? "未填写" : guardianshipBean.getBloodType() + "型");
        mTvAddress.setText(TextUtils.isEmpty(guardianshipBean.getDz()) ? "暂未填写" : guardianshipBean.getDz());
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
//                if (position == 0) {
//                    //家庭医生服务报告
//                    ToastUtils.showShort("该功能还在开发中...");
//                } else if (position == 1) {
//                    //健康管理报告
//                    Routerfit.register(GuardianshipRouterApi.class).skipHealthManagerReportActivity();
//                } else if (position == 2) {
//                    //预警信息记录
//                    Routerfit.register(GuardianshipRouterApi.class).skipWarningInformationRecordActivity();
//                } else if (position == 3) {
//                    //健康检测记录
//                    ToastUtils.showShort("该功能还在开发中...");
//                } else if (position == 4) {
//                    //监护圈
//                    Routerfit.register(GuardianshipRouterApi.class).skipCustodyCircleActivity(guardianshipBean);
//                } else if (position == 5) {
//                    //远程监控
//                    Routerfit.register(GuardianshipRouterApi.class).skipRemoteMeasureActivity(
//                            guardianshipBean.getWatchCode(), guardianshipBean.getBid() + "");
//                }

                if (position == 0) {
                    //监护圈
                    Routerfit.register(GuardianshipRouterApi.class).skipCustodyCircleActivity(guardianshipBean);
                } else if (position == 1) {
                    //健康检测记录
                    Routerfit.register(GuardianshipRouterApi.class).skipDetectionRecordTypeActivity();
                } else if (position == 2) {
                    //远程监控
                    Routerfit.register(GuardianshipRouterApi.class).skipRemoteMeasureActivity(
                            guardianshipBean.getWatchCode(), guardianshipBean.getBid() + "");
                }
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return new ResidentDetailPresenter(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_data_more) {
            ToastUtils.showShort("该功能还在开发中...");
        } else if (i == R.id.cv_head) {
            Routerfit.register(GuardianshipRouterApi.class).skipPersonDetailActivity(guardianshipBean);
        } else if (i == R.id.ll_location) {
            Routerfit.register(GuardianshipRouterApi.class).skipResidentLocationDetailActivity(guardianshipBean);
        }
    }

    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
        healthDataMenus.clear();
        HandRingHealthDataBena handRingHealthDataBena = (HandRingHealthDataBena) objects[0];
        healthDataMenus.add(new HealthDataMenu("高压", handRingHealthDataBena.getHighPressure(), "mmHg"));
        healthDataMenus.add(new HealthDataMenu("低压", handRingHealthDataBena.getLowPressure(), "mmHg"));
        healthDataMenus.add(new HealthDataMenu("心率", handRingHealthDataBena.getHeartRate(), "bpm"));
        adapter1.notifyDataSetChanged();
    }

    @Override
    protected void clickToolbarRight() {
        showVoiceOrVideoConnectDialog(guardianshipBean);
    }

    private void showVoiceOrVideoConnectDialog(GuardianshipBean familyBean) {
        FDialog.build()
                .setSupportFM(getSupportFragmentManager())
                .setLayoutId(R.layout.dialog_layout_voice_video_connect)
                .setWidth(AutoSizeUtils.pt2px(this, 710))
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        holder.getView(R.id.tv_video_connect).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showShort("视频通话");
                                String wyyxId = familyBean.getWyyxId();
                                String wyyxPwd = familyBean.getWyyxPwd();
                                if (!TextUtils.isEmpty(wyyxId)) {
                                    Routerfit.register(CommonRouterApi.class).getCallServiceImp()
                                            .launchNoCheckWithCallFamily(ResidentDetailActivity.this, wyyxId);
                                }
                                dialog.dismiss();
                            }
                        });
                        holder.getView(R.id.tv_voice_connect).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getWatchInfo(familyBean);
                                dialog.dismiss();
                            }
                        });
                        holder.getView(R.id.tv_cancel_connect).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setOutCancel(false)
                .setShowBottom(true)
                .show();
    }

    private void getWatchInfo(GuardianshipBean guardianshipBean) {
        Box.getRetrofit(GuardianshipApi.class)
                .getWatchInfo(guardianshipBean.getWatchCode())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<WatchInformationBean>() {
                    @Override
                    public void onNext(WatchInformationBean watchInformationBean) {
                        showPhoneTipsDialog(guardianshipBean.getBname(), watchInformationBean.getDeviceMobileNo());
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showPhoneTipsDialog(guardianshipBean.getBname(), guardianshipBean.getTel());
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
                        holder.setOnClickListener(R.id.tv_confirm, v -> {
                            CallPhoneUtils.instance().callPhone(ResidentDetailActivity.this, phone);
                            dialog.dismiss();
                        });
                        holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());
                    }
                })
                .show();
    }
}
