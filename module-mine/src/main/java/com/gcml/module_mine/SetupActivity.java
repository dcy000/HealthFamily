package com.gcml.module_mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_mine.api.MineRouterApi;
import com.gcml.module_mine.bean.SetupBean;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.AppUtils;
import com.gzq.lib_resource.utils.CacheUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

@Route(path = "/mine/setup")
public class SetupActivity extends StateBaseActivity {
    private ArrayList<SetupBean> menu = new ArrayList<SetupBean>() {
        {
            try {
                add(new SetupBean("清除缓存", CacheUtils.getTotalCacheSize()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            add(new SetupBean("意见反馈", "尽情吐槽我们吧"));
            add(new SetupBean("关于我们", "版本" + AppUtils.getAppInfo().getVersionName()));
        }
    };
    private RecyclerView mRvMenu;
    private BaseQuickAdapter<SetupBean, BaseViewHolder> adapter;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_setup;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {
        showSuccess();
        mTvTitle.setText("设置");
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        initRv();
    }

    private void initRv() {
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));

        mRvMenu.setAdapter(adapter = new BaseQuickAdapter<SetupBean, BaseViewHolder>(R.layout.item_setup_menu, menu) {
            @Override
            protected void convert(BaseViewHolder helper, SetupBean item) {
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_content, item.getContent());
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    //清理缓存
                    CacheUtils.clearAllCache();
                    menu.get(position).setContent("0.0K");
                    adapter.notifyDataSetChanged();
                    ToastUtils.showShort("清理缓存成功");
                } else if (position == 1) {
                    //意见反馈
                    Routerfit.register(MineRouterApi.class).skipFeedbackActivity();
                } else if (position == 2) {
                    //关于我们
                    Routerfit.register(MineRouterApi.class).skipAboutUsActivity();
                }
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
            @Override
            public void preData(Object... objects) {

            }

            @Override
            public void refreshData(Object... objects) {

            }

            @Override
            public void loadMoreData(Object... objects) {

            }
        };
    }
}