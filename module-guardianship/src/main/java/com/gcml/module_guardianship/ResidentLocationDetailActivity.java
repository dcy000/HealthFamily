package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.presenter.IResidentLocationView;
import com.gcml.module_guardianship.presenter.ResidentLocationPresenter;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.AppUtils;
import com.gzq.lib_resource.utils.MapMarkerUtils;
import com.gzq.lib_resource.utils.NavigationUtils;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;

import me.jessyan.autosize.utils.AutoSizeUtils;

@Route(path = "/guardianship/resident/location/detail")
public class ResidentLocationDetailActivity extends StateBaseActivity implements View.OnClickListener, IResidentLocationView {
    private MapView mMapview;
    private ImageView mIvRefreshFamilyLocation;
    /**
     * 李小打发起紧急呼救
     */
    private TextView mTvSosTitle;
    private ImageView mIvNavigation;
    private AMap aMap;
    private GuardianshipBean guardianshipBean;
    private ResidentLocationPresenter residentLocationPresenter;
    private LatLng latLng;
    private BaseQuickAdapter<String, BaseViewHolder> dialogAdapter;
    private double lat, lon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapview.onCreate(savedInstanceState);
        aMap = mMapview.getMap();
        setLocationStyle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        residentLocationPresenter.getHandRingLatLon(guardianshipBean.getBid());
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
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_location_details;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        guardianshipBean = intentArgument.getParcelableExtra("data");
    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("详细地址");
        mMapview = (MapView) findViewById(R.id.mapview);
        mIvRefreshFamilyLocation = (ImageView) findViewById(R.id.iv_refresh_family_location);
        mIvRefreshFamilyLocation.setOnClickListener(this);
        mTvSosTitle = (TextView) findViewById(R.id.tv_sos_title);
        mTvSosTitle.setText(guardianshipBean.getDz());
        mIvNavigation = (ImageView) findViewById(R.id.iv_navigation);
        mIvNavigation.setOnClickListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        residentLocationPresenter = new ResidentLocationPresenter(this);
        return residentLocationPresenter;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_refresh_family_location) {
            residentLocationPresenter.getHandRingLatLon(guardianshipBean.getBid());
            showRotateAnim(mIvRefreshFamilyLocation);
        } else if (i == R.id.iv_navigation) {
            ArrayList<String> applications = checkMapApplication();
            if (applications.size() == 0) {
                ToastUtils.showShort("未在您的手机上检测第三方地图导航应用");
                return;
            }
            showBaiduOrGaodeNavigationDialog(applications);
        } else {
        }
    }


    private void showRotateAnim(View view) {
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        view.startAnimation(operatingAnim);
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
                                            .instance(ResidentLocationDetailActivity.this)
                                            .naviWithBaidu(lat, lon);
                                } else if (position == 1) {
                                    //高德地图
                                    NavigationUtils
                                            .instance(ResidentLocationDetailActivity.this)
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

    @Override
    public void postDealSOSResultSuccess() {

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
        markerOptions.title("未知");
        markerOptions.snippet("经纬度：" + latLng.latitude + "," + latLng.longitude);
        markerOptions.draggable(false);
        MapMarkerUtils.instance(this).customizeMarkerIcon(guardianshipBean.getUserPhoto(), new MapMarkerUtils.OnMarkerIconLoadListener() {
            @Override
            public void markerIconLoadingFinished(View view, BitmapDescriptor bitmapDescriptor) {
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);

            }
        });
        //TODO:需要根据经纬度获取地理名称
//        getAddressByLatlng(latLng);
    }
}
