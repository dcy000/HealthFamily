package com.gcml.module_guardianship.bean;

public class MeasureDataBean {
    private int heartRate;
    private int highPressure;
    private int lowPressure;
    private long bpTime;

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(int highPressure) {
        this.highPressure = highPressure;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(int lowPressure) {
        this.lowPressure = lowPressure;
    }

    public long getBpTime() {
        return bpTime;
    }

    public void setBpTime(long bpTime) {
        this.bpTime = bpTime;
    }
}
