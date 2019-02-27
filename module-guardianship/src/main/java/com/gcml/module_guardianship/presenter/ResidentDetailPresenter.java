package com.gcml.module_guardianship.presenter;

import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.bean.HandRingHealthDataBena;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;

public class ResidentDetailPresenter extends BasePresenter {
    public ResidentDetailPresenter(IView view) {
        super(view);
    }

    @Override
    public void preData(Object... objects) {
        String userId = objects[0] + "";
        Box.getRetrofit(GuardianshipApi.class)
                .getHandRingHealthData(userId)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<HandRingHealthDataBena>() {
                    @Override
                    public void onNext(HandRingHealthDataBena handRingHealthDataBena) {
                        mView.loadDataSuccess(handRingHealthDataBena);
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
