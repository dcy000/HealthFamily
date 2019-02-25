package com.gzq.lib_resource.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class NavigationUtils {
    private Activity activity;
    private static NavigationUtils navigationUtils;

    public NavigationUtils(Activity activity) {
        this.activity = activity;
    }

    public static NavigationUtils instance(Activity activity) {
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
//        double a[] = GPSUtil.gcj02_To_Gps84(mLat, mLon);//lon 维度  lat 经度
//        double lat = a[0];
//        double lon = a[1];
//        Uri mUri = Uri.parse("geo:" + lat + "" + "," + lon + "" + "?q=" + endDescription);
//        Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
//        activity.startActivity(mIntent);

        String uri = "amapuri://route/plan/"
                + "?dlat="+ mLat+"&dlon="+mLon
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
     * @param mLat
     * @param mLon
     */
    public void naviWithBaidu(double mLat, double mLon) {
//        double b[] = GPSUtil.gcj02_To_Gps84(mLat, mLon);//lon 维度  lat 经度
//        double lat2 = b[0];
//        double lon2 = b[1];
//        Uri mUri = Uri.parse("geo:" + lat2 + "" + "," + lon2 + "" + "?q=" + endDescription);
//        Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
//        activity.startActivity(mIntent);

        String uri = "baidumap://map/direction"
                + "?origin=我的位置"
                +"&destination=name:终点|latlng:"+mLat+","+mLon
                +"&coord_type=bd09ll"
                + "&mode=driving"
                + "&src=andr.杭州国辰迈联机器人科技有限公司.智慧E健康";//src为统计来源必填，companyName、appName是公司名和应用名
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.addCategory("android.intent.category.DEFAULT");
        activity.startActivity(intent);
    }
}
