package com.gzq.lib_resource.click;

import android.view.View;

public abstract class UnFastClickListener implements View.OnClickListener {
    private long mLastClickTime = 0;
    private long timeInterval = 1000L;

    public UnFastClickListener() {
    }

    public UnFastClickListener(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        long inter = nowTime - mLastClickTime;
        if (inter > timeInterval) {
            //单次点击
            onSingleClick(v);
        } else {
            //快速点击
            onFastClick(v, inter);
        }
    }

    public abstract void onSingleClick(View v);

    public abstract void onFastClick(View v, long interval);
}
