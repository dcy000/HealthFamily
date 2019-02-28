package com.gcml.module_health_manager.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FamilyDoctorServiceBean implements Parcelable {
    private int icon;
    private String title;

    public FamilyDoctorServiceBean() {
    }

    public FamilyDoctorServiceBean(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    protected FamilyDoctorServiceBean(Parcel in) {
        icon = in.readInt();
        title = in.readString();
    }

    public static final Creator<FamilyDoctorServiceBean> CREATOR = new Creator<FamilyDoctorServiceBean>() {
        @Override
        public FamilyDoctorServiceBean createFromParcel(Parcel in) {
            return new FamilyDoctorServiceBean(in);
        }

        @Override
        public FamilyDoctorServiceBean[] newArray(int size) {
            return new FamilyDoctorServiceBean[size];
        }
    };

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(title);
    }
}
