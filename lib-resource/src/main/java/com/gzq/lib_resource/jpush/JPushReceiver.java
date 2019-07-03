package com.gzq.lib_resource.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.app.AppStore;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.utils.AppUtils;
import com.sjtu.yifei.route.Routerfit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                //同时更新角标
                AppStore.sosDeal.postValue(((int) KVUtils.get(KVConstants.KEY_SOS_DEAL_UNREAD_NUM, 0))+1);
                //通知页面刷新数据
                AppStore.sosJpush.postValue(true);
//                if (AppUtils.isAppBackground()) {
//                    Routerfit.register(CommonRouterApi.class).skipMainActivityNewTask();
//                } else {
//                    Routerfit.register(CommonRouterApi.class).skipMainActivity();
//                }

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Timber.d("[JPushReceiver] 用户点击打开了通知");
                //同时更新角标
                AppStore.sosDeal.postValue(((int) KVUtils.get(KVConstants.KEY_SOS_DEAL_UNREAD_NUM, 0))+1);
                //通知页面刷新数据
                AppStore.sosJpush.postValue(true);
                if (AppUtils.isAppBackground()) {
                    Routerfit.register(CommonRouterApi.class).skipMainActivityNewTask();
                } else {
                    Routerfit.register(CommonRouterApi.class).skipMainActivity();
                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            } else {
                Timber.d("[JPushReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }
}
