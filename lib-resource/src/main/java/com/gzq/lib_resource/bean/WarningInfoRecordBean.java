package com.gzq.lib_resource.bean;

public class WarningInfoRecordBean {
    private String warningTime;
    private String warningType;
    private String warningDealPerson;

    public WarningInfoRecordBean() {
    }

    public WarningInfoRecordBean(String warningTime, String warningType, String warningDealPerson) {
        this.warningTime = warningTime;
        this.warningType = warningType;
        this.warningDealPerson = warningDealPerson;
    }

    public String getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(String warningTime) {
        this.warningTime = warningTime;
    }

    public String getWarningType() {
        return warningType;
    }

    public void setWarningType(String warningType) {
        this.warningType = warningType;
    }

    public String getWarningDealPerson() {
        return warningDealPerson;
    }

    public void setWarningDealPerson(String warningDealPerson) {
        this.warningDealPerson = warningDealPerson;
    }
}
