package com.gcml.healthfamily;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.app.AppStore;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.mvp.base.IView;
import com.gzq.lib_resource.widget.BottomBar;
import com.gzq.lib_resource.widget.BottomBarTab;
import com.sjtu.yifei.route.Routerfit;

import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import timber.log.Timber;

/**
 * Created by gzq on 19-2-3.
 */

public class MainActivity extends StateBaseActivity {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomBar mBottomBar;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
//        MainGuardianshipFragment firstFragment = findFragment(MainGuardianshipFragment.class);
//        if (firstFragment == null) {
//            mFragments[FIRST] = Routerfit.register(AppRouterApi.class).getFirstFragment();
//            mFragments[SECOND] = Routerfit.register(AppRouterApi.class).getSecondFragment();
//            mFragments[THIRD] = Routerfit.register(AppRouterApi.class).getThirdFragment();
//            mFragments[FOURTH] = Routerfit.register(AppRouterApi.class).getFourthFragment();
//
//            loadMultipleRootFragment(R.id.fl_tab_container, 0,
//                    mFragments[FIRST],
//                    mFragments[SECOND],
//                    mFragments[THIRD],
//                    mFragments[FOURTH]);
//        } else {
//            mFragments[FIRST] = firstFragment;
//            mFragments[SECOND] = findFragment(MainSOSDealFragment.class);
//            mFragments[THIRD] = findFragment(MainHealthManagerFragment.class);
//            mFragments[FOURTH] = findFragment(MainMineFragment.class);
//        }

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

    @Override
    public void initView() {
        //设置状态栏的颜色
        StatusBarCompat.setStatusBarColor(this, Box.getColor(R.color.white));
        //加载页面成功
        showSuccess();
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
