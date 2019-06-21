package com.gcml.call;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.gzq.lib_core.base.delegate.AppLifecycle;

public class CallAppLifecycleCallbacks implements AppLifecycle {

    @Override
    public void attachBaseContext(@NonNull Context base) {
        CallApp.INSTANCE.attachBaseContext(null, base);
    }

    @Override
    public void onCreate(Application app) {
        CallApp.INSTANCE.onCreate(app);
    }

    @Override
    public void onTerminate(Application app) {
        CallApp.INSTANCE.onTerminate(app);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }
}
