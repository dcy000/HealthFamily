package com.gzq.lib_resource;

import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_resource.bean.UserEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommonApi {
    @GET("ZZB/br/selOneUserEverything")
    Observable<HttpResult<UserEntity>> getProfile(
            @Query("bid") String userId
    );
}
