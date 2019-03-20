package com.gcml.module_health_measure.api;

import com.gcml.devices.base.BluetoothBean;
import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Go;

public interface HealthMeasureRouterApi {
    @Go("/health/measure/measure/activity")
    boolean skipMeasureActivity(@Extra("data") BluetoothBean brandMenu);
}
