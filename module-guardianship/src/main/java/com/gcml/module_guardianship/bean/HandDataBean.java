package com.gcml.module_guardianship.bean;

public class HandDataBean {

    /**
     * seq : 0.0
     * createdOn : 1.559199482E12
     * createdTime : 2019-05-30 14:58:02
     * wristbandZhHealthId : 4590a6cc-d034-4e18-bbfe-cb8054ec67ef
     * imei : 350183775695484
     * heartRate : 75.0
     * dbp : 81.0
     * sdp : 124.0
     */

    private double seq;
    private double createdOn;
    private String createdTime;
    private String wristbandZhHealthId;
    private String imei;
    private double heartRate;
    private double dbp;
    private double sdp;

    public double getSeq() {
        return seq;
    }

    public void setSeq(double seq) {
        this.seq = seq;
    }

    public double getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(double createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getWristbandZhHealthId() {
        return wristbandZhHealthId;
    }

    public void setWristbandZhHealthId(String wristbandZhHealthId) {
        this.wristbandZhHealthId = wristbandZhHealthId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(double heartRate) {
        this.heartRate = heartRate;
    }

    public double getDbp() {
        return dbp;
    }

    public void setDbp(double dbp) {
        this.dbp = dbp;
    }

    public double getSdp() {
        return sdp;
    }

    public void setSdp(double sdp) {
        this.sdp = sdp;
    }
}
