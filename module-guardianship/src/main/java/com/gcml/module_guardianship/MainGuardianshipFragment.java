package com.gcml.module_guardianship;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.presenter.GuardianshipPresenter;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.ScreenUtils;
import com.jaeger.library.StatusBarUtil;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.List;

/**
 * Created by gzq on 19-2-3.
 */

@Route(path = "/guardianship/main")
public class MainGuardianshipFragment extends StateBaseFragment implements View.OnClickListener {
    /**
     * 监护（0）
     */
    private TextView mRuardianshipTitle;
    private ImageView mGuardianshipAdd;
    private RecyclerView mGuardianshipRv;
    /**
     * 搜索
     */
    private EditText mEtGotoSearch;
    private BaseQuickAdapter<GuardianshipBean, BaseViewHolder> adapter;
    private RelativeLayout mRl;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.guardianship_fragment_main;
    }

    @Override
    public void initParams(Bundle bundle) {
    }

    @Override
    public void initView(View view) {
        mRuardianshipTitle = (TextView) view.findViewById(R.id.ruardianship_title);
        mGuardianshipAdd = (ImageView) view.findViewById(R.id.guardianship_add);
        mGuardianshipAdd.setOnClickListener(this);
        mGuardianshipRv = (RecyclerView) view.findViewById(R.id.guardianship_rv);
        mEtGotoSearch = (EditText) view.findViewById(R.id.et_goto_search);
        mEtGotoSearch.setOnClickListener(this);
        mRl = view.findViewById(R.id.rl);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mRl.getLayoutParams());
        layoutParams.topMargin = ScreenUtils.getStatusBarHeight(mContext);
        mRl.setLayoutParams(layoutParams);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new GuardianshipPresenter(this);
    }


    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
        List<GuardianshipBean> guardianships = (List<GuardianshipBean>) objects[0];
        mGuardianshipRv.setLayoutManager(new LinearLayoutManager(mActivity));
        mGuardianshipRv.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 20));
        adapter = new BaseQuickAdapter<GuardianshipBean, BaseViewHolder>(R.layout.item_layout_guardianship, guardianships) {
            @Override
            protected void convert(BaseViewHolder helper, GuardianshipBean item) {
                helper.setText(R.id.item_name, item.getName());
                helper.setText(R.id.item_address, item.getAddress());
                Glide.with(Box.getApp())
                        .load(item.getHead())
                        .into((ImageView) helper.getView(R.id.item_head));
            }
        };
        mGuardianshipRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Routerfit.register(GuardianshipRouterApi.class).skipResidentDetailActivity();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.guardianship_add) {
            Routerfit.register(GuardianshipRouterApi.class).skipQrCodeScanActivity();
        } else if (i == R.id.et_goto_search) {
            Routerfit.register(GuardianshipRouterApi.class).skipSearchFamilyActivity();
        }
    }
}
