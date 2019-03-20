package com.gzq.lib_core.bean;

public interface BluetoothParams {
    /**
     * 已经绑定设备的key
     */
    String KEY_DEVICE_HAS_BAND="key_device_has_band";
    /**
     * Intent传递参数key
     */
    String PARAM_MEASURE_TYPE = "measure_type";
    /**
     * 血压
     */
    int TYPE_BLOODPRESSURE = 22;
    /**
     * 血糖
     */
    int TYPE_BLOODSUGAR = 23;
    /**
     * 耳温
     */
    int TYPE_TEMPERATURE = 21;
    /**
     * 血氧
     */
    int TYPE_BLOODOXYGEN = 24;
    /**
     * 体重
     */
    int TYPE_WEIGHT = 25;
    /**
     * 三合一
     */
    int TYPE_THREE = 26;
    /**
     * 心电
     */
    int TYPE_ECG = 27;
}
