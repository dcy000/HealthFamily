package com.gcml.module_guardianship.bean;

public class LatLonBean {

    /**
     * seq : 0
     * createdOn : 1551236674904
     * createdTime : 2019-02-27 11:04:34
     * wristbandZhLocationId : b1cc4b572d56491a9083aeb810935c64
     * imei : 350183475190643
     * type : amap
     * lat : 30.1932541
     * lon : 120.2657609
     */

    private int seq;
    private long createdOn;
    private String createdTime;
    private String wristbandZhLocationId;
    private String imei;
    private String type;
    private String lat;
    private String lon;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getWristbandZhLocationId() {
        return wristbandZhLocationId;
    }

    public void setWristbandZhLocationId(String wristbandZhLocationId) {
        this.wristbandZhLocationId = wristbandZhLocationId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
