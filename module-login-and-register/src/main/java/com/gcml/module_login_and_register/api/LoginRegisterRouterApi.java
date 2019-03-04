package com.gcml.module_login_and_register.api;

import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Go;
import com.sjtu.yifei.route.ActivityCallback;

public interface LoginRegisterRouterApi {
    /**
     * 跳转到RegisterMessageCodeActivity
     *
     * @return
     */
    @Go("/register/messagecode")
    boolean skipSocialRegisterMessageCodeActivity();

    @Go("/main/mainactivity")
    boolean skipMainActivity();

    @Go("/register/inputpassword")
    boolean skipSocialInputPasswordActivity();

    @Go("/register/choose/role")
    boolean skipChooseRegisterRoleActivity();

    @Go("/register/inputName")
    boolean skipSocialInputNameActivity();

    @Go("/register/FamilyMessageCode")
    boolean skipFamilyRegisteMessageCodeActivity();

    @Go("/register/FamilyInputPassword")
    boolean skipFamilyInputPasswordActivity();

    @Go("/register/FamilyInputName")
    boolean skipFamilyInputNameActivity();

    @Go("/login/forgetPassword")
    boolean skipForgetPasswordActivity();

    @Go("/setup/newPassword")
    boolean skipSetupNewPasswordActivity();


    /**
     * 1. 人脸识别登录 （verify = false）
     * 2. 人脸认证登录 （verify = true）
     */
    @Go("/face/auth/signin")
    boolean skipFaceBdSignInActivity(@Extra("verify") boolean verify, @Extra("faceId") String faceId, @Extra ActivityCallback callback);

}
