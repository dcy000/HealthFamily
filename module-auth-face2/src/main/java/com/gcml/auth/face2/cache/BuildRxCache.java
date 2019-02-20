package com.gcml.auth.face2.cache;

import android.content.Context;

import io.rx_cache2.internal.RxCache;


public interface BuildRxCache {
    void buildRxCache(Context context, RxCache.Builder builder);
}

