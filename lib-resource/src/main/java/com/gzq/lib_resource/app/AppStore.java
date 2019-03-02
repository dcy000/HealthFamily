package com.gzq.lib_resource.app;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.session.SessionManager;
import com.gzq.lib_core.session.SessionStateChangedListener;
import com.gzq.lib_resource.BuildConfig;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.state_page.DevelopmentPage;
import com.gzq.lib_resource.state_page.EmptyPage;
import com.gzq.lib_resource.state_page.ErrorPage;
import com.gzq.lib_resource.state_page.LoadingPage;
import com.gzq.lib_resource.state_page.NetErrorPage;
import com.gzq.lib_resource.utils.AppUtils;
import com.gzq.lib_resource.utils.Handlers;
import com.kingja.loadsir.core.LoadSir;
import com.sjtu.yifei.route.Routerfit;
import com.tencent.bugly.crashreport.CrashReport;

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

        //观察用户系统的变化
        observerSessions();
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
                            //极光设置别名，友盟登陆
//                            new JpushAliasUtils(Box.getApp()).setAlias("user_" + user.bid);
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
            }
        });

    }
}
