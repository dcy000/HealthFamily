package com.gcml.module_health_manager;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_health_manager.api.HealthManagerRouterApi;
import com.gcml.module_health_manager.bean.MenuBena;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.ScreenUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

/**
 * Created by gzq on 19-2-3.
 */
@Route(path = "/healthmanager/main")
public class MainHealthManagerFragment extends StateBaseFragment implements View.OnClickListener {
    /**
     * 智能健康管理
     */
    private TextView mRuardianshipTitle;
    private RelativeLayout mRl;
    private RecyclerView mRvMenuTop;
    private RecyclerView mRvMenuBottom;
    private ArrayList<MenuBena> menuTop = new ArrayList<MenuBena>() {{
        add(new MenuBena(R.drawable.healthmanager_ic_health_task, "健康任务"));
        add(new MenuBena(R.drawable.healthmanager_ic_health_report, "健康方案报告"));
        add(new MenuBena(R.drawable.healthmanager_ic_risk_assessment, "风险评估报告"));
    }};
    private ArrayList<MenuBena> menuBottom = new ArrayList<MenuBena>() {
        {
            add(new MenuBena(R.drawable.healthmanager_ic_health_measure, "健康监测"));
            add(new MenuBena(R.drawable.healthmanager_ic_family_doctor, "家庭医生服务"));
        }
    };
    private BaseQuickAdapter<MenuBena, BaseViewHolder> adapterTop;
    private BaseQuickAdapter<MenuBena, BaseViewHolder> adapterBottom;
    private LinearLayout mLlHealthVideo;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.health_manager_fragment_main;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        showSuccess();
        mRuardianshipTitle = (TextView) view.findViewById(R.id.ruardianship_title);
        mRl = (RelativeLayout) view.findViewById(R.id.rl);
        mRvMenuTop = (RecyclerView) view.findViewById(R.id.rv_menu_top);
        mRvMenuBottom = (RecyclerView) view.findViewById(R.id.rv_menu_bottom);
        mLlHealthVideo = view.findViewById(R.id.ll_health_video);
        mLlHealthVideo.setOnClickListener(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mRl.getLayoutParams());
        layoutParams.topMargin = ScreenUtils.getStatusBarHeight(mContext);
        mRl.setLayoutParams(layoutParams);
        initRv();
    }

    private void initRv() {
        mRvMenuTop.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMenuTop.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 2));
        mRvMenuTop.setAdapter(adapterTop = new BaseQuickAdapter<MenuBena, BaseViewHolder>(R.layout.item_layout_menu, menuTop) {
            @Override
            protected void convert(BaseViewHolder helper, MenuBena item) {
                helper.setText(R.id.title, item.getTitle());
                ((ImageView) helper.getView(R.id.iv_resource)).setImageResource(item.getImgResource());
            }
        });

        mRvMenuBottom.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMenuBottom.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 2));
        mRvMenuBottom.setAdapter(adapterBottom = new BaseQuickAdapter<MenuBena, BaseViewHolder>(R.layout.item_layout_menu, menuBottom) {
            @Override
            protected void convert(BaseViewHolder helper, MenuBena item) {
                helper.setText(R.id.title, item.getTitle());
                ((ImageView) helper.getView(R.id.iv_resource)).setImageResource(item.getImgResource());
            }
        });
        adapterTop.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    //健康任务
                    Routerfit.register(HealthManagerRouterApi.class).skipHealthTaskActivity();
                } else if (position == 1) {
                    //健康方案报告
                    Routerfit.register(HealthManagerRouterApi.class).skipHealthProgrammeReportActivity();
                } else if (position == 2) {
                    //风险评估报告
                    Routerfit.register(HealthManagerRouterApi.class).skipRiskAssessmentReportActivity();
                }
            }
        });
        adapterBottom.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    //健康监测
                    Routerfit.register(HealthManagerRouterApi.class).skipHealthMeasureActivity();
                } else if (position == 1) {
                    //家庭医生服务
                    Routerfit.register(HealthManagerRouterApi.class).skipFamilyDoctorServiceActivity();
                }
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_health_video) {
            //健康宣教
            Routerfit.register(HealthManagerRouterApi.class).skipHealthVideoActivity();
        }
    }
}
