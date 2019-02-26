package com.gzq.lib_resource.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MsgBean implements Parcelable {

    private String warningId;
    private int userId;
    private String userName;
    private String userPhoto;
    private int dealGuardianId;
    private String equipmentId;
    private String equipmentType;
    private String warningType;
    private String warningAddress;
    private String warningContent;
    private long warningTime;
    private String dealStatus;
    private long dealTime;
    private String dealContent;
    private String feedback;

    protected MsgBean(Parcel in) {
        warningId = in.readString();
        userId = in.readInt();
        userName = in.readString();
        userPhoto = in.readString();
        dealGuardianId = in.readInt();
        equipmentId = in.readString();
        equipmentType = in.readString();
        warningType = in.readString();
        warningAddress = in.readString();
        warningContent = in.readString();
        warningTime = in.readLong();
        dealStatus = in.readString();
        dealTime = in.readLong();
        dealContent = in.readString();
        feedback = in.readString();
    }

    public static final Creator<MsgBean> CREATOR = new Creator<MsgBean>() {
        @Override
        public MsgBean createFromParcel(Parcel in) {
            return new MsgBean(in);
        }

        @Override
        public MsgBean[] newArray(int size) {
            return new MsgBean[size];
        }
    };

    public String getWarningId() {
        return warningId;
    }

    public void setWarningId(String warningId) {
        this.warningId = warningId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getDealGuardianId() {
        return dealGuardianId;
    }

    public void setDealGuardianId(int dealGuardianId) {
        this.dealGuardianId = dealGuardianId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getWarningType() {
        return warningType;
    }

    public void setWarningType(String warningType) {
        this.warningType = warningType;
    }

    public String getWarningAddress() {
        return warningAddress;
    }

    public void setWarningAddress(String warningAddress) {
        this.warningAddress = warningAddress;
    }

    public String getWarningContent() {
        return warningContent;
    }

    public void setWarningContent(String warningContent) {
        this.warningContent = warningContent;
    }

    public long getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(long warningTime) {
        this.warningTime = warningTime;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealContent() {
        return dealContent;
    }

    public void setDealContent(String dealContent) {
        this.dealContent = dealContent;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(warningId);
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(userPhoto);
        dest.writeInt(dealGuardianId);
        dest.writeString(equipmentId);
        dest.writeString(equipmentType);
        dest.writeString(warningType);
        dest.writeString(warningAddress);
        dest.writeString(warningContent);
        dest.writeLong(warningTime);
        dest.writeString(dealStatus);
        dest.writeLong(dealTime);
        dest.writeString(dealContent);
        dest.writeString(feedback);
    }
}
