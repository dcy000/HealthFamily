package com.gcml.module_guardianship;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.api.GuardianshipRouterApi;
import com.gcml.module_guardianship.bean.GuardianshipBean;
import com.gcml.module_guardianship.bean.WatchInformationBean;
import com.gcml.module_guardianship.presenter.GuardianshipPresenter;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.constants.KVConstants;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.CallPhoneUtils;
import com.gzq.lib_resource.utils.ScreenUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by gzq on 19-2-3.
 */

@Route(path = "/guardianship/main")
public class MainGuardianshipFragment extends StateBaseFragment<GuardianshipPresenter> implements View.OnClickListener {
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
    private List<GuardianshipBean> guardianshipBeans = new ArrayList<>();

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
        initRv();
    }

    private void initRv() {
        mGuardianshipRv.setLayoutManager(new LinearLayoutManager(mActivity));
        mGuardianshipRv.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 20));
        adapter = new BaseQuickAdapter<GuardianshipBean, BaseViewHolder>(R.layout.item_layout_guardianship, guardianshipBeans) {
            @Override
            protected void convert(BaseViewHolder helper, GuardianshipBean item) {
                helper.setText(R.id.item_name, item.getBname());
                helper.setText(R.id.item_address, TextUtils.isEmpty(item.getDz()) ? "未填" : item.getDz());
                Glide.with(Box.getApp())
                        .load(item.getUserPhoto())
                        .into((ImageView) helper.getView(R.id.item_head));

                helper.getView(R.id.iv_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWatchInfo(item);
                    }
                });
            }
        };
        mGuardianshipRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KVUtils.put(KVConstants.KEY_PATIENTID, guardianshipBeans.get(position).getBid());
                Routerfit.register(GuardianshipRouterApi.class).skipResidentDetailActivity(guardianshipBeans.get(position));
            }
        });
    }


    @Override
    public IPresenter obtainPresenter() {
        return new GuardianshipPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getP().preData();
    }

    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
        guardianshipBeans.clear();
        List<GuardianshipBean> object = (List<GuardianshipBean>) objects[0];
        guardianshipBeans.addAll(object);
        adapter.notifyDataSetChanged();
        mRuardianshipTitle.setText("监护(" + object.size() + ")");

    }

    private void getWatchInfo(GuardianshipBean guardianshipBean) {
        Box.getRetrofit(GuardianshipApi.class)
                .getWatchInfo(guardianshipBean.getWatchCode())
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<WatchInformationBean>() {
                    @Override
                    public void onNext(WatchInformationBean watchInformationBean) {
                        showPhoneTipsDialog(guardianshipBean.getBname(), watchInformationBean.getDeviceMobileNo());
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showPhoneTipsDialog(guardianshipBean.getBname(), guardianshipBean.getTel());
                    }
                });
    }

    private void showPhoneTipsDialog(String name, String phone) {

        FDialog.build()
                .setSupportFM(getFragmentManager())
                .setLayoutId(R.layout.dialog_layout_phone_tips)
                .setWidth(AutoSizeUtils.pt2px(mContext, 540))
                .setOutCancel(false)
                .setDimAmount(0.5f)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, FDialog dialog) {
                        holder.setText(R.id.tv_title, name + "的电话号码");
                        holder.setText(R.id.tv_message, phone);
                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallPhoneUtils.instance().callPhone(mActivity, phone);
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
        getP().preData();
    }
}
