package com.ml.module_shouhuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.gzq.lib_resource.bean.MsgBean;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.data.TimeUtils;
import com.ml.module_shouhuan.R;
import com.sjtu.yifei.annotation.Route;

import java.text.SimpleDateFormat;

@Route(path = "/shouhuan/msg/alreadydo")
public class MsgAlreadyDoActivity extends StateBaseActivity {
    /**
     * 李小大发起紧急呼救！
     */
    private TextView mTvTitleTitle;
    /**
     * 浙江省杭州市萧山区建设二路957号（地铁站）
     */
    private TextView mTvAddress;
    /**
     * 2019.01.17 16:32
     */
    private TextView mTvTime;
    private TextView mTvResult;
    private MsgBean msg;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_msg_already_do;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        msg = intentArgument.getParcelableExtra("msg");
    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("已处理信息");
        mTvTitleTitle = (TextView) findViewById(R.id.tv_title_title);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mTvTitleTitle.setText(msg.getUserName() + "发起的紧急呼叫");
        mTvAddress.setText(msg.getWarningAddress());
        mTvTime.setText(TimeUtils.milliseconds2String(msg.getWarningTime(), new SimpleDateFormat("yyyy.MM.dd HH:mm")));
        mTvResult.setText(msg.getDealContent());
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

}
