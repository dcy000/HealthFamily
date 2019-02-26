package com.ml.module_shouhuan.api;

import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_resource.bean.MsgBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShouhuanApi {
    /**
     * 获取消息
     * @param userId
     * @param dealStatus
     * @return
     */
    @GET("ZZB/api/guardian/warning/")
    Observable<HttpResult<List<MsgBean>>> getMsg(
            @Query("guardianId") String userId,
            @Query("dealStatus") String dealStatus
    );
}
