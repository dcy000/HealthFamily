package com.gcml.module_login_and_register.presenter;

import com.gcml.module_login_and_register.api.GuardianshipApi;
import com.gcml.module_login_and_register.bean.GuardianshipBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;

import java.util.List;

/**
 * Created by gzq on 19-2-6.
 */

public class GuardianshipPresenter extends BasePresenter {
    public GuardianshipPresenter(IView view) {
        super(view);
        preData();
    }

    @Override
    public void preData(Object... objects) {
        Box.getRetrofit(GuardianshipApi.class)
                .getGuardianships()
                .compose(RxUtils.<List<GuardianshipBean>>httpResponseTransformer())
                .as(RxUtils.<List<GuardianshipBean>>autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<List<GuardianshipBean>>() {
                    @Override
                    public void onNext(List<GuardianshipBean> guardianshipBeans) {
                        mView.loadDataSuccess(guardianshipBeans);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        mView.loadDataError(ex.code, ex.message);
                    }

                    @Override
                    protected void onNetError() {
                        mView.onNetworkError();
                    }
                });
    }

    @Override
    public void refreshData(Object... objects) {

    }

    @Override
    public void loadMoreData(Object... objects) {

    }
}
