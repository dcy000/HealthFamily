package com.gcml.module_sos_deal;

import android.os.Bundle;
import android.view.View;

import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

/**
 * Created by gzq on 19-2-3.
 */
@Route(path = "/sosdeal/main")
public class MainSOSDealFragment extends StateBaseFragment{
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.main_fragment_sos_deal;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {

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

    }

    @Override
    public void loadDataError(Object... objects) {

    }

    @Override
    public void onNetworkError() {

    }
}
