package com.gzq.lib_resource.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserEntity implements Parcelable {

    private String accountType;
    private String community;
    private String faceGroup;
    private String faceId;
    @SerializedName("facePhoto")
    private String headPath;
    @SerializedName("guardianId")
    private String userId;
    @SerializedName("guardianName")
    private String userName;
    @SerializedName("mobileNum")
    private String phone;
    private String serverId;
    private String wyyxId;
    private String wyyxPwd;

    public UserEntity() {
    }

    protected UserEntity(Parcel in) {
        accountType = in.readString();
        community = in.readString();
        faceGroup = in.readString();
        faceId = in.readString();
        headPath = in.readString();
        userId = in.readString();
        userName = in.readString();
        phone = in.readString();
        serverId = in.readString();
        wyyxId = in.readString();
        wyyxPwd = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

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

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountType);
        dest.writeString(community);
        dest.writeString(faceGroup);
        dest.writeString(faceId);
        dest.writeString(headPath);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(phone);
        dest.writeString(serverId);
        dest.writeString(wyyxId);
        dest.writeString(wyyxPwd);
    }
}
