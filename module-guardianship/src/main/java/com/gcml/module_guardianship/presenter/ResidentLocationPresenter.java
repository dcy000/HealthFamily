package com.gcml.module_guardianship.presenter;

import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.bean.FamilyBean;
import com.gcml.module_guardianship.bean.LatLonBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IView;

import java.util.List;

import retrofit2.http.PUT;

public class ResidentLocationPresenter extends BasePresenter {
    public ResidentLocationPresenter(IView view) {
        super(view);
    }

    @Override
    public void preData(Object... objects) {
        Integer userId = (Integer) objects[0];
        Box.getRetrofit(GuardianshipApi.class)
                .getResidentRelationships(userId.toString())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<List<FamilyBean>>() {
                    @Override
                    public void onNext(List<FamilyBean> familyBeans) {
                        mView.loadDataSuccess(familyBeans);
                    }
                });
    }

    @Override
    public void refreshData(Object... objects) {

    }

    @Override
    public void loadMoreData(Object... objects) {

    }

    /**
     * 获取手环的经纬度
     * @param userId
     */
    public void getHandRingLatLon(Integer userId){
        Box.getRetrofit(GuardianshipApi.class)
                .getLatLon(userId.toString())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<LatLonBean>() {
                    @Override
                    public void onNext(LatLonBean latLonBean) {
                        ((IResidentLocationView) mView).getHandRingLatLon(Double.parseDouble(latLonBean.getLat()), Double.parseDouble(latLonBean.getLon()));
                    }
                });
    }
    public void postDealSOSResult(String warningId, String result) {
        UserEntity user = Box.getSessionManager().getUser();
        Box.getRetrofit(GuardianshipApi.class)
                .postSOSDealResult(warningId, user.getUserId(), result)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(mLifecycleOwner))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ((IResidentLocationView) mView).postDealSOSResultSuccess();
                    }
                });
    }
}
