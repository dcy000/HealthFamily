package com.gcml.healthfamily;

import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_resource.api.IFinishActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/finish/main/activity")
public class FinishActivityImp implements IFinishActivity {
    @Override
    public void finishActivity() {
        ActivityUtils.finishActivity(MainActivity.class);
    }
}
