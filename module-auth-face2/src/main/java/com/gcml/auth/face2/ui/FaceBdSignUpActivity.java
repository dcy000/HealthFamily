package com.gcml.auth.face2.ui;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.gcml.auth.face2.BR;
import com.gcml.auth.face2.R;
import com.gcml.auth.face2.databinding.FaceActivityBdSignUpBinding;
import com.gcml.auth.face2.dialog.IconDialog;
import com.gcml.auth.face2.model.FaceBdErrorUtils;
import com.gcml.auth.face2.model.exception.FaceBdError;
import com.gcml.auth.face2.mvvm.BaseActivity;
import com.gcml.auth.face2.utils.NetUitls;
import com.gcml.auth.face2.utils.PreviewHelper;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.bean.UserEntity;
import com.gzq.lib_resource.dialog.DialogViewHolder;
import com.gzq.lib_resource.dialog.FDialog;
import com.gzq.lib_resource.dialog.ViewConvertListener;
import com.gzq.lib_resource.utils.ScreenUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.autosize.utils.AutoSizeUtils;
import timber.log.Timber;

/**
 * 1. 注册人脸
 * 2. 更新人脸
 */
@Route(path = "/face/auth/signup")
public class FaceBdSignUpActivity extends BaseActivity<FaceActivityBdSignUpBinding, FaceBdSignUpViewModel> {

//    private String userId;

    @Override
    protected int layoutId() {
        return R.layout.face_activity_bd_sign_up;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    private PreviewHelper mPreviewHelper;
    private Animation mAnimation;

    @Override
    protected void init(Bundle savedInstanceState) {
//        callId = getIntent().getStringExtra("callId");
        binding.setPresenter(this);
        mPreviewHelper = new PreviewHelper(this);
        mPreviewHelper.setSurfaceHolder(binding.svPreview.getHolder());
        mPreviewHelper.setPreviewView(binding.svPreview);
        mAnimation = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.face_anim_rotate);
        binding.ivAnimation.setAnimation(mAnimation);
        binding.ivAnimation.post(new Runnable() {
            @Override
            public void run() {
                int[] outLocation = new int[2];
                int barHeight = ScreenUtils.getStatusBarHeight(FaceBdSignUpActivity.this);
                Timber.i("Face CropRect: %s x %s", outLocation[0], outLocation[1]);
                binding.ivAnimation.getLocationOnScreen(outLocation);
                mPreviewHelper.setCropRect(new Rect(
                        outLocation[0],
                        outLocation[1] + 2*barHeight,
                        outLocation[0] + binding.ivAnimation.getWidth(),
                        outLocation[1] + binding.ivAnimation.getHeight() + 2* barHeight
                ));
            }
        });
        binding.previewMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                start();
//                takeFrames("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPreviewHelper.rxStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<PreviewHelper.Status>() {
                    @Override
                    public void onNext(PreviewHelper.Status status) {
                        onPreviewStatusChanged(status);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        start();
    }

//    private IconDialog iconDialog;

    private <T> ObservableTransformer<T, T> checkFace() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .flatMap(new Function<T, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(T t) throws Exception {
                                return rxShowAvatar(t);
                            }
                        });
            }
        };
    }

    private FDialog dialog;

    private <T> Observable<T> rxShowAvatar(T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                dialog = FDialog.build()
                        .setSupportFM(getSupportFragmentManager())
                        .setLayoutId(R.layout.dialog_layout_head_confirm_cancel)
                        .setWidth(AutoSizeUtils.pt2px(FaceBdSignUpActivity.this, 600))
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(DialogViewHolder holder, FDialog dialog) {
                                Glide.with(Box.getApp())
                                        .load(maps.get(0))
                                        .into(((CircleImageView) holder.getView(R.id.civ_head)));
                                holder.getView(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        if (!emitter.isDisposed()) {
                                            emitter.onError(new RuntimeException("retry"));
                                        }
                                    }
                                });

                                holder.getView(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        if (!emitter.isDisposed()) {
                                            emitter.onNext(t);
                                        }
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.5f)
                        .setOutCancel(false)
                        .show();
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread());
    }

    private void start() {
        mPreviewHelper.rxFrame()
                .buffer(1)
                .map(bitmapToBase64Mapper())
                .compose(viewModel.ensureLive())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String img) throws Exception {
                        imageData = img;
                        if (TextUtils.isEmpty(img)) {
                            return Observable.error(new FaceBdError(FaceBdErrorUtils.ERROR_FACE_LIVELESS, ""));
                        }
                        return Observable.just(img);
                    }
                })
                .compose(checkFace())
