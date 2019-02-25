package com.gcml.module_guardianship.api;

import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.bean.HealthDataMenu;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by gzq on 19-2-6.
 */

public interface GuardianshipApi {
    //mock数据
    @GET("healthdata")
    Observable<HttpResult<List<HealthDataMenu>>> getHealthDatas();

    /**
     * 获取居民列表
     * @param userId
     * @return
     */
    @GET("ZZB/api/guardian/{guardianId}/users/")
    Observable<HttpResult<List<GuardianshipBean>>> getGuardianships(
            @Path("guardianId") String userId
    );
}
