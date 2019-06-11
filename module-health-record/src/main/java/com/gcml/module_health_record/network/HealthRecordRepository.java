package com.gcml.module_health_record.network;

import com.gcml.module_health_record.bean.BUA;
import com.gcml.module_health_record.bean.BloodOxygenHistory;
import com.gcml.module_health_record.bean.BloodPressureHistory;
import com.gcml.module_health_record.bean.BloodSugarHistory;
import com.gcml.module_health_record.bean.CholesterolHistory;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.bean.HeartRateHistory;
import com.gcml.module_health_record.bean.PulseHistory;
import com.gcml.module_health_record.bean.TemperatureHistory;
import com.gcml.module_health_record.bean.WeightHistory;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.constants.KVConstants;

import java.util.List;

import io.reactivex.Observable;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/20 10:32
 * created by:gzq
 * description:TODO
 */
public class HealthRecordRepository {
    String userId = KVUtils.get(KVConstants.KEY_PATIENTID, 0) + "";
    private HealthRecordServer healthRecordServer = Box.getRetrofit(HealthRecordServer.class);

    public Observable<List<TemperatureHistory>> getTemperatureHistory(String start, String end, String temp) {
        return healthRecordServer.getTemperatureHistory(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<BloodPressureHistory>> getBloodpressureHistory(String start, String end, String temp) {
        return healthRecordServer.getBloodpressureHistory(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<BloodSugarHistory>> getBloodSugarHistory(String start, String end, String temp) {
        return healthRecordServer.getBloodSugarHistory(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<BloodOxygenHistory>> getBloodOxygenHistory(String start, String end, String temp) {
        return healthRecordServer.getBloodOxygenHistory(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<HeartRateHistory>> getHeartRateHistory(String start, String end, String temp) {
        return healthRecordServer.getHeartRateHistory(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }


    public Observable<List<PulseHistory>> getPulseHistory(String start, String end, String temp) {
        return healthRecordServer.getPulseHistory(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<CholesterolHistory>> getCholesterolHistory(String start, String end, String temp) {
        return healthRecordServer.getCholesterolHistory(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<BUA>> getBUAHistory(String start, String end, String temp) {
        return healthRecordServer.getBUAHistory(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<ECGHistory>> getECGHistory(String start, String end, String temp) {
        return healthRecordServer.getECGHistory(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }

    public Observable<List<WeightHistory>> getWeight(String start, String end, String temp) {
        return healthRecordServer.getWeight(userId, start, end, temp).compose(RxUtils.httpResponseTransformer());
    }
}
