package com.gcml.module_health_record.bean;

/**
 * Created by gzq on 2017/12/2.
 */

public class BloodPressureHistory extends BaseBean{
    public float low_pressure;
    public float high_pressure;
    public BloodPressureHistory(float low_pressure, float high_pressure, long time) {
        this.low_pressure = low_pressure;
        this.high_pressure = high_pressure;
        this.time = time;
    }
}
