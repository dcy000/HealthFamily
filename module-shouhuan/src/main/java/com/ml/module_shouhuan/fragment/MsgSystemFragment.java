package com.ml.module_shouhuan.fragment;

import android.os.Bundle;
import android.view.View;

import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.ml.module_shouhuan.R;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/shouhuan/msgSystem")
public class MsgSystemFragment extends StateBaseFragment {
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.fragment_msg_system;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
