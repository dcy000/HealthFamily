package com.gcml.module_guardianship;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
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
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.ScreenUtils;
import com.jaeger.library.StatusBarUtil;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

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
    private GuardianshipPresenter guardianshipPresenter;
    private LinearLayout mLlContainer;

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
        mLlContainer = view.findViewById(R.id.ll_container);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mRl.getLayoutParams());
        layoutParams.topMargin = ScreenUtils.getStatusBarHeight(mContext);
        mRl.setLayoutParams(layoutParams);
    }

    @Override
    protected View placeView() {
        return mLlContainer;
    }

    @Override
    public IPresenter obtainPresenter() {
        guardianshipPresenter = new GuardianshipPresenter(this);
        return guardianshipPresenter;
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
                helper.setText(R.id.item_name, item.getResidentBean().getBname());
                helper.setText(R.id.item_address, item.getResidentBean().getDz());
                Glide.with(Box.getApp())
                        .load(item.getResidentBean().getUserPhoto())
                        .into((ImageView) helper.getView(R.id.item_head));

                helper.getView(R.id.iv_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPhoneTipsDialog(item);
                    }
                });
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

    private void showPhoneTipsDialog(GuardianshipBean item) {
        FDialog.build()
                .setSupportFM(getFragmentManager())
                .setLayoutId(R.layout.dialog_layout_phone_tips)
                .setWidth(AutoSizeUtils.pt2px(mContext, 540))
                .setOutCancel(false)
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        holder.setText(R.id.tv_title, item.getResidentBean().getBname() + "的电话号码");
                        holder.setText(R.id.tv_message, "15181438908");
                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callPhone("15181438909");
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show();
    }

    public void callPhone(String phoneNum) {
        RxPermissions permissions = new RxPermissions(mActivity);
        permissions.requestEachCombined(Manifest.permission.CALL_PHONE)
                .subscribe(new CommonObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + phoneNum);
                            intent.setData(data);
                            startActivity(intent);
                        } else {
                            ToastUtils.showLong("拨打电话，需要您同意相关权限");
                        }
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

    @Override
    public void reloadData(View view) {
        guardianshipPresenter.preData(null);
    }
}
