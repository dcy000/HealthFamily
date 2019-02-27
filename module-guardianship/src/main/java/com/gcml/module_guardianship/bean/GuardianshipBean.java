package com.gcml.module_guardianship.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gzq on 19-2-6.
 */

public class GuardianshipBean implements Parcelable {


    private int bid;
    private int doid;
    private String eqid;
    private String bname;
    private String sex;
    private String dz;
    private int age;
    private String sfz;
    private String tel;
    private String mh;
    private int state;
    private int height;
    private String bloodType;
    private String eatingHabits;
    private String smoke;
    private String drink;
    private String exerciseHabits;
    private String userPhoto;
    private String xfid;
    private String xfuserid;
    private String allergy;
    private String fetation;
    private String birthday;
    private String hypertensionHand;
    private String hypertensionPrimaryState;
    private String hypertensionLevel;
    private String hypertensionTarget;
    private String wyyxId;
    private String wyyxPwd;
    private int serverId;
    private String watchCode;
    private long watchBindTime;
    private String weight;

    protected GuardianshipBean(Parcel in) {
        bid = in.readInt();
        doid = in.readInt();
        eqid = in.readString();
        bname = in.readString();
        sex = in.readString();
        dz = in.readString();
        age = in.readInt();
        sfz = in.readString();
        tel = in.readString();
        mh = in.readString();
        state = in.readInt();
        height = in.readInt();
        bloodType = in.readString();
        eatingHabits = in.readString();
        smoke = in.readString();
        drink = in.readString();
        exerciseHabits = in.readString();
        userPhoto = in.readString();
        xfid = in.readString();
        xfuserid = in.readString();
        allergy = in.readString();
        fetation = in.readString();
        birthday = in.readString();
        hypertensionHand = in.readString();
        hypertensionPrimaryState = in.readString();
        hypertensionLevel = in.readString();
        hypertensionTarget = in.readString();
        wyyxId = in.readString();
        wyyxPwd = in.readString();
        serverId = in.readInt();
        watchCode = in.readString();
        watchBindTime = in.readLong();
        weight = in.readString();
    }

    public static final Creator<GuardianshipBean> CREATOR = new Creator<GuardianshipBean>() {
        @Override
        public GuardianshipBean createFromParcel(Parcel in) {
            return new GuardianshipBean(in);
        }

        @Override
        public GuardianshipBean[] newArray(int size) {
            return new GuardianshipBean[size];
        }
    };

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getDoid() {
        return doid;
    }

    public void setDoid(int doid) {
        this.doid = doid;
    }

    public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMh() {
        return mh;
    }

    public void setMh(String mh) {
        this.mh = mh;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getEatingHabits() {
        return eatingHabits;
    }

    public void setEatingHabits(String eatingHabits) {
        this.eatingHabits = eatingHabits;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getExerciseHabits() {
        return exerciseHabits;
    }

    public void setExerciseHabits(String exerciseHabits) {
        this.exerciseHabits = exerciseHabits;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getXfid() {
        return xfid;
    }

    public void setXfid(String xfid) {
        this.xfid = xfid;
    }

    public String getXfuserid() {
        return xfuserid;
    }

    public void setXfuserid(String xfuserid) {
        this.xfuserid = xfuserid;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getFetation() {
        return fetation;
    }

    public void setFetation(String fetation) {
        this.fetation = fetation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHypertensionHand() {
        return hypertensionHand;
    }

    public void setHypertensionHand(String hypertensionHand) {
        this.hypertensionHand = hypertensionHand;
    }

    public String getHypertensionPrimaryState() {
        return hypertensionPrimaryState;
    }

    public void setHypertensionPrimaryState(String hypertensionPrimaryState) {
        this.hypertensionPrimaryState = hypertensionPrimaryState;
    }

    public String getHypertensionLevel() {
        return hypertensionLevel;
    }

    public void setHypertensionLevel(String hypertensionLevel) {
        this.hypertensionLevel = hypertensionLevel;
    }

    public String getHypertensionTarget() {
        return hypertensionTarget;
    }

    public void setHypertensionTarget(String hypertensionTarget) {
        this.hypertensionTarget = hypertensionTarget;
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

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getWatchCode() {
        return watchCode;
    }

    public void setWatchCode(String watchCode) {
        this.watchCode = watchCode;
    }

    public long getWatchBindTime() {
        return watchBindTime;
    }

    public void setWatchBindTime(long watchBindTime) {
        this.watchBindTime = watchBindTime;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bid);
        dest.writeInt(doid);
        dest.writeString(eqid);
        dest.writeString(bname);
        dest.writeString(sex);
        dest.writeString(dz);
        dest.writeInt(age);
        dest.writeString(sfz);
        dest.writeString(tel);
        dest.writeString(mh);
        dest.writeInt(state);
        dest.writeInt(height);
        dest.writeString(bloodType);
        dest.writeString(eatingHabits);
        dest.writeString(smoke);
        dest.writeString(drink);
        dest.writeString(exerciseHabits);
        dest.writeString(userPhoto);
        dest.writeString(xfid);
        dest.writeString(xfuserid);
        dest.writeString(allergy);
        dest.writeString(fetation);
        dest.writeString(birthday);
        dest.writeString(hypertensionHand);
        dest.writeString(hypertensionPrimaryState);
        dest.writeString(hypertensionLevel);
        dest.writeString(hypertensionTarget);
        dest.writeString(wyyxId);
        dest.writeString(wyyxPwd);
        dest.writeInt(serverId);
        dest.writeString(watchCode);
        dest.writeLong(watchBindTime);
        dest.writeString(weight);
    }
}
