package com.gcml.healthfamily;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.app.AppStore;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.mvp.base.IView;
import com.gzq.lib_resource.widget.BottomBar;
import com.gzq.lib_resource.widget.BottomBarTab;
import com.jaeger.library.StatusBarUtil;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import timber.log.Timber;

/**
 * Created by gzq on 19-2-3.
 */
@Route(path = "/main/mainactivity")
public class MainActivity extends StateBaseActivity {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomBar mBottomBar;
    private boolean isInit = false;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        requestPermissionss();
    }

    private void requestPermissionss() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.requestEach(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .as(RxUtils.<Permission>autoDisposeConverter(this))
                .subscribe(new CommonObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            initFragments();
                        } else {
                            ToastUtils.showLong("请同意相关权限后，再次打开应用");
                        }
                    }
                });
    }

    private void initFragments() {
        if (!isInit) {
            isInit = true;
            mFragments[FIRST] = Routerfit.register(AppRouterApi.class).getFirstFragment();
            mFragments[SECOND] = Routerfit.register(AppRouterApi.class).getSecondFragment();
            mFragments[THIRD] = Routerfit.register(AppRouterApi.class).getThirdFragment();
            mFragments[FOURTH] = Routerfit.register(AppRouterApi.class).getFourthFragment();

            loadMultipleRootFragment(R.id.fl_tab_container, 0,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        }
    }

    @Override
    public void initView() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
        //设置状态栏的颜色
        StatusBarCompat.setStatusBarColor(this, Box.getColor(R.color.white));
        //加载页面成功
        showSuccess();
        mToolbar.setVisibility(View.GONE);
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        final BottomBarTab guardianship = new BottomBarTab(this, R.drawable.ic_first, "监护");
        final BottomBarTab sosDeal = new BottomBarTab(this, R.drawable.ic_second, "警报处理");
        final BottomBarTab healthManager = new BottomBarTab(this, R.drawable.ic_third, "健康管理");
        final BottomBarTab mine = new BottomBarTab(this, R.drawable.ic_fourth, "我的");

        mBottomBar
                .addItem(guardianship)
                .addItem(sosDeal)
                .addItem(healthManager)
                .addItem(mine);


        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                if (prePosition == 0) {
                    StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
                } else {
//                    StatusBarCompat.resetActionBarContainerTopMargin(getWindow(), android.support.v7.appcompat.R.id.action_bar_container);
                    //设置状态栏的颜色
                    StatusBarCompat.setStatusBarColor(MainActivity.this, Box.getColor(R.color.white));
                }
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                final SupportFragment currentFragment = mFragments[position];

                Timber.w(currentFragment + "");

            }
        });

        AppStore.guardianship.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                guardianship.setUnreadCount(integer);
            }
        });
        AppStore.healthManager.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                healthManager.setUnreadCount(integer);
            }
        });

        AppStore.sosDeal.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                sosDeal.setUnreadCount(integer);
            }
        });
        AppStore.mine.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                mine.setUnreadCount(integer);
            }
        });

    }

    @Override
    public IPresenter obtainPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultVerticalAnimator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStore.guardianship.removeObservers(this);
        AppStore.healthManager.removeObservers(this);
        AppStore.sosDeal.removeObservers(this);
        AppStore.mine.removeObservers(this);
    }

    static class MainPresenter extends BasePresenter {

        public MainPresenter(IView view) {
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
