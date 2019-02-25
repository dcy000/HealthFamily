package com.gcml.module_mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/mine/feedback")
public class FeedbackActivity extends StateBaseActivity {
    /**
     * 亲，我们非常重视您给我们提出的宝贵建议，帮助我们不断完善产品，谢谢
     */
    private EditText mEtContent;
    /**
     * 0/200
     */
    private TextView mTvContentLength;
    /**
     * 填写您的手机或邮箱
     */
    private EditText mEtEmail;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_feedback;
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
        mTvTitle.setText("意见反馈");
        mLlRight.setVisibility(View.VISIBLE);
        mIvRight.setVisibility(View.GONE);
        mTvRight.setText("写好了");
        mTvRight.setTextColor(Box.getColor(R.color.colorAccent));
        mEtContent = (EditText) findViewById(R.id.et_content);
        mTvContentLength = (TextView) findViewById(R.id.tv_content_length);
        mEtEmail = (EditText) findViewById(R.id.et_email);
    }

    @Override
    protected void clickToolbarRight() {
        ToastUtils.showShort("写好了");
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
