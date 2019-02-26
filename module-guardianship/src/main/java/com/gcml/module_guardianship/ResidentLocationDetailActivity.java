package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.bean.FamilyBean;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.presenter.IResidentLocationView;
import com.gcml.module_guardianship.presenter.ResidentLocationPresenter;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.bean.MsgBean;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.AppUtils;
import com.gzq.lib_resource.utils.CallPhoneUtils;
import com.gzq.lib_resource.utils.MapMarkerUtils;
import com.gzq.lib_resource.utils.NavigationUtils;
import com.gzq.lib_resource.utils.data.TimeUtils;
import com.sjtu.yifei.annotation.Route;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

@Route(path = "/guardianship/resident/location/detail")
public class ResidentLocationDetailActivity extends StateBaseActivity implements View.OnClickListener, IResidentLocationView {

    private MapView mMapview;
    /**
     * 李小打发起紧急呼救
     */
    private TextView mTvSosTitle;
    /**
     * 浙江省杭州市萧山区建设二路957号（地铁站）
     */
    private TextView mTvSosAddress;
    /**
     * 2019.01.17 16:32
     */
    private TextView mTvSosTime;
    private ImageView mIvNavigation;
    /**
     * 呼叫李小打
     */
    private TextView mTvCallSelf;
    private RecyclerView mRvFamily;
    /**
     * 在完成紧急处理后请填写处理结果。
     */
    private EditText mEtWarningDealResult;
    /**
     * 预警信息确认
     */
    private TextView mTvWarningInfoConfirm;
    private ArrayList<FamilyBean> familyBeans = new ArrayList<FamilyBean>();
    private AMap aMap;
    private ImageView mIvRefreshFamilyLocation;
    private BaseQuickAdapter<FamilyBean, BaseViewHolder> adapter;
    private BaseQuickAdapter<String, BaseViewHolder> dialogAdapter;
    private MsgBean data;
    private ResidentLocationPresenter residentLocationPresenter;
    private FamilyBean doctorBean;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_resident_location_details;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        data = intentArgument.getParcelableExtra("data");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapview.onCreate(savedInstanceState);
        aMap = mMapview.getMap();
        setLocationStyle();
        setLocationMarks();
    }

    private void setLocationMarks() {
        LatLng latLng = new LatLng(39.906901, 116.397972);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("测试");
        markerOptions.snippet("测试：39.906901,116.397972");
        markerOptions.draggable(false);
        MapMarkerUtils.instance(this).customizeMarkerIcon(Box.getString(R.string.head_img), new MapMarkerUtils.OnMarkerIconLoadListener() {
            @Override
            public void markerIconLoadingFinished(View view, BitmapDescriptor bitmapDescriptor) {
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);

            }
        });
    }

    private void setLocationStyle() {
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(10000);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.common_ic_blue_location));
        aMap.setMyLocationStyle(myLocationStyle);

    }

    @Override
    protected void onStart() {
        super.onStart();
        residentLocationPresenter.preData(data.getUserId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapview.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapview.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapview.onSaveInstanceState(outState);
    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("紧急呼叫");
        mMapview = (MapView) findViewById(R.id.mapview);
        mTvSosTitle = (TextView) findViewById(R.id.tv_sos_title);
        mTvSosTitle.setText(data.getUserName() + "发起紧急呼叫");
        mTvSosAddress = (TextView) findViewById(R.id.tv_sos_address);
        mTvSosAddress.setText(data.getWarningAddress());
        mTvSosTime = (TextView) findViewById(R.id.tv_sos_time);
        mTvSosTime.setText(TimeUtils.milliseconds2String(data.getWarningTime(), new SimpleDateFormat("yyyy.MM.dd HH:mm")));
        mIvNavigation = (ImageView) findViewById(R.id.iv_navigation);
        mIvNavigation.setOnClickListener(this);
        mTvCallSelf = (TextView) findViewById(R.id.tv_call_self);
        mTvCallSelf.setOnClickListener(this);
        mRvFamily = (RecyclerView) findViewById(R.id.rv_family);
        mEtWarningDealResult = (EditText) findViewById(R.id.et_warning_deal_result);
        mTvWarningInfoConfirm = (TextView) findViewById(R.id.tv_warning_info_confirm);
        mTvWarningInfoConfirm.setOnClickListener(this);
        mIvRefreshFamilyLocation = (ImageView) findViewById(R.id.iv_refresh_family_location);
        mIvRefreshFamilyLocation.setOnClickListener(this);
        initRv();
    }

    private void initRv() {
        UserEntity user = Box.getSessionManager().getUser();
        mRvFamily.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRvFamily.addItemDecoration(new LinearLayoutDividerItemDecoration(28, 0));
        mRvFamily.setAdapter(adapter = new BaseQuickAdapter<FamilyBean, BaseViewHolder>(R.layout.item_layout_location_family, familyBeans) {
            @Override
            protected void convert(BaseViewHolder helper, FamilyBean item) {
                helper.setText(R.id.tv_name, item.getGuardianName());
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (user.getPhone().equals(familyBeans.get(position).getMobileNum())) {
                    showCannotCallSelfDialog();
                    return;
                }
                showPhoneTipsDialog(familyBeans.get(position));
            }
        });
    }


    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
        List<FamilyBean> object = (List<FamilyBean>) objects[0];
        for (FamilyBean familyBean : object) {
            if (familyBean.getGuardianType().contains("医生")) {
                doctorBean = familyBean;
                mTvCallSelf.setText("呼叫医生" + familyBean.getGuardianName());
            }
        }
        familyBeans.addAll(object);
        adapter.notifyDataSetChanged();

    }

    @Override
    public IPresenter obtainPresenter() {
        residentLocationPresenter = new ResidentLocationPresenter(this);
        return residentLocationPresenter;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.iv_navigation) {
            ArrayList<String> applications = checkMapApplication();
            if (applications.size() == 0) {
                ToastUtils.showShort("未在您的手机上检测第三方地图导航应用");
                return;
            }
            showBaiduOrGaodeNavigationDialog(applications);
        } else if (id == R.id.iv_refresh_family_location) {

        } else if (id == R.id.tv_call_self) {
            showPhoneTipsDialog(doctorBean);
        } else if (id == R.id.tv_warning_info_confirm) {
            postDealResult();
        }
    }

    private void postDealResult() {
        String result = mEtWarningDealResult.getText().toString().trim();
        if (TextUtils.isEmpty(result)){
            ToastUtils.showShort("必须填写处理结果");
            return;
        }
        residentLocationPresenter.postDealSOSResult(data.getWarningId(), result);
    }


    private ArrayList<String> checkMapApplication() {
        ArrayList<String> mapApplocations = new ArrayList<>();
        if (AppUtils.isInstalled("com.baidu.BaiduMap")) {
            mapApplocations.add("百度地图");
        }
        if (AppUtils.isInstalled("com.autonavi.minimap")) {
            mapApplocations.add("高德地图");
        }
        return mapApplocations;
    }

    private void showBaiduOrGaodeNavigationDialog(ArrayList<String> applications) {
        FDialog.build()
                .setSupportFM(getSupportFragmentManager())
                .setLayoutId(R.layout.dialog_layout_baidu_gaode_navigation)
                .setWidth(AutoSizeUtils.pt2px(this, 710))
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        RecyclerView rvDialog = holder.getView(R.id.rv_dialog);
                        rvDialog.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
                        rvDialog.addItemDecoration(new DividerItemDecoration(dialog.getContext(), DividerItemDecoration.VERTICAL));
                        rvDialog.setAdapter(dialogAdapter = new BaseQuickAdapter<String, BaseViewHolder>
                                (R.layout.item_dialog_layout_navigation, applications) {
                            @Override
                            protected void convert(BaseViewHolder helper, String item) {
                                helper.setText(R.id.tv_content, item);
                            }
                        });
                        dialogAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                if (position == 0) {
                                    //百度地图
                                    NavigationUtils
                                            .instance(ResidentLocationDetailActivity.this)
                                            .naviWithBaidu(39.906901, 116.397972);
                                } else if (position == 1) {
                                    //高德地图
                                    NavigationUtils
                                            .instance(ResidentLocationDetailActivity.this)
                                            .naviWithGaode(39.906901, 116.397972);
                                }
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

    private void showVoiceOrVideoConnectDialog() {
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
                            }
                        });
                        holder.getView(R.id.tv_voice_connect).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showShort("语音通话");
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

    private void showCannotCallSelfDialog() {
        FDialog.build()
                .setSupportFM(getSupportFragmentManager())
                .showConfirm()
                .setConfirmTextColor(R.color.colorAccent)
                .setContentText("不能拨打自己的号码")
                .show();
    }

    private void showPhoneTipsDialog(FamilyBean item) {
        FDialog.build()
                .setSupportFM(getSupportFragmentManager())
                .setLayoutId(R.layout.dialog_layout_phone_tips)
                .setWidth(AutoSizeUtils.pt2px(this, 540))
                .setOutCancel(false)
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        holder.setText(R.id.tv_title, item.getGuardianName() + "的电话号码");
                        holder.setText(R.id.tv_message, item.getMobileNum());
                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallPhoneUtils.instance().callPhone(ResidentLocationDetailActivity.this, item.getMobileNum());
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
    public void postDealSOSResultSuccess() {
        ToastUtils.showShort("处理成功");
        finish();
    }
}
