package com.gcml.call;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.gzq.lib_core.base.delegate.AppLifecycle;

public class CallApp implements AppLifecycle {
    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        CallInitHelper.getInstance().init(application, true);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }
}
