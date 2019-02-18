package com.gcml.module_login_and_register.api;

import com.gcml.module_login_and_register.bean.GuardianshipBean;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by gzq on 19-2-6.
 */

public interface GuardianshipApi {
    @GET("get")
    Observable<HttpResult<List<GuardianshipBean>>> getGuardianships();
}
