package com.gcml.auth.face2.utils;

import com.gcml.auth.face2.cache.BuildRxCache;
import com.gcml.auth.face2.cache.Cache;
import com.gcml.auth.face2.cache.CacheType;
import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.Box;

import java.io.File;
import java.util.ServiceLoader;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

public class RxCacheHelper {

    private volatile RxCache instance;

    public static RxCache get() {
        return RxCacheHelper.INSTANCE.instance();
    }

    public static <T> T provider(Class<T> provider) {
        return RxCacheHelper.INSTANCE.obtainProvider(provider);
    }

    public static File cacheDir() {
        return RxCacheHelper.INSTANCE.cacheDir;
    }

    public static File rxCacheDir() {
        return RxCacheHelper.INSTANCE.rxCacheDir;
    }

    public static void clearAllCache() {
        RxCacheHelper.get().evictAll();
    }

    private <T> T obtainProvider(Class<T> provider) {
        if (providerCache == null) {
            synchronized (this) {
                if (providerCache == null) {
                    providerCache = CacheHelper.factory().build(CacheType.CUSTOM_CACHE_TYPE);
                }
            }
        }
        Object obj = providerCache.get(provider.getName());
        if (obj == null) {
            synchronized (this) {
                obj = providerCache.get(provider.getName());
                if (obj == null) {
                    obj = RxCacheHelper.get().using(provider);
                    providerCache.put(provider.getName(), obj);
                }
            }
        }
        return (T) obj;
    }

    private volatile Cache<String, Object> providerCache;

    private final File cacheDir = FileHelper.getCacheDirectory(App.getApp(), "");

    private final File rxCacheDir = new File(cacheDir, "RxCache");

    private RxCache instance() {
        if (instance == null) {
            synchronized (RxCacheHelper.class) {
                if (instance == null) {
                    RxCache.Builder builder = new RxCache.Builder();
                    ServiceLoader<BuildRxCache> loader = ServiceLoader.load(BuildRxCache.class);
                    for (BuildRxCache buildRxCache : loader) {
                        buildRxCache.buildRxCache(App.getApp(), builder);
                    }
                    instance = builder.persistence(rxCacheDir, new GsonSpeaker(Box.getGson()));
                }
            }
        }
        return instance;
    }

    private static RxCacheHelper INSTANCE;

    static {
        INSTANCE = new RxCacheHelper();
    }
}
