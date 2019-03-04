package com.gcml.healthfamily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.sjtu.yifei.route.Routerfit;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setTranslucent(getWindow(), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Box.getSessionManager().isLogin()) {
            Routerfit.register(CommonRouterApi.class).skipMainActivity();
        } else {
            Routerfit.register(AppRouterApi.class).skipLoginActivity();
        }
        finish();
    }
}
