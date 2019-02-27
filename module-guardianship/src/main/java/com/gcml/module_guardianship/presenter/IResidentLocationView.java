package com.gcml.module_guardianship.presenter;

import com.gzq.lib_resource.mvp.base.IView;

public interface IResidentLocationView extends IView {
    void postDealSOSResultSuccess();
    void getHandRingLatLon(double lat,double lon);
}
