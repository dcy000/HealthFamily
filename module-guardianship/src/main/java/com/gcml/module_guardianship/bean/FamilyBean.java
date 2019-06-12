package com.gcml.module_guardianship.bean;

public class FamilyBean {

    private int guardianId;
    private String guardianName;
    private String guardianPhoto;
    private String guardianType;
    private String mobileNum;
    private String wyyxId;
    private String wyyxPwd;
    private int accountCate;
    private boolean isSelf;
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

    public String getWyyxId() {
        return wyyxId;
    }

    public void setWyyxId(String wyyxId) {
        this.wyyxId = wyyxId;
    }

    public String getWyyxPwd() {
        return wyyxPwd;
    }

    public void setWyyxPwd(String wyyxPwd) {
        this.wyyxPwd = wyyxPwd;
    }

    public int getAccountCate() {
        return accountCate;
    }

    public void setAccountCate(int accountCate) {
        this.accountCate = accountCate;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}
