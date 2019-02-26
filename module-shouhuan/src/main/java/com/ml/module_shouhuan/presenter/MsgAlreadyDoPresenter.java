package com.ml.module_shouhuan.presenter;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;
import com.ml.module_shouhuan.api.ShouhuanApi;
import com.gzq.lib_resource.bean.MsgBean;

import java.util.List;

public class MsgAlreadyDoPresenter extends BasePresenter {
    public MsgAlreadyDoPresenter(IView view) {
        super(view);
    }

    @Override
    public void preData(Object... objects) {
        UserEntity user = Box.getSessionManager().getUser();
        //处理状态 0：未处理 1：已处理
        Box.getRetrofit(ShouhuanApi.class)
                .getMsg(user.getUserId(), "1")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<List<MsgBean>>() {
                    @Override
                    public void onNext(List<MsgBean> msgBeans) {
                        mView.loadDataSuccess(msgBeans);
                    }

                    @Override
                    protected void onEmptyData() {
                        mView.loadDataEmpty();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        mView.loadDataError();
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
