package com.gcml.module_login_and_register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gcml.module_login_and_register.R;
import com.gcml.module_login_and_register.api.LoginRegisterRouterApi;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.mvp.base.IView;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/register/choose/role")
public class ChooseRegisterRoleActivity extends StateBaseActivity implements View.OnClickListener {
    private ImageView mIvSocialWork;
    private ImageView mIvFamilyMember;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_register_choose_role;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mIvSocialWork = (ImageView) findViewById(R.id.iv_social_work);
        mIvSocialWork.setOnClickListener(this);
        mIvFamilyMember = (ImageView) findViewById(R.id.iv_family_member);
        mIvFamilyMember.setOnClickListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new ChooseRegisterRolePresenter(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_social_work) {
            //将选择的类型存储在MMKV中
            KVUtils.put(KVConstants.KEY_ACCOUNT_TYPE, "0");
            Routerfit.register(LoginRegisterRouterApi.class).skipSocialRegisterMessageCodeActivity();
        } else if (i == R.id.iv_family_member) {
            //将选择的类型存储在MMKV中
            KVUtils.put(KVConstants.KEY_ACCOUNT_TYPE, "1");
            Routerfit.register(LoginRegisterRouterApi.class).skipFamilyRegisteMessageCodeActivity();
        } else {
        }
    }

    static class ChooseRegisterRolePresenter extends BasePresenter {

        public ChooseRegisterRolePresenter(IView view) {
            super(view);
        }

        @Override
        public void preData(Object... objects) {

        }

        @Override
        public void refreshData(Object... objects) {

        }

        @Override
        public void loadMoreData(Object... objects) {

        }
    }
}
