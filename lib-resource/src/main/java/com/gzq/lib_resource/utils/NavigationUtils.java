package com.gzq.lib_resource.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.SupportActivity;

import com.gzq.lib_core.base.BaseLifecycle;

public class NavigationUtils extends BaseLifecycle {
    private SupportActivity activity;
    private static NavigationUtils navigationUtils;

    @SuppressLint("RestrictedApi")
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.activity.getLifecycle().removeObserver(this);
        navigationUtils=null;
    }

    @SuppressLint("RestrictedApi")
    public NavigationUtils(SupportActivity activity) {
        this.activity = activity;
        this.activity.getLifecycle().addObserver(this);
    }

    public static NavigationUtils instance(SupportActivity activity) {
        if (navigationUtils == null) {
            navigationUtils = new NavigationUtils(activity);
        }
        return navigationUtils;
    }

    /**
     * 使用高德地图导航
     *
     * @param mLat
     * @param mLon
     */
    public void naviWithGaode(double mLat, double mLon) {
        double a[] = GPSUtil.gcj02_To_Gps84(mLat, mLon);//lon 维度  lat 经度
        double lat = a[0];
        double lon = a[1];
        String uri = "amapuri://route/plan/"
                + "?dlat=" + lat + "&dlon=" + lon
                + "&sname=我的位置"
                + "&dname=终点"
                + "&dev=1"
                + "&t=0";
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.autonavi.minimap");
        activity.startActivity(intent);
    }

    /**
     * 使用百度地图导航
     *
     * @param mLat
     * @param mLon
     */
    public void naviWithBaidu(double mLat, double mLon) {
        double b[] = GPSUtil.gcj02_To_Gps84(mLat, mLon);//lon 维度  lat 经度
        double lat = b[0];
        double lon = b[1];
        String uri = "baidumap://map/direction"
                + "?origin=我的位置"
                + "&destination=name:终点|latlng:" + lat + "," + lon
                + "&coord_type=bd09ll"
                + "&mode=driving"
                + "&src=andr.杭州国辰迈联机器人科技有限公司.智慧E健康";//src为统计来源必填，companyName、appName是公司名和应用名
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.addCategory("android.intent.category.DEFAULT");
        activity.startActivity(intent);
    }
}
