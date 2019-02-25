package com.gcml.module_health_manager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.gzq.lib_resource.dialog.DialogClickListener;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

/**
 * Created by gzq on 19-2-3.
 */
@Route(path = "/healthmanager/main")
public class MainHealthManagerFragment extends StateBaseFragment {
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.health_manager_fragment_main;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        showSuccess();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
            @Override
            public void preData(Object... objects) {

            }

            @Override
            public void refreshData(Object... objects) {

            }

            @Override
            public void loadMoreData(Object... objects) {

            }
        };
    }

    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
    }

    @Override
    public void loadDataError(Object... objects) {

    }

    @Override
    public void onNetworkError() {

    }
}
