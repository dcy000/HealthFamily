package com.gzq.lib_core.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.gzq.lib_core.BuildConfig;
import com.gzq.lib_core.base.delegate.AppDelegate;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.base.delegate.GlobalModule;
import com.gzq.lib_core.base.delegate.MetaValue;
import com.gzq.lib_core.base.quality.LeakCanaryUtil;
import com.gzq.lib_core.log.CrashReportingTree;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.ManifestParser;

import java.util.List;

import timber.log.Timber;

public class App extends Application {
    private static final String TAG = "App";
    private AppLifecycle appLifecycle;
    private static Application instance;
    private static GlobalConfig globalConfig;
    private static GlobalConfig.Builder globalBuilder;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化分包插件
        MultiDex.install(base);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        if (appLifecycle == null) {
            appLifecycle = new AppDelegate(base);
        }
        appLifecycle.attachBaseContext(base);
        Timber.tag(TAG).i("attachBaseContext: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initGlobalConfig();
        //初始化屏幕适配器
        ObjectFactory.initAutoSize(App.getGlobalConfig());
        //初始化LeakCanary
        LeakCanaryUtil.getInstance().init(instance);
        //初始化KVUtil
        KVUtils.init(instance);
        //用户信息管理器
        ObjectFactory.initSessionManager(instance, App.getGlobalConfig());
        //崩溃拦截配置
        ObjectFactory.initCrashManager(instance, App.getGlobalConfig());

        appLifecycle.onCreate(instance);
        Timber.tag(TAG).i("onCreate");

    }

    private void initGlobalConfig() {
        //先初始化全局配置，然后进行生命周期的分发
        List<GlobalModule> globalModules = new ManifestParser<GlobalModule>(instance, MetaValue.GLOBAL_CONFIG).parse();
        if (globalModules == null) {
            throw new IllegalArgumentException("Please config global");
        }
        if (globalModules != null && globalModules.size() > 1) {
            throw new IllegalArgumentException("Only one GlobalConfig initialization is allowed");
        }
        GlobalModule globalModule = globalModules.get(0);
        globalBuilder = GlobalConfig.builder();
        globalModule.applyOptions(instance, globalBuilder);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        appLifecycle.onTerminate(instance);
        appLifecycle = null;
        instance = null;
        globalConfig = null;
        globalBuilder = null;
        Log.i(TAG, "onTerminate: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        appLifecycle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
        Timber.tag(TAG).i("onConfigurationChanged:");
    }

    /**
     * 获取Application的实例，一般在工具类或者生命周期较早的初始化中使用
     *
     * @return
     */
    public static Application getApp() {
        return instance;
    }

    public static GlobalConfig getGlobalConfig() {
        if (globalConfig == null) {
            globalConfig = globalBuilder.build();
        }
        return globalConfig;
    }
}
