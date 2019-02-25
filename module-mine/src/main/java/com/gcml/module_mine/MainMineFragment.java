package com.gcml.module_mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gcml.module_mine.api.MineRouterApi;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gzq on 19-2-3.
 */
@Route(path = "/mine/main")
public class MainMineFragment extends StateBaseFragment implements View.OnClickListener {
    private CircleImageView mCivHead;
    /**
     * 李雷
     */
    private TextView mTvName;
    /**
     * 朝明社区
     */
    private TextView mTvCommunity;
    private LinearLayout mLlServiceHistory;
    private LinearLayout mLlSetup;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.mine_fragment_main;
    }

    @Override
    public void initParams(Bundle bundle) {
        showSuccess();
    }

    @Override
    public void initView(View view) {

        mCivHead = (CircleImageView) view.findViewById(R.id.civ_head);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvCommunity = (TextView) view.findViewById(R.id.tv_community);
        mLlServiceHistory = (LinearLayout) view.findViewById(R.id.ll_service_history);
        mLlServiceHistory.setOnClickListener(this);
        mLlSetup = (LinearLayout) view.findViewById(R.id.ll_setup);
        mLlSetup.setOnClickListener(this);

        Glide.with(Box.getApp())
                .load(Box.getString(R.string.head_img))
                .into(mCivHead);
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


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_service_history) {
        } else if (i == R.id.ll_setup) {
            Routerfit.register(MineRouterApi.class).skipSetupActivity();
        } else {
        }
    }
}
