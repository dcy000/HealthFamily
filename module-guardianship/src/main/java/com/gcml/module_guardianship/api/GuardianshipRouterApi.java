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
    boolean skipAddResidentInformationActivity();

    @Go("/guardianship/add/relationship")
    boolean skipAddRelationshipActivity(@Extra("watchInfo") WatchInformationBean watchInformationBean);

    @Go("/guardianship/resident/detail")
    boolean skipResidentDetailActivity(@Extra("data") GuardianshipBean guardianshipBean);

    @Go("/guardianship/resident/location/detail")
    boolean skipResidentLocationDetailActivity(@Extra("data") GuardianshipBean guardianshipBean);
}
