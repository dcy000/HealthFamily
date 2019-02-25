package com.gcml.module_guardianship.bean;

import com.gzq.lib_resource.bean.ResidentBean;
import com.gzq.lib_resource.bean.ResidentRecordBean;

/**
 * Created by gzq on 19-2-6.
 */

public class GuardianshipBean {
    private ResidentBean residentBean;

    private ResidentRecordBean residentRecordBean;

    public ResidentBean getResidentBean() {
        return residentBean;
    }

    public void setResidentBean(ResidentBean residentBean) {
        this.residentBean = residentBean;
    }

    public ResidentRecordBean getResidentRecordBean() {
        return residentRecordBean;
    }

    public void setResidentRecordBean(ResidentRecordBean residentRecordBean) {
        this.residentRecordBean = residentRecordBean;
    }
}