//                .compose(viewModel.ensureFaceAdded())
//                .compose(ensureAddFace())
                .flatMap(new Function<String, ObservableSource<String>>() {

                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        UserEntity user = Box.getSessionManager().getUser();
                        return viewModel.addFaceByApi(user.getUserId(), s, image)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        binding.ivAnimation.startAnimation(mAnimation);
                        binding.ivTips.setText("人脸识别中");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        binding.ivAnimation.clearAnimation();
                        imageData = "";

                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.w(throwable);
                        FaceBdError wrapped = FaceBdErrorUtils.wrap(throwable);
                        String msg = FaceBdErrorUtils.getMsg(wrapped.getCode());
                        binding.ivTips.setText(msg);
                        error = true;
                        start();
                        takeFrames(msg);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        binding.previewMask.setEnabled(true);
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        error = false;
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // "${imageType},${image}"
    private volatile String imageData = "";

    private volatile ArrayList<String> images;
    private volatile List<Bitmap> maps;
    private volatile byte[] image;

    @NonNull
    private Function<List<Bitmap>, List<String>> bitmapToBase64Mapper() {
        return new Function<List<Bitmap>, List<String>>() {
            @Override
            public List<String> apply(List<Bitmap> bitmaps) throws Exception {
                maps = bitmaps;
                images = new ArrayList<>();
                for (Bitmap bitmap : bitmaps) {
                    image = PreviewHelper.bitmapToBytes(bitmaps.get(0));
                    images.add(PreviewHelper.bitmapToBase64(bitmap, false));
                }
                return images;
            }
        };
    }

//    private IconDialog iconDialog;

    private void onPreviewStatusChanged(PreviewHelper.Status status) {
        if (status.code == PreviewHelper.Status.ERROR_ON_OPEN_CAMERA) {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            binding.ivTips.setText("打开相机失败");
            ToastUtils.showShort("打开相机失败");
        } else if (status.code == PreviewHelper.Status.EVENT_CAMERA_OPENED) {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
//            start(0);
            takeFrames("");
        }
    }

    private void takeFrames(String msg) {
        binding.previewMask.setEnabled(false);
        if (!NetUitls.isConnected()) {
            binding.previewMask.setEnabled(true);
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "请把脸对准框内";
        }
        binding.ivTips.setText(msg);
//        MLVoiceSynthetize.startSynthesize(getApplicationContext(), msg);

        mPreviewHelper.takeFrames(1, 1800, 500);
    }

    public ObservableTransformer<String, String> ensureAddFace() {
        return new ObservableTransformer<String, String>() {
            @Override
            public ObservableSource<String> apply(Observable<String> upstream) {
                return upstream
                        .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
                            @Override
                            public ObservableSource<? extends String> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof FaceBdError) {
                                    FaceBdError error = (FaceBdError) throwable;
                                    if (error.getCode() == FaceBdErrorUtils.ERROR_USER_NOT_EXIST
                                            || error.getCode() == FaceBdErrorUtils.ERROR_USER_NOT_FOUND) {
                                        UserEntity user = Box.getSessionManager().getUser();
                                        return viewModel.addFace(imageData, user.getUserId())
                                                .subscribeOn(Schedulers.io());
                                    }
                                }
                                return Observable.error(throwable);
                            }
                        });
            }
        };
    }

    public void goBack() {
        finish();
    }

    public void goWifi() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mPreviewHelper != null) {
            mPreviewHelper.configCamera();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
//        if (!TextUtils.isEmpty(callId)) {
//            CCResult result;
//            if (error) {
//                result = CCResult.error("人脸录入失败");
//            } else {
//                result = CCResult.success("faceId", userId);
//            }
//            //为确保不管登录成功与否都会调用CC.sendCCResult，在onDestroy方法中调用
//            CC.sendCCResult(callId, result);
//        }
        if (error) {
            Routerfit.setResult(RESULT_OK, "人脸录入失败");
        } else {
            Routerfit.setResult(RESULT_OK, "success");
        }
        super.finish();
    }

    //    private String callId;
    private volatile boolean error = true;
}
