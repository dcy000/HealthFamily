package com.gcml.module_login_and_register.api;

import com.sjtu.yifei.annotation.Go;

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
}
