package com.gcml.module_mine;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_mine.bean.MyInforMenuBean;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.PermissionUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.api.CommonApi;
import com.gzq.lib_resource.api.CommonRouterApi;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;
import com.tbruyelle.rxpermissions2.Permission;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/mine/my/information")
public class MyInformationActivity extends StateBaseActivity {
    private RecyclerView mRvMenu;
    private ArrayList<MyInforMenuBean> myInforMenuBeans = new ArrayList<>();
    private BaseQuickAdapter<MyInforMenuBean, BaseViewHolder> adapter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_my_information;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        UserEntity user = Box.getSessionManager().getUser();
        if (user.getAccountType().equals("0")) {
            myInforMenuBeans.add(new MyInforMenuBean("头像", user.getHeadPath(), ""));
            myInforMenuBeans.add(new MyInforMenuBean("姓名", null, user.getUserName()));
            myInforMenuBeans.add(new MyInforMenuBean("手机号", null, user.getPhone()));
            myInforMenuBeans.add(new MyInforMenuBean("身份", null, user.getAccountType().equals("0") ? "社工" : "家属"));
            myInforMenuBeans.add(new MyInforMenuBean("单位", null, user.getCommunity()));
        } else {
            myInforMenuBeans.add(new MyInforMenuBean("头像", user.getHeadPath(), ""));
            myInforMenuBeans.add(new MyInforMenuBean("姓名", null, user.getUserName()));
            myInforMenuBeans.add(new MyInforMenuBean("手机号", null, user.getPhone()));
            myInforMenuBeans.add(new MyInforMenuBean("身份", null, user.getAccountType().equals("0") ? "社工" : "家属"));
        }

    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("个人信息");
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        initRV();
    }

    private void initRV() {
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        mRvMenu.addItemDecoration(new LinearLayoutDividerItemDecoration(32, 1, Color.parseColor("#EEEEEE")));
        mRvMenu.setAdapter(adapter = new BaseQuickAdapter<MyInforMenuBean, BaseViewHolder>(R.layout.item_layout_my_infor_menu, myInforMenuBeans) {
            @Override
            protected void convert(BaseViewHolder helper, MyInforMenuBean item) {
                helper.setText(R.id.tv_title, item.getTitle());
                if (item.getImg() != null) {
                    Glide.with(Box.getApp())
                            .load(item.getImg())
                            .into((CircleImageView) helper.getView(R.id.civ_head));
                }
                helper.setText(R.id.tv_content, item.getContent());
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    PermissionUtils.requestEach(MyInformationActivity.this,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA)
                            .subscribe(new CommonObserver<Permission>() {
                                @Override
                                public void onNext(Permission permission) {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    String message = e.getMessage();
                                    if (!TextUtils.isEmpty(message)) {
                                        ToastUtils.showLong(message);
                                    }
                                }

                                @Override
                                public void onComplete() {
                                    super.onComplete();
                                    registerFace(adapter);
                                }
                            });
                }
            }
        });
    }

    private void registerFace(BaseQuickAdapter adapter) {
        Routerfit.register(CommonRouterApi.class).skipFaceBdSignUpActivity(new ActivityCallback() {
            @Override
            public void onActivityResult(int result, Object data) {
                if (data.toString().equals("success")) {
                    UserEntity user = Box.getSessionManager().getUser();
                    Box.getRetrofit(CommonApi.class)
                            .getProfile(user.getUserId())
                            .compose(RxUtils.httpResponseTransformer())
                            .as(RxUtils.autoDisposeConverter(MyInformationActivity.this))
                            .subscribe(new CommonObserver<UserEntity>() {
                                @Override
                                public void onNext(UserEntity userEntity) {
                                    Box.getSessionManager().setUser(userEntity);
                                    myInforMenuBeans.get(0).setImg(userEntity.getHeadPath());
                                    adapter.notifyDataSetChanged();
                                    ToastUtils.showShort("修改成功");
                                }
                            });
                } else {
                    ToastUtils.showShort(data.toString());
                }
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
