package com.gcml.module_login_and_register.bean;

public class RegisterResultBean {

    /**
     * accountType : string
     * community : string
     * faceGroup : string
     * faceId : string
     * facePhoto : string
     * guardianId : 0
     * guardianName : string
     * mobileNum : string
     * serverId : 0
     * wyyxId : string
     * wyyxPwd : string
     */

    private String accountType;
    private String community;
    private String faceGroup;
    private String faceId;
    private String facePhoto;
    private int guardianId;
    private String guardianName;
    private String mobileNum;
    private int serverId;
    private String wyyxId;
    private String wyyxPwd;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getFaceGroup() {
        return faceGroup;
    }

    public void setFaceGroup(String faceGroup) {
        this.faceGroup = faceGroup;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getFacePhoto() {
        return facePhoto;
    }

    public void setFacePhoto(String facePhoto) {
        this.facePhoto = facePhoto;
    }

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

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
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
}
