package com.gzq.lib_core.base;

import android.app.Application;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.base.quality.LeakCanaryUtil;
import com.gzq.lib_core.session.SessionManager;
import com.gzq.lib_core.utils.KVUtils;

import me.jessyan.autosize.AutoSizeConfig;
import retrofit2.Retrofit;
import timber.log.Timber;

public class Box implements AppLifecycle {
    private static final String TAG = "Box";
    private static Application mApplication;
    private static Gson gson;
    private static Retrofit retrofit;

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        //初始化全局变量
        mApplication = application;
        gson = ObjectFactory.getGson(mApplication, App.getGlobalConfig());
        retrofit = ObjectFactory.getRetrofit(mApplication, App.getGlobalConfig());
        //初始化屏幕适配器
        ObjectFactory.initAutoSize(App.getGlobalConfig());
        //初始化LeakCanary
        LeakCanaryUtil.getInstance().init(application);
        //初始化KVUtil
        KVUtils.init(application);
        //用户信息管理器
        ObjectFactory.initSessionManager(mApplication, App.getGlobalConfig());
        //崩溃拦截配置
        ObjectFactory.initCrashManager(mApplication, App.getGlobalConfig());
        Timber.tag(TAG).i("onCreate");
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        ObjectFactory.clear();
        mApplication = null;
        gson = null;
        retrofit = null;
        Timber.tag(TAG).i("onTerminate");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
    }


    public static Application getApp() {
        return mApplication;
    }

    /**
     * Gson
     *
     * @return
     */
    public static Gson getGson() {
        return gson;
    }

    /**
     * @param serviceClazz
     * @param <T>
     * @return
     */
    public static <T> T getRetrofit(Class<T> serviceClazz) {
        return retrofit.create(serviceClazz);
    }

    /**
     * 数据库Room
     *
     * @param databaseClazz
     * @param <DB>
     * @return
     */
    public static <DB extends RoomDatabase> DB getRoomDataBase(Class<? extends RoomDatabase> databaseClazz) {
        return ObjectFactory.getRoomDatabase(mApplication, databaseClazz, App.getGlobalConfig());
    }

    /**
     * 获取缓存数据库
     *
     * @param databaseClazz
     * @param <DB>
     * @return
     */
    public static <DB extends RoomDatabase> DB getCacheRoomDataBase(Class<? extends RoomDatabase> databaseClazz) {
        return ObjectFactory.getCacheRoomDatabase(mApplication, databaseClazz);
    }

    /**
     * 用户信息管理器
     *
     * @return
     */
    public static SessionManager getSessionManager() {
        return SessionManager.get();
    }

    /**
     * 为了解决在fragment中使用{@link android.support.v4.app.Fragment#getString(int)}
     * 偶尔会报java.lang.IllegalStateException-->Fragment xxxxx{xxx} not attached to a context的错误
     *
     * @param id 字符串资源id
     * @return
     */
    public static String getString(@StringRes int id) {
        return getApp().getResources().getString(id);
    }

    /**
     * 为了解决在fragment中使用{@link Fragment#getContext()} getContext().getColor(int color)
     * 偶尔会报java.lang.IllegalStateException-->Fragment xxxxx{xxx} not attached to a context的错误
     *
     * @param id 颜色资源id
     * @return
     */
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getApp(), id);
    }
}
