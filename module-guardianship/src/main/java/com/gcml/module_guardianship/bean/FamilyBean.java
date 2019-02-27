package com.gcml.module_guardianship.bean;

public class FamilyBean {

    private int guardianId;
    private String guardianName;
    private String guardianPhoto;
    private String guardianType;
    private String mobileNum;

    public int getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(int guardianId) {
        this.guardianId = guardianId;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianPhoto() {
        return guardianPhoto;
    }

    public void setGuardianPhoto(String guardianPhoto) {
        this.guardianPhoto = guardianPhoto;
    }

    public String getGuardianType() {
        return guardianType;
    }

    public void setGuardianType(String guardianType) {
        this.guardianType = guardianType;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }
}
