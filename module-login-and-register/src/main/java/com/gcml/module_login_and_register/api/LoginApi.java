package com.gcml.module_login_and_register.api;

import com.gcml.module_login_and_register.bean.RegisterPutBean;
import com.gcml.module_login_and_register.bean.RegisterResultBean;
import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_resource.bean.UserEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {
    /**
     * 监护人登录
     *
     * @param userName
     * @param password
     * @return
     */
    @POST("ZZB/api/guardian/login/")
    Observable<HttpResult<UserEntity>> loginWithGuardianship(
            @Query("userName") String userName,
            @Query("password") String password
    );

    /**
     * 注册获取验证码
     *
     * @param mobile
     * @param isCheck
     * @return
     */
    @GET("ZZB/api/guardian/regiseter/valid/")
    Observable<HttpResult<String>> getMsgCode(
            @Query("mobile") String mobile,
            @Query("check") boolean isCheck
    );

    /**
     * 注册
     *
     * @return
     */
    @POST("ZZB/api/guardian/regiseter/")
    Observable<HttpResult<Object>> register(
            @Body RegisterPutBean registerPutBean
    );
}
