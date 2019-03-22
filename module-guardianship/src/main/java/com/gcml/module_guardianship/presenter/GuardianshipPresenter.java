package com.gcml.module_guardianship.presenter;

import com.gcml.module_guardianship.MainGuardianshipFragment;
import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;

import java.util.List;

/**
 * Created by gzq on 19-2-6.
 */

public class GuardianshipPresenter extends BasePresenter<MainGuardianshipFragment> {

    public GuardianshipPresenter(MainGuardianshipFragment view) {
        super(view);
    }

    public void preData(Object... objects) {
        UserEntity user = Box.getSessionManager().getUser();
        Box.getRetrofit(GuardianshipApi.class)
                .getGuardianships(user.getUserId())
                .compose(RxUtils.<List<GuardianshipBean>>httpResponseTransformer())
                .as(RxUtils.<List<GuardianshipBean>>autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<List<GuardianshipBean>>() {
                    @Override
                    public void onNext(List<GuardianshipBean> guardianshipBeans) {
                        mView.loadDataSuccess(guardianshipBeans);
                    }

                    @Override
                    protected void onEmptyData() {
                        mView.loadDataEmpty();
                    }

                    @Override
                    protected void onNetError() {
                        mView.onNetworkError();
                    }
                });
    }
}
