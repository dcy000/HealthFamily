package com.gcml.module_guardianship.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class WatchInformationBean implements Parcelable {

    /**
     * bpCorrectionHigh : 0
     * bpCorrectionLow : 0
     * bpThresholdHigh : 0
     * bpThresholdLow : 0
     * deviceMobileNo : string
     * fallingAlarm : string
     * height : 0
     * hrmThresholdHigh : 0
     * hrmThresholdLow : 0
     * imei : string
     * name : string
     * ownerBirthday : string
     * ownerBloodType : string
     * ownerGender : string
     * restricted : string
     * userid : 0
     * weight : 0
     * wristbandZhInfoId : string
     */

    private int bpCorrectionHigh;
    private int bpCorrectionLow;
    private int bpThresholdHigh;
    private int bpThresholdLow;
    private String deviceMobileNo;
    private String fallingAlarm;
    private int height;
    private int hrmThresholdHigh;
    private int hrmThresholdLow;
    private String imei;
    private String name;
    private String ownerBirthday;
    private String ownerBloodType;
    private String ownerGender;
    private String restricted;
    private int userid;
    private int weight;
    private String wristbandZhInfoId;

    protected WatchInformationBean(Parcel in) {
        bpCorrectionHigh = in.readInt();
        bpCorrectionLow = in.readInt();
        bpThresholdHigh = in.readInt();
        bpThresholdLow = in.readInt();
        deviceMobileNo = in.readString();
        fallingAlarm = in.readString();
        height = in.readInt();
        hrmThresholdHigh = in.readInt();
        hrmThresholdLow = in.readInt();
        imei = in.readString();
        name = in.readString();
        ownerBirthday = in.readString();
        ownerBloodType = in.readString();
        ownerGender = in.readString();
        restricted = in.readString();
        userid = in.readInt();
        weight = in.readInt();
        wristbandZhInfoId = in.readString();
    }

    public static final Creator<WatchInformationBean> CREATOR = new Creator<WatchInformationBean>() {
        @Override
        public WatchInformationBean createFromParcel(Parcel in) {
            return new WatchInformationBean(in);
        }

        @Override
        public WatchInformationBean[] newArray(int size) {
            return new WatchInformationBean[size];
        }
    };

    public int getBpCorrectionHigh() {
        return bpCorrectionHigh;
    }

    public void setBpCorrectionHigh(int bpCorrectionHigh) {
        this.bpCorrectionHigh = bpCorrectionHigh;
    }

    public int getBpCorrectionLow() {
        return bpCorrectionLow;
    }

    public void setBpCorrectionLow(int bpCorrectionLow) {
        this.bpCorrectionLow = bpCorrectionLow;
    }

    public int getBpThresholdHigh() {
        return bpThresholdHigh;
    }

    public void setBpThresholdHigh(int bpThresholdHigh) {
        this.bpThresholdHigh = bpThresholdHigh;
    }

    public int getBpThresholdLow() {
        return bpThresholdLow;
    }

    public void setBpThresholdLow(int bpThresholdLow) {
        this.bpThresholdLow = bpThresholdLow;
    }

    public String getDeviceMobileNo() {
        return deviceMobileNo;
    }

    public void setDeviceMobileNo(String deviceMobileNo) {
        this.deviceMobileNo = deviceMobileNo;
    }

    public String getFallingAlarm() {
        return fallingAlarm;
    }

    public void setFallingAlarm(String fallingAlarm) {
        this.fallingAlarm = fallingAlarm;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHrmThresholdHigh() {
        return hrmThresholdHigh;
    }

    public void setHrmThresholdHigh(int hrmThresholdHigh) {
        this.hrmThresholdHigh = hrmThresholdHigh;
    }

    public int getHrmThresholdLow() {
        return hrmThresholdLow;
    }

    public void setHrmThresholdLow(int hrmThresholdLow) {
        this.hrmThresholdLow = hrmThresholdLow;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerBirthday() {
        return ownerBirthday;
    }

    public void setOwnerBirthday(String ownerBirthday) {
        this.ownerBirthday = ownerBirthday;
    }

    public String getOwnerBloodType() {
        return ownerBloodType;
    }

    public void setOwnerBloodType(String ownerBloodType) {
        this.ownerBloodType = ownerBloodType;
    }

    public String getOwnerGender() {
        return ownerGender;
    }

    public void setOwnerGender(String ownerGender) {
        this.ownerGender = ownerGender;
    }

    public String getRestricted() {
        return restricted;
    }

    public void setRestricted(String restricted) {
        this.restricted = restricted;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getWristbandZhInfoId() {
        return wristbandZhInfoId;
    }

    public void setWristbandZhInfoId(String wristbandZhInfoId) {
        this.wristbandZhInfoId = wristbandZhInfoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bpCorrectionHigh);
        dest.writeInt(bpCorrectionLow);
        dest.writeInt(bpThresholdHigh);
        dest.writeInt(bpThresholdLow);
        dest.writeString(deviceMobileNo);
        dest.writeString(fallingAlarm);
        dest.writeInt(height);
        dest.writeInt(hrmThresholdHigh);
        dest.writeInt(hrmThresholdLow);
        dest.writeString(imei);
        dest.writeString(name);
        dest.writeString(ownerBirthday);
        dest.writeString(ownerBloodType);
        dest.writeString(ownerGender);
        dest.writeString(restricted);
        dest.writeInt(userid);
        dest.writeInt(weight);
        dest.writeString(wristbandZhInfoId);
    }
}
