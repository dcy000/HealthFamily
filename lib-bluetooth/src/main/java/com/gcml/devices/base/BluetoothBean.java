package com.gcml.devices.base;

import android.os.Parcel;
import android.os.Parcelable;

public class BluetoothBean implements Parcelable {
    private int deviceType;
    private int image;
    private String name;
    private String bluetoothName;
    private String bluetoothAddress;
    private boolean isBand;

    public BluetoothBean() {
    }

    protected BluetoothBean(Parcel in) {
        deviceType = in.readInt();
        image = in.readInt();
        name = in.readString();
        bluetoothName = in.readString();
        bluetoothAddress = in.readString();
        isBand = in.readByte() != 0;
    }

    public static final Creator<BluetoothBean> CREATOR = new Creator<BluetoothBean>() {
        @Override
        public BluetoothBean createFromParcel(Parcel in) {
            return new BluetoothBean(in);
        }

        @Override
        public BluetoothBean[] newArray(int size) {
            return new BluetoothBean[size];
        }
    };

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public boolean isBand() {
        return isBand;
    }

    public void setBand(boolean band) {
        isBand = band;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(deviceType);
        dest.writeInt(image);
        dest.writeString(name);
        dest.writeString(bluetoothName);
        dest.writeString(bluetoothAddress);
        dest.writeByte((byte) (isBand ? 1 : 0));
    }
}
