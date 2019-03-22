package com.gcml.module_guardianship.presenter;

import android.text.TextUtils;

import com.gcml.module_guardianship.AddCustodyActivity;
import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.base.BasePresenter;

public class AddCustodyPresenter extends BasePresenter<AddCustodyActivity> {

    public AddCustodyPresenter(AddCustodyActivity view) {
        super(view);
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
