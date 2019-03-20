package com.gzq.lib_resource.app;

import android.app.Application;
import android.app.Notification;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.session.SessionManager;
import com.gzq.lib_core.session.SessionStateChangedListener;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_resource.BuildConfig;
import com.gzq.lib_resource.R;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.state_page.DevelopmentPage;
import com.gzq.lib_resource.state_page.EmptyPage;
import com.gzq.lib_resource.state_page.ErrorPage;
import com.gzq.lib_resource.state_page.LoadingPage;
import com.gzq.lib_resource.state_page.NetErrorPage;
import com.gzq.lib_resource.utils.AppUtils;
import com.gzq.lib_resource.utils.Handlers;
import com.gzq.lib_resource.utils.TagAliasOperatorHelper;
import com.kingja.loadsir.core.LoadSir;
import com.sjtu.yifei.route.Routerfit;
import com.tencent.bugly.crashreport.CrashReport;
import com.xuexiang.xaop.XAOP;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import me.yokeyword.fragmentation.Fragmentation;
import timber.log.Timber;

/**
 * created on 2018/10/24 17:01
 * description:业务全局实体
 */
public class AppStore implements AppLifecycle {

    private static final String BUGGLY_APPID = "8931446044";
    public static MutableLiveData<Integer> guardianship = new MutableLiveData<>();
    public static MutableLiveData<Integer> healthManager = new MutableLiveData<>();
    public static MutableLiveData<Integer> sosDeal = new MutableLiveData<>();
    public static MutableLiveData<Integer> mine = new MutableLiveData<>();
    public static MutableLiveData<Boolean> isShowMsgFragment = new MutableLiveData<>();
    private int jpushSerialNumber = 12984012;

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        //初始化Fragment框架Fragmentation
        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                .install();


        //初始化Buggly
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(application);
        userStrategy.setAppChannel(AppUtils.getAppInfo().getPackageName());
        userStrategy.setAppVersion(AppUtils.getAppInfo().getVersionName());
        CrashReport.initCrashReport(application, BUGGLY_APPID, BuildConfig.DEBUG, userStrategy);

        //初始化状态页面控件LoadSir
        LoadSir.beginBuilder()
                .addCallback(new ErrorPage())
                .addCallback(new LoadingPage())
                .addCallback(new EmptyPage())
                .addCallback(new NetErrorPage())
                .addCallback(new DevelopmentPage())
                .setDefaultCallback(LoadingPage.class)
                .commit();
        //初始化ARetrofit路由框架
        Routerfit.init(application);

        //初始化极光
        JPushInterface.setDebugMode(BuildConfig.DEBUG); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(application); // 初始化 JPush
        TagAliasOperatorHelper.getInstance().init(application);
        setDefaultJpushNotification();
        //观察用户系统的变化
        observerSessions();
        //初始化XAOP
        XAOP.init(application);
        //不适用日志切片
        XAOP.debug(false);
    }

    private void setDefaultJpushNotification() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(App.getApp());
        builder.statusBarDrawable = R.drawable.resource_desktop_logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    /**
     * 如果Sessions发生变化了则重新登录一次网易云信账号
     * 并且全局只设置一处监听即可
     */
    private void observerSessions() {

        Box.getSessionManager().addSessionStateChangedListener(new SessionStateChangedListener() {
            @Override
            public void onUserInfoChanged(SessionManager sessionManager) {
                final UserEntity user = sessionManager.getUser();
                Timber.tag("SessionManager").i(user.toString());
                Handlers.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(user.getWyyxId()) && !TextUtils.isEmpty(user.getWyyxPwd())) {
                            Timber.i("网易视频初始化");
                            //视频通话必须在主线程登录
                            Routerfit.register(CommonRouterApi.class).getCallServiceImp()
                                    .loginWY(user.getWyyxId(), user.getWyyxPwd());
                        }
                        if (user != null) {
                            //极光设置别名
                            if (JPushInterface.isPushStopped(App.getApp())) {
                                JPushInterface.resumePush(App.getApp());
                            }
                            TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean(
                                    TagAliasOperatorHelper.ACTION_SET,
                                    null,
                                    user.getWyyxId(),
                                    true
                            );
                            TagAliasOperatorHelper.getInstance().handleAction(App.getApp(), jpushSerialNumber++, tagAliasBean);
                            Timber.i("极光设置别名");
                        }
                    }
                });
            }

            @Override
            public void onTokenInfoChanged(SessionManager sessionManager) {

            }

            @Override
            public void onUserInfoCleared(SessionManager sessionManager) {
                //退出网易IM
                Routerfit.register(CommonRouterApi.class).getCallServiceImp().logoutWY();
                //关闭极光推送
                JPushInterface.stopPush(App.getApp());
                //清理底部按钮未读的消息数
                KVUtils.put(KVConstants.KEY_SOS_DEAL_UNREAD_NUM, 0);
                //通常如果用户的信息被清理，会重新跳转到登录页面，故全局统一处理
                //跳转到登录页
                Routerfit.register(CommonRouterApi.class).skipLoginActivityWithNewTask();
            }
        });

    }
}
