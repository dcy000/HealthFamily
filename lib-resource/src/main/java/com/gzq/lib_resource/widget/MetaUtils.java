package com.gzq.lib_resource.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class MetaUtils {
    public static int getAppMetadata(Context context, String metaDataKey) {
        ApplicationInfo info;
        try {
            PackageManager pm = context.getPackageManager();
            info = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            return info.metaData.getInt(metaDataKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
