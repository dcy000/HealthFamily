package com.ml.module_shouhuan.presenter;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;
import com.ml.module_shouhuan.api.ShouhuanApi;
import com.gzq.lib_resource.bean.MsgBean;

import java.util.List;

import timber.log.Timber;

public class MsgTodoPresenter extends BasePresenter {
    public MsgTodoPresenter(IView view) {
        super(view);
    }

    public void preData(Object... objects) {
        UserEntity user = Box.getSessionManager().getUser();
        //处理状态 0：未处理 1：已处理
        Box.getRetrofit(ShouhuanApi.class)
                .getMsg(user.getUserId(), "0")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<List<MsgBean>>() {
                    @Override
                    public void onNext(List<MsgBean> msgBeans) {
                        Timber.i("加载数据成功onNext");
                        mView.loadDataSuccess(msgBeans);
                    }

                    @Override
                    protected void onEmptyData() {
                        mView.loadDataEmpty();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        if (ex.code == 500) {
                            ToastUtils.showShort("您还没有监护的居民");
                        }
                        mView.loadDataError();
                    }
                });
    }

}
