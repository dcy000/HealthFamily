package com.gzq.lib_resource.state_page;

import android.content.Context;
import android.view.View;

import com.gzq.lib_resource.R;
import com.kingja.loadsir.callback.Callback;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/31 9:46
 * created by: gzq
 * description: TODO
 */
public class LoadingPage extends Callback{
    @Override
    protected int onCreateView() {
        return R.layout.view_loading_page;
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        //true 屏蔽reload事件
        return true;
    }
}
