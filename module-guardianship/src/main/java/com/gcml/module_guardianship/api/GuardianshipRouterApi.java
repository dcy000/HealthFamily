package com.gcml.module_guardianship.api;

import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Go;

public interface GuardianshipRouterApi {
    @Go("/guardianship/search/family")
    boolean skipSearchFamilyActivity();

    @Go("/guardianship/qrcode/scan")
    boolean skipQrCodeScanActivity();

    @Go("/guardianship/add/resident/information")
    boolean skipAddResidentInformationActivity(@Extra("watchCode")String watchCode,@Extra("phone")String phone);

    @Go("/guardianship/add/relationship")
    boolean skipAddRelationshipActivity(@Extra("watchInfo") WatchInformationBean watchInformationBean,@Extra("watchCode")String watchCode);

    @Go("/guardianship/resident/detail")
    boolean skipResidentDetailActivity(@Extra("data") GuardianshipBean guardianshipBean);

    @Go("/guardianship/resident/location/detail")
    boolean skipResidentLocationDetailActivity(@Extra("data") GuardianshipBean guardianshipBean);

    @Go("/health/manager/report")
    boolean skipHealthManagerReportActivity();

    @Go("/warning/information/record")
    boolean skipWarningInformationRecordActivity();

    @Go("/person/detail/")
    boolean skipPersonDetailActivity(@Extra("data") GuardianshipBean guardianshipBean);

    @Go("/custody/circle/me")
    boolean skipCustodyCircleActivity(@Extra("data") GuardianshipBean guardianshipBean);

    @Go("/add/custody/activity")
    boolean skipAddCustodyActivity(@Extra("userId")String userId);
}
