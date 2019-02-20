package com.gcml.module_guardianship.api;

import com.sjtu.yifei.annotation.Go;

public interface GuardianshipRouterApi {
    @Go("/guardianship/search/family")
    boolean skipSearchFamilyActivity();

    @Go("/guardianship/qrcode/scan")
    boolean skipQrCodeScanActivity();

    @Go("/guardianship/add/resident/information")
    boolean skipAddResidentInformationActivity();

    @Go("/guardianship/add/relationship")
    boolean skipAddRelationshipActivity();

    @Go("/guardianship/resident/detail")
    boolean skipResidentDetailActivity();
}
