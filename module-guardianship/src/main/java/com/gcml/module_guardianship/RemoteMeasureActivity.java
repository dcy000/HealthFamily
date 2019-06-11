package com.gcml.module_guardianship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.module_guardianship.api.GuardianshipApi;
import com.gcml.module_guardianship.bean.MeasureDataBean;
import com.gcml.module_guardianship.presenter.RemoteMeasurePresenter;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.sjtu.yifei.annotation.Route;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

@Route(path = "/guardianship/remote/measure/activity")
public class RemoteMeasureActivity extends StateBaseActivity {
    private ImageView mIvTest;
    /**
     * 开始
     * 检测
     */
    private TextView mTvTime;
    /**
     * 117/94
     */
    private TextView mTvBloodPressure;
    /**
     * 84
     */
    private TextView mTvHeartRate;
    private Disposable countDownDisposable = Disposables.empty();
    private String watchCode;
    private String userId;
    private long currentMill;
    private boolean isMeasureEnd;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_remote_measure;
    }

    @Override
    protected boolean isBackgroundF8F8F8() {
        return true;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        watchCode = intentArgument.getStringExtra("watchCode");
        userId = intentArgument.getStringExtra("userId");
    }

    @Override
    public void initView() {
        showSuccess();
        getTitleTextView().setText("远程测量");
        mIvTest = (ImageView) findViewById(R.id.iv_test);
        mIvTest.setOnClickListener(this);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvTime.setOnClickListener(this);
        mTvBloodPressure = (TextView) findViewById(R.id.tv_blood_pressure);
        mTvHeartRate = (TextView) findViewById(R.id.tv_heart_rate);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new RemoteMeasurePresenter(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_test) {
            if (!isMeasureEnd) {
                fetchData();
            } else {
                ToastUtils.showShort("正在测量，请耐心等候");
            }
        } else if (i == R.id.tv_time) {
            if (!isMeasureEnd) {
                fetchData();
            } else {
                ToastUtils.showShort("正在测量，请耐心等候");
            }
        }
    }


    private void fetchData() {
        Box.getRetrofit(GuardianshipApi.class)
                .measureByHandRing(watchCode, "1")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        currentMill = System.currentTimeMillis();
                        startTimeDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i(e.getMessage());
                    }
                });
    }


    private void startTimeDown() {
        countDownDisposable = RxUtils.rxCountDown(1, 100)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mTvTime.setText("100s"))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        mTvTime.setText("开始 测量");
                    }
                })
                .doOnDispose(() -> {
                    mTvTime.setText("开始 测量");
                    isMeasureEnd = false;
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(integer -> {
                    mTvTime.setText(integer + "s");
                    if (integer % 2 == 0) {
                        getData();
                    }
                });
    }

    private void getData() {
        Box.getRetrofit(GuardianshipApi.class)
                .getMeasureData(userId)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<MeasureDataBean>() {
                    @Override
                    public void onNext(MeasureDataBean bean) {
                        if (bean.getBpTime() > currentMill) {
                            isMeasureEnd = true;
                            countDownDisposable.dispose();
                            mTvBloodPressure.setText(bean.getHighPressure() + "/" + bean.getLowPressure());
                            mTvHeartRate.setText(bean.getHeartRate() + "");
                        }
                    }
                });
    }
}
