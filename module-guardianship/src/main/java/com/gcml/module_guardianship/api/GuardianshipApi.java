package com.gcml.module_guardianship.api;

import com.gcml.module_guardianship.bean.FamilyBean;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.bean.HandDataBean;
import com.gcml.module_guardianship.bean.HandRingHealthDataBena;
import com.gcml.module_guardianship.bean.HealthDataMenu;
import com.gcml.module_guardianship.bean.LatLonBean;
import com.gcml.module_guardianship.bean.MeasureDataBean;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_resource.bean.ResidentBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by gzq on 19-2-6.
 */

public interface GuardianshipApi {
    //mock数据
    @GET("healthdata")
    Observable<HttpResult<List<HealthDataMenu>>> getHealthDatas();

    /**
     * 获取居民列表
     *
     * @param userId
     * @return
     */
    @GET("ZZB/api/guardian/{guardianId}/users/")
    Observable<HttpResult<List<GuardianshipBean>>> getGuardianships(
            @Path("guardianId") String userId
    );

    /**
     * 根据手环码查用户信息
     *
     * @param watchCode
     * @return
     */
    @GET("ZZB/api/user/watch/")
    Observable<HttpResult<WatchInformationBean>> getWatchInfo(
            @Query("watchCode") String watchCode
    );

    @POST("ZZB/api/user/watch/")
    Observable<HttpResult<Object>> bindPatient(
            @Query("watchCode") String watchCode,
            @Query("userId") String userId
    );

    /**
     * 添加监护人
     *
     * @return
     */
    @POST("ZZB/api/guardian/user/{userId}/")
    Observable<HttpResult<Object>> addResident(
            @Path("userId") String userId,
            @Query("mobileNum") String mobileNum
    );

    /**
     * 获取居民的监护圈子
     *
     * @return
     */
    @GET("ZZB/api/guardian/user/{userId}/guardians/")
    Observable<HttpResult<List<FamilyBean>>> getResidentRelationships(
            @Path("userId") String userId
    );

    /**
     * 紧急呼叫的处理结果
     *
     * @return
     */
    @POST("ZZB/api/guardian/warning/{warningId}/")
    Observable<HttpResult<Object>> postSOSDealResult(
            @Path("warningId") String warningId,
            @Query("guardianId") String guardianId,
            @Query("content") String content
    );

    /**
     * 获取手环的经纬度信息
     *
     * @param userId
     * @return
     */
    @GET("ZZB/api/user/watch/address/")
    Observable<HttpResult<LatLonBean>> getLatLon(
            @Query("userId") String userId
    );

    /**
     * 获取手环上的测量数据
     *
     * @param userId
     * @return
     */
    @GET("ZZB/api/user/watch/detection/")
    Observable<HttpResult<HandRingHealthDataBena>> getHandRingHealthData(
            @Query("userId") String userId
    );

    /**
     * 通过身份证号码拿用户信息
     *
     * @param idCard
     * @return
     */
    @GET("ZZB/api/user/info/idCard/")
    Observable<HttpResult<ResidentBean>> getUserInfoByIdcard(
            @Query("idCard") String idCard
    );

    /**
     * 激活手环
     *
     * @param watchCode
     * @param phone
     * @return
     */
    @POST("ZZB/api/wristband/ZhInfo/setWristbandZh")
    Observable<HttpResult<Object>> activateHandRing(
            @Query("imei") String watchCode,
            @Query("deviceMobileNo") String phone
    );

    /**
     * @param imei 手环码
     * @param type 类型：1->血压；2->心率
     * @return
     */
    @POST("ZZB/api/wristband/Zh/startMeasure")
    Observable<HttpResult<Object>> measureByHandRing(
            @Query("imei") String imei,
            @Query("type") String type
    );

    @GET("ZZB/api/user/watch/detection/")
    Observable<HttpResult<MeasureDataBean>> getMeasureData(
            @Query("userId") String userId
    );

    @POST("ZZB/api/wristband/Zh/selHealth")
    Observable<HttpResult<List<HandDataBean>>> getHandDatas(
            @Query("imei") String imei,
            @Query("page") String page,
            @Query("limit") String limit
    );
}
