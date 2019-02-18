package com.gcml.healthfamily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_resource.utils.WeakHandler;
import com.sjtu.yifei.route.Routerfit;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setTranslucent(getWindow(), true);
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Routerfit.register(AppRouterApi.class).skipLoginActivity();
                finish();
            }
        }, 100);
    }
}