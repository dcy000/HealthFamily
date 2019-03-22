package com.gzq.lib_resource.mvp;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.NetworkUtils;
import com.gzq.lib_resource.R;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.mvp.base.BaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.state_page.DevelopmentPage;
import com.gzq.lib_resource.state_page.EmptyPage;
import com.gzq.lib_resource.state_page.ErrorPage;
import com.gzq.lib_resource.state_page.LoadingPage;
import com.gzq.lib_resource.state_page.NetErrorPage;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * created on 2018/10/31 9:31
 * created by: gzq
 * description: 二次封装的带状态页面的BaseActivity
 */
public abstract class StateBaseActivity<P extends IPresenter> extends BaseActivity implements View.OnClickListener {
    private LoadService mStateView;
    private RelativeLayout mToolbar;
    private LinearLayout mLlLeft;
    private TextView mTvLeft;
    private TextView mTvTitle;
    private LinearLayout mLlRight;
    private TextView mTvRight;
    private ImageView mIvRight;
    private View mContentContain;
    private FDialog progressLoadingDialog;


    public P getP() {
        return (P) super.getP();
    }

    public View getToolbar() {
        return mToolbar;
    }
    public View getLeftView(){
        return mLlLeft;
    }
    public View getRightView(){
        return mLlRight;
    }
    public TextView getLeftTextView(){
        return mTvLeft;
    }
    public TextView getRightTextView(){
        return mTvRight;
    }
    public TextView getTitleTextView(){
        return mTvTitle;
    }
    public ImageView getRightImageView(){
        return mIvRight;
    }
    @Override
    public void setContentView(int layoutResID) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        LinearLayout parent = new LinearLayout(this);
        if (isBackgroundF8F8F8()) {
            parent.setBackgroundColor(Box.getColor(R.color.background_gray_f8f8f8));
        } else {
            parent.setBackgroundColor(Box.getColor(R.color.white));
        }
        parent.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parent);
        if (isShowToolbar()) {
            View mToolbarView = LayoutInflater.from(this).inflate(R.layout.toolbar_layout, parent, true);
            initToolbar(mToolbarView);
        }
        LayoutInflater.from(this).inflate(layoutResID, parent, true);
        mContentContain = parent.getChildAt(1);
        setStatusBar();
    }

    protected void setStatusBar() {
        //设置状态栏的颜色
        StatusBarCompat.setStatusBarColor(this, Box.getColor(R.color.white));
    }

    protected boolean isShowToolbar() {
        return true;
    }

    protected boolean isBackgroundF8F8F8() {
        return false;
    }

    private void initToolbar(View mToolbarView) {
        mToolbar = mToolbarView.findViewById(R.id.toolbar);
        mLlLeft = mToolbarView.findViewById(R.id.ll_left);
        mLlLeft.setOnClickListener(this);
        mTvLeft = mToolbarView.findViewById(R.id.tv_left);
        mTvTitle = mToolbarView.findViewById(R.id.tv_title);
        mLlRight = mToolbarView.findViewById(R.id.ll_right);
        mLlRight.setOnClickListener(this);
        mTvRight = mToolbarView.findViewById(R.id.tv_right);
        mIvRight = mToolbarView.findViewById(R.id.iv_right);
    }

    @Override
    protected void initStateView() {
        Object rootView = placeView();
        if (rootView == null) {
            rootView = mContentContain;
        }
        mStateView = LoadSir.getDefault().register(rootView, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                reloadData(v);
            }
        }).setCallBack(EmptyPage.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                customEmptyPage(context, view);
            }
        }).setCallBack(ErrorPage.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                customErrorPage(context, view);
            }
        }).setCallBack(LoadingPage.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                customLoadingPage(context, view);
            }
        }).setCallBack(NetErrorPage.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                customNetErrorPage(context, view);
            }
        });
        //判断网络是否可用
        if (!NetworkUtils.isAvailable()) {
            showNetError();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressLoading();
    }

    @CallSuper
    @Override
    public void loadDataSuccess(Object... objects) {
        showSuccess();
    }

    @Override
    public void loadDataError(Object... objects) {
        showError();
    }

    @Override
    public void loadDataEmpty() {
        showEmpty();
    }

    @Override
    public void onNetworkError() {
        showNetError();
    }

    protected View placeView() {
        return null;
    }

    protected void customEmptyPage(Context context, View view) {

    }

    protected void customErrorPage(Context context, View view) {

    }

    protected void customLoadingPage(Context context, View view) {

    }

    protected void customNetErrorPage(Context context, View view) {

    }

    /**
     * 重试
     *
     * @param view
     */
    public void reloadData(View view) {

    }

    public void showLoading() {
        if (mStateView != null) {
            mStateView.showCallback(LoadingPage.class);
        }
    }

    public void showEmpty() {
        if (mStateView != null) {
            mStateView.showCallback(EmptyPage.class);
        }
    }

    public void showError() {
        if (mStateView != null) {
            mStateView.showCallback(ErrorPage.class);
        }
    }

    public void showSuccess() {
        if (mStateView != null) {
            mStateView.showSuccess();
        }
    }

    public void showNetError() {
        if (mStateView != null) {
            mStateView.showCallback(NetErrorPage.class);
        }
    }

    public void showDeveloping() {
        if (mStateView != null) {
            mStateView.showCallback(DevelopmentPage.class);
        }
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_left) {
            clickToolbarLeft();
        } else if (id == R.id.ll_right) {
            clickToolbarRight();
        }
    }

    protected void clickToolbarLeft() {
        finish();
    }

    protected void clickToolbarRight() {

    }

    public void showProgressLoading() {
        progressLoadingDialog = FDialog.build()
                .setSupportFM(getSupportFragmentManager())
                .setLayoutId(R.layout.dialog_layout_loading)
                .setDimAmount(1)
                .show();
    }

    public void dismissProgressLoading() {
        if (progressLoadingDialog != null) {
            progressLoadingDialog.dismiss();
        }
        progressLoadingDialog = null;
    }


}
