package com.gcml.module_health_manager.api;

import com.gcml.module_health_manager.bean.FamilyDoctorServiceBean;
import com.gzq.lib_core.bean.BluetoothParams;
import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Go;

import java.util.ArrayList;

public interface HealthManagerRouterApi {
    @Go("/healthmanager/health/programme/report")
    boolean skipHealthProgrammeReportActivity();

    @Go("/healthmanager/health/task")
    boolean skipHealthTaskActivity();

    @Go("/healthmanager/risk/assessment/report")
    boolean skipRiskAssessmentReportActivity();

    @Go("/healthmanager/health/measure")
    boolean skipHealthMeasureActivity();

    @Go("/healthmanager/family/doctor/service")
    boolean skipFamilyDoctorServiceActivity();

    @Go("/healthmanager/health/file")
    boolean skipHealthFileActivity();

    @Go("/healthmanager/bloodpressure/followup")
    boolean skipBloodpressureFollowupActivity();

    @Go("/healthmanager/bloodsugar/followup")
    boolean skipBloodsugarFollowupActivity();

    @Go("/healthmanager/zhongyi/tizhi")
    boolean skipZhongyiTizhiActivity();

    @Go("/healthmanager/checkup/report")
    boolean skipHealthCheckupReportActivity();

    @Go("/healthmanager/health/video")
    boolean skipHealthVideoActivity();

    @Go("/healthmanager/health/measure/detail")
    boolean skipHealthMeasureDetailActivity(
            @Extra("posi") String position,
            @Extra("data") ArrayList<FamilyDoctorServiceBean> familyDoctorServiceBeans);

    @Go("/health/measure/choose/device")
    boolean skipChooseDeviceActivity(
            @Extra(BluetoothParams.PARAM_MEASURE_TYPE) int measureType
    );
}
