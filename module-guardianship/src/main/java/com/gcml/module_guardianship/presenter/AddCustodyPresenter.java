package com.gcml.module_guardianship.presenter;

import android.text.TextUtils;

import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;

public class AddCustodyPresenter extends BasePresenter {
    public AddCustodyPresenter(IView view) {
        super(view);
    }

    @Override
    public void preData(Object... objects) {

    }

    @Override
    public void refreshData(Object... objects) {

    }

    @Override
    public void loadMoreData(Object... objects) {

    }

    public void saveCustody(String name, String phone, boolean familySelected, boolean socialSelected, String userId) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || (!familySelected && !socialSelected)) {
            ToastUtils.showShort("请完善信息");
            return;
        }
        Box.getRetrofit(GuardianshipApi.class)
                .addResident(userId, phone)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("添加成功");
                        ActivityUtils.finishActivity();
                    }
                });
    }
}
