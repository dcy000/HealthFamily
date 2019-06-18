package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.bean.FamilyBean;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.gcml.module_guardianship.presenter.IResidentLocationView;
import com.gcml.module_guardianship.presenter.ResidentLocationPresenter;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.CommonApi;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.app.AppStore;
import com.gzq.lib_resource.bean.MsgBean;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.AppUtils;
import com.gzq.lib_resource.utils.CallPhoneUtils;
import com.gzq.lib_resource.utils.MapMarkerUtils;
import com.gzq.lib_resource.utils.NavigationUtils;
import com.gzq.lib_resource.utils.data.TimeUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

@Route(path = "/guardianship/resident/sos/location/detail")
public class ResidentSOSLocationDetailActivity extends StateBaseActivity<ResidentLocationPresenter> implements View.OnClickListener, IResidentLocationView, GeocodeSearch.OnGeocodeSearchListener {

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
    private LatLng latLng;
    private double lat, lon;

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
        getP().preData(data.getUserId());
        getP().getHandRingLatLon(data.getUserId());

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
        getTitleTextView().setText("紧急呼叫");
        mMapview = (MapView) findViewById(R.id.mapview);
        mTvSosTitle = (TextView) findViewById(R.id.tv_sos_title);
        mTvSosAddress = (TextView) findViewById(R.id.tv_sos_address);
        if (data.getWarningType().equals("0")) {
            mTvSosTitle.setText(data.getUserName() + "测量数据异常");
            mTvSosAddress.setText(data.getWarningContent());
        } else if (data.getWarningType().equals("1")) {
            mTvSosTitle.setText(data.getUserName() + " 发起紧急呼叫");
            mTvSosAddress.setText(data.getWarningAddress());
        } else if (data.getWarningType().equals("2")) {

        }
        mTvSosTime = (TextView) findViewById(R.id.tv_sos_time);
        mTvSosTime.setText(TimeUtils.milliseconds2String(data.getWarningTime(), new SimpleDateFormat("yyyy.MM.dd HH:mm")));
        mIvNavigation = (ImageView) findViewById(R.id.iv_navigation);
        mIvNavigation.setOnClickListener(this);
        mTvCallSelf = (TextView) findViewById(R.id.tv_call_self);
        mTvCallSelf.setText("呼叫" + data.getUserName());
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
//                showPhoneTipsDialog(familyBeans.get(position));
                showVoiceOrVideoConnectDialog(familyBeans.get(position));
            }
        });
    }

    private void callFamilyWithVideo(FamilyBean familyBean) {
        Box.getRetrofit(CommonApi.class)
                .getProfile(familyBean.getGuardianId() + "")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {

                    }
                });

    }


    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
        List<FamilyBean> object = (List<FamilyBean>) objects[0];
//        for (FamilyBean familyBean : object) {
//            if (familyBean.getGuardianType().contains("医生")) {
//                doctorBean = familyBean;
//                mTvCallSelf.setText("呼叫医生" + familyBean.getGuardianName());
//            }
//        }
        familyBeans.clear();
        familyBeans.addAll(object);
        UserEntity user = Box.getSessionManager().getUser();
        for (FamilyBean bean : familyBeans) {
            if (TextUtils.equals(user.getUserName(), bean.getGuardianName())) {
                familyBeans.remove(bean);
                break;
            }
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public IPresenter obtainPresenter() {
        return new ResidentLocationPresenter(this);
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
            getP().getHandRingLatLon(data.getUserId());
            showRotateAnim(mIvRefreshFamilyLocation);
        } else if (id == R.id.tv_call_self) {
            getWatchInfo(data);
        } else if (id == R.id.tv_warning_info_confirm) {
            postDealResult();
        }
    }

    private void getWatchInfo(MsgBean guardianshipBean) {
        Box.getRetrofit(GuardianshipApi.class)
                .getWatchInfo(guardianshipBean.getEquipmentId())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<WatchInformationBean>() {
                    @Override
                    public void onNext(WatchInformationBean watchInformationBean) {
                        showPhoneTipsDialog(guardianshipBean.getUserName(), watchInformationBean.getDeviceMobileNo());
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
                                CallPhoneUtils.instance().callPhone(ResidentSOSLocationDetailActivity.this, phone);
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


    private void showRotateAnim(View view) {
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        view.startAnimation(operatingAnim);
    }

    private void postDealResult() {
        String result = mEtWarningDealResult.getText().toString().trim();
        if (TextUtils.isEmpty(result)) {
            ToastUtils.showShort("必须填写处理结果");
            return;
        }
        getP().postDealSOSResult(data.getWarningId(), result);
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
                                if (lat == 0 && lon == 0) {
                                    ToastUtils.showShort("未获取到居民的位置信息，无法进行导航");
                                    return;
                                }
                                if (position == 0) {
                                    //百度地图
                                    NavigationUtils
                                            .instance(ResidentSOSLocationDetailActivity.this)
                                            .naviWithBaidu(lat, lon);
                                } else if (position == 1) {
                                    //高德地图
                                    NavigationUtils
                                            .instance(ResidentSOSLocationDetailActivity.this)
                                            .naviWithGaode(lat, lon);
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

    private void showVoiceOrVideoConnectDialog(FamilyBean familyBean) {
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
                                            .launchNoCheckWithCallFamily(ResidentSOSLocationDetailActivity.this, wyyxId);
                                }
                                dialog.dismiss();
                            }
                        });
                        holder.getView(R.id.tv_voice_connect).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showPhoneTipsDialog(familyBean);
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
                                CallPhoneUtils.instance().callPhone(ResidentSOSLocationDetailActivity.this, item.getMobileNum());
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
        Integer value = AppStore.sosDeal.getValue();
        if (value!=null&&value>0){
            AppStore.sosDeal.postValue(value-1);
        }
        finish();
    }

    @Override
    public void getHandRingLatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        setLocationMarks(lat, lon);
    }

    private void setLocationMarks(double lat, double lon) {
        latLng = new LatLng(lat, lon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
//        markerOptions.title("浙江省杭州市萧山区建设一路");
        markerOptions.snippet("经纬度：" + latLng.latitude + "," + latLng.longitude);
        markerOptions.draggable(false);
        MapMarkerUtils.instance(this).customizeMarkerIcon(data.getUserPhoto(), new MapMarkerUtils.OnMarkerIconLoadListener() {
            @Override
            public void markerIconLoadingFinished(View view, BitmapDescriptor bitmapDescriptor) {
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);

            }
        });
        //TODO:需要根据经纬度获取地理名称
//        getAddressByLatlng(latLng);
    }

    /**
     * 根据经纬度逆向获取地理信息
     *
     * @param latLng
     */
    private void getAddressByLatlng(LatLng latLng) {
        //地理搜索类
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        ToastUtils.showShort("onRegeocodeSearched：" + Box.getGson().toJson(regeocodeResult));
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        String simpleAddress = formatAddress.substring(9);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(simpleAddress);
        markerOptions.snippet("经纬度：" + latLng.latitude + "," + latLng.longitude);
        markerOptions.draggable(false);
        MapMarkerUtils.instance(this).customizeMarkerIcon(Box.getString(R.string.head_img), new MapMarkerUtils.OnMarkerIconLoadListener() {
            @Override
            public void markerIconLoadingFinished(View view, BitmapDescriptor bitmapDescriptor) {
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);

            }
        });
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        ToastUtils.showShort("onGeocodeSearched：" + Box.getGson().toJson(geocodeResult));
    }
}
