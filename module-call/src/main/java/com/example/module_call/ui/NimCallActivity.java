package com.example.module_call.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gzq.lib_resource.mvp.base.BaseActivity;
import com.gzq.lib_resource.utils.Handlers;
import com.example.module_call.R;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatOnlineAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.sjtu.yifei.annotation.Route;

import java.util.Map;
@Route(path = "/nim/call/activity")
public class NimCallActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "NimCallActivity";

    public static final String EXTRA_INCOMING_CALL = "extra_incoming_call";
    public static final String EXTRA_SOURCE = "extra_source";
    public static final String EXTRA_PEER_ACCOUNT = "extra_peer_account";
    public static final String EXTRA_CALL_CONFIG = "extra_call_config";
    public static final String EXTRA_CALL_TYPE = "extra_call_type";
    public static final int SOURCE_UNKNOWN = -1;
    public static final int SOURCE_BROADCAST = 0;
    public static final int SOURCE_INTERNAL = 1;
    private Handler mHandler = new Handler();
    private ImageView mIvCallSmallCover;
    private FrameLayout mFlCallSmallContainer;
    private FrameLayout mFlCallLargeContainer;
    private TextView mTvCallTime;
    private ImageView mIvCallPeerAvatar;
    /**  */
    private TextView mTvCallNickname;
    private TextView mTvCallStatus;
    private ImageView mIcCallSwitchCamera;
    private ImageView mIvCallToggleCamera;
    private ImageView mIvCallToggleMute;
    private ImageView mIvCallHangUp;
    /**
     * 拒绝
     */
    private TextView mTvCallRefuse;
    /**
     * 接听
     */
    private TextView mTvCallReceive;
    private ImageView mIvFinish;
    private ConstraintLayout mClCallRoot;

    public static void launchNoCheck(final Context context, final String account) {
        launch(context, account, AVChatType.VIDEO.getValue(), SOURCE_INTERNAL);
    }

//    public static void launch(final Context context, final String account) {
//        Box.getRetrofit(CommonAPI.class)
//                .queryMoneyById(DeviceUtils.getIMEI())
//                .compose(RxUtils.<RobotAmount>httpResponseTransformer())
//                .subscribe(new CommonObserver<RobotAmount>() {
//                    @Override
//                    public void onNext(RobotAmount robotAmount) {
//                        final String amount = robotAmount.getAmount();
//
//                        if (!TextUtils.isEmpty(amount) && Float.parseFloat(amount) > 0) {
//                            //有余额
//                            launch(context, account, AVChatType.VIDEO.getValue(), SOURCE_INTERNAL);
//                        } else {
//                            ToastUtils.showShort("余额不足，请充值后再试");
//                        }
//                    }
//                });
//    }

    public static void launch(Context context, String account, int callType, int source) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, NimCallActivity.class);
        intent.putExtra(EXTRA_PEER_ACCOUNT, account);
        intent.putExtra(EXTRA_INCOMING_CALL, false);
        intent.putExtra(EXTRA_CALL_TYPE, callType);
        intent.putExtra(EXTRA_SOURCE, source);
        context.startActivity(intent);
    }

    public static void launch(Context context, AVChatData config, int source) {
        Intent intent = new Intent();
        intent.setClass(context, NimCallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_CALL_CONFIG, config);
        intent.putExtra(EXTRA_INCOMING_CALL, true);
        intent.putExtra(EXTRA_SOURCE, source);
        context.startActivity(intent);
    }

    private boolean mIsIncomingCall;
    public AVChatData mCallData;
    public int mCallType;
    public String mPeerAccount;
    public AVChatSurfaceViewRenderer mSmallRenderer;
    public AVChatSurfaceViewRenderer mLargeRenderer;

    private boolean shouldEnableToggle = false;


    public NimCallHelper.OnCallStateChangeListener mCallListener = new NimCallHelper.OnCallStateChangeListener() {
        @Override
        public void onCallStateChanged(CallState state) {
            if (mClCallRoot == null) {
                return;
            }
            if (CallState.isVideoMode(state))
                switch (state) {
                    case OUTGOING_VIDEO_CALLING:
                        showProfile(true);
                        showStatus(R.string.avchat_wait_recieve);
                        showTime(false);
                        showRefuseReceive(false);
                        shouldEnableToggle = true;
                        enableCameraToggle();
                        showBottomPanel(true);
                        break;
                    case INCOMING_VIDEO_CALLING:
                        showProfile(true);
                        showStatus(R.string.avchat_video_call_request);
                        showTime(false);
                        showRefuseReceive(true);
                        showBottomPanel(false);
                        break;
                    case VIDEO_CONNECTING:
                        showStatus(R.string.avchat_connecting);
                        shouldEnableToggle = true;
                        break;
                    case VIDEO:
                        canSwitchCamera = true;
                        showProfile(false);
                        hideStatus();
                        showTime(true);
                        showRefuseReceive(false);
                        showBottomPanel(true);
                        break;
                    case OUTGOING_AUDIO_TO_VIDEO:
                        break;
                    default:
                        break;
                }
        }
    };
    public String mLargeAccount;
    public String mSmallAccount;
    private boolean isClosedCamera;
    private boolean isLocalVideoOff;
    private boolean localPreviewInSmallSize;
    private boolean canSwitchCamera;
    private boolean isPeerVideoOff;

    private void showStatus(@StringRes int res) {
        mTvCallStatus.setVisibility(View.VISIBLE);
        mTvCallStatus.setText(res);
    }

    private void hideStatus() {
        mTvCallStatus.setVisibility(View.GONE);
    }

    private void showBottomPanel(boolean show) {
        mIcCallSwitchCamera.setVisibility(show ? View.VISIBLE : View.GONE);
        mIvCallToggleCamera.setVisibility(show ? View.VISIBLE : View.GONE);
        mIvCallToggleMute.setVisibility(show ? View.VISIBLE : View.GONE);
        mIvCallHangUp.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showTime(boolean show) {
        mTvCallTime.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void enableCameraToggle() {
        if (shouldEnableToggle) {
            if (canSwitchCamera && AVChatCameraCapturer.hasMultipleCameras())
                mIcCallSwitchCamera.setEnabled(true);
        }
    }

    private void showRefuseReceive(boolean show) {
        mTvCallRefuse.setVisibility(show ? View.VISIBLE : View.GONE);
        mTvCallReceive.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    private void showProfile(boolean show) {
        //show avatar
        mIvCallPeerAvatar.setVisibility(show ? View.VISIBLE : View.GONE);
        mTvCallNickname.setVisibility(show ? View.VISIBLE : View.GONE);
        String peerAccount = NimCallHelper.getInstance().getPeerAccount();
        mTvCallNickname.setText(peerAccount);

    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_nim_call;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        if (!validSource()) {
            finish();
            return;
        }

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        mIsIncomingCall = getIntent().getBooleanExtra(EXTRA_INCOMING_CALL, false);
        NimCallHelper.getInstance().initCallParams();
        NimCallHelper.getInstance().setChatting(true);

        //init
        mSmallRenderer = new AVChatSurfaceViewRenderer(this);
        mLargeRenderer = new AVChatSurfaceViewRenderer(this);
        NimCallHelper.getInstance().addOnCallStateChangeListener(mCallListener);

        registerNimCallObserver(true);
        if (mIsIncomingCall) {
            incomingCalling();
        } else {
            outgoingCalling();
        }
        isCallEstablished = false;
        NimCallHelper.getInstance().setOnCloseSessionListener(new NimCallHelper.OnCloseSessionListener() {
            @Override
            public void onCloseSession() {
//                closeSession();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeSession();
                    }
                }, 1000);
            }
        });
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
    }


    @Override
    public void initView() {
        mIvCallSmallCover = (ImageView) findViewById(R.id.iv_call_small_cover);
        mFlCallSmallContainer = (FrameLayout) findViewById(R.id.fl_call_small_container);
        mFlCallLargeContainer = (FrameLayout) findViewById(R.id.fl_call_large_container);
        mTvCallTime = (TextView) findViewById(R.id.tv_call_time);
        mIvCallPeerAvatar = (ImageView) findViewById(R.id.iv_call_peer_avatar);
        mTvCallNickname = (TextView) findViewById(R.id.tv_call_nickname);
        mTvCallStatus = (TextView) findViewById(R.id.tv_call_status);
        mIcCallSwitchCamera = (ImageView) findViewById(R.id.ic_call_switch_camera);
        mIcCallSwitchCamera.setOnClickListener(this);
        mIvCallToggleCamera = (ImageView) findViewById(R.id.iv_call_toggle_camera);
        mIvCallToggleCamera.setOnClickListener(this);
        mIvCallToggleMute = (ImageView) findViewById(R.id.iv_call_toggle_mute);
        mIvCallToggleMute.setOnClickListener(this);
        mIvCallHangUp = (ImageView) findViewById(R.id.iv_call_hang_up);
        mIvCallHangUp.setOnClickListener(this);
        mTvCallRefuse = (TextView) findViewById(R.id.tv_call_refuse);
        mTvCallRefuse.setOnClickListener(this);
        mTvCallReceive = (TextView) findViewById(R.id.tv_call_receive);
        mTvCallReceive.setOnClickListener(this);
        mIvFinish = (ImageView) findViewById(R.id.iv_finish);
        mIvFinish.setOnClickListener(this);
        mClCallRoot = (ConstraintLayout) findViewById(R.id.cl_call_root);
    }


    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    private void outgoingCalling() {
        final AVChatType chatType = mCallType == AVChatType.VIDEO.getValue() ? AVChatType.VIDEO : AVChatType.AUDIO;
        final String account = NimAccountHelper.getInstance().getAccount();

        NimCallHelper.getInstance().call2(mPeerAccount, chatType, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                Log.i("mylog444444444444", "success avChatData : " + avChatData);
                if (chatType == AVChatType.VIDEO) {
                    initLargeSurfaceView(account);
                    canSwitchCamera = true;
                }
            }

            @Override
            public void onFailed(int code) {
                Log.i("mylog444444444444", "failed code : " + code);
            }

            @Override
            public void onException(Throwable exception) {
                Log.i("mylog444444444444", "exception : " + exception);
            }
        });
    }

    private void initSmallSurfaceView(String account) {
        mSmallAccount = account;
        if (account.equals(NimAccountHelper.getInstance().getAccount())) {
            AVChatManager.getInstance().setupLocalVideoRender(mSmallRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(account, mSmallRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        ViewParent parent = mSmallRenderer.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mSmallRenderer);
        }
        mFlCallSmallContainer.addView(mSmallRenderer, 0);
        mSmallRenderer.setZOrderMediaOverlay(true);
    }

    private void initLargeSurfaceView(String account) {
        if (mFlCallLargeContainer == null) {
            return;
        }
        mLargeAccount = account;
        if (account.equals(NimAccountHelper.getInstance().getAccount())) {
            AVChatManager.getInstance().setupLocalVideoRender(
                    mLargeRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        } else {
            AVChatManager.getInstance().setupRemoteVideoRender(
                    account, mLargeRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        ViewParent parent = mLargeRenderer.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mLargeRenderer);
        }
        mFlCallLargeContainer.addView(mLargeRenderer, 0);
        mLargeRenderer.setZOrderMediaOverlay(false);
    }


    private void incomingCalling() {
        NimCallHelper.getInstance().inComingCalling(mCallData);
    }

    /**
     * 判断来电还是去电
     *
     * @return
     */
    private boolean validSource() {
        Intent intent = getIntent();
        int source = intent.getIntExtra(EXTRA_SOURCE, SOURCE_UNKNOWN);
        switch (source) {
            case SOURCE_BROADCAST: // incoming call
                mCallData = (AVChatData) intent.getSerializableExtra(EXTRA_CALL_CONFIG);
                mCallType = mCallData.getChatType().getValue();
                return true;
            case SOURCE_INTERNAL: // outgoing call
                mPeerAccount = intent.getStringExtra(EXTRA_PEER_ACCOUNT);
                mCallType = intent.getIntExtra(EXTRA_CALL_TYPE, -1);
                return mCallType == AVChatType.VIDEO.getValue()
                        || mCallType == AVChatType.AUDIO.getValue();
            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
        NimCallHelper.getInstance().setChatting(false);
        registerNimCallObserver(false);
        NimCallHelper.getInstance().destroy();
//        stopTimer();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NimCallHelper.getInstance().resumeVideo();
    }

    @Override
    protected void onPause() {
        NimCallHelper.getInstance().pauseVideo();
        super.onPause();
    }


    private void registerNimCallObserver(boolean register) {
        AVChatManager.getInstance().observeAVChatState(callStateObserver, register);
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, register);
        AVChatManager.getInstance().observeControlNotification(callControlObserver, register);
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, register);
        AVChatManager.getInstance().observeOnlineAckNotification(onlineAckObserver, register);
        CallTimeoutObserver.getInstance().observeTimeoutNotification(timeoutObserver, register, mIsIncomingCall);
        PhoneStateObserver.getInstance().observeAutoHangUpForLocalPhone(autoHangUpForLocalPhoneObserver, register);
    }

    private boolean isCallEstablished;

    private AVChatStateObserver callStateObserver = new AVChatStateObserver() {
        @Override
        public void onTakeSnapshotResult(String account, boolean success, String file) {

        }

        @Override
        public void onAVRecordingCompletion(String account, String filePath) {

        }

        @Override
        public void onAudioRecordingCompletion(String filePath) {

        }

        @Override
        public void onLowStorageSpaceWarning(long availableSize) {

        }

        @Override
        public void onAudioMixingEvent(int event) {

        }

        @Override
        public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {
            Log.i(TAG, "result code->" + code);
            if (code == 200) {
                Log.d(TAG, "onConnectServer success");
            } else if (code == 101) { // 连接超时
                NimCallHelper.getInstance().closeSessions(CallExitCode.PEER_NO_RESPONSE);
            } else if (code == 401) { // 验证失败
                NimCallHelper.getInstance().closeSessions(CallExitCode.CONFIG_ERROR);
            } else if (code == 417) { // 无效的channelId
                NimCallHelper.getInstance().closeSessions(CallExitCode.INVALIDE_CHANNELID);
            } else { // 连接服务器错误，直接退出
                NimCallHelper.getInstance().closeSessions(CallExitCode.CONFIG_ERROR);
            }
        }

        @Override
        public void onUserJoined(String account) {
            Log.d(TAG, "onUserJoined -> " + account);
            NimCallHelper.getInstance().setPeerAccount(account);
            initLargeSurfaceView(NimCallHelper.getInstance().getPeerAccount());
        }

        @Override
        public void onUserLeave(String account, int event) {
            Log.d(TAG, "onUserLeave -> " + account);
            mIvCallHangUp.performClick();
            NimCallHelper.getInstance().closeSessions(CallExitCode.HANGUP);
        }

        @Override
        public void onLeaveChannel() {

        }

        @Override
        public void onProtocolIncompatible(int status) {

        }

        @Override
        public void onDisconnectServer() {

        }

        @Override
        public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {

        }

        @Override
        public void onCallEstablished() {
            CallTimeoutObserver.getInstance().observeTimeoutNotification(
                    timeoutObserver, false, mIsIncomingCall);
            startTimer();

            if (mCallType == AVChatType.VIDEO.getValue()) {
                NimCallHelper.getInstance().notifyCallStateChanged(CallState.VIDEO);
                initSmallSurfaceView(NimAccountHelper.getInstance().getAccount());
            } else {
                NimCallHelper.getInstance().notifyCallStateChanged(CallState.AUDIO);
            }
            isCallEstablished = true;
        }

        @Override
        public void onDeviceEvent(int code, String desc) {

        }

        @Override
        public void onConnectionTypeChanged(int netType) {

        }

        @Override
        public void onFirstVideoFrameAvailable(String account) {

        }

        @Override
        public void onFirstVideoFrameRendered(String user) {

        }

        @Override
        public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {

        }

        @Override
        public void onVideoFpsReported(String account, int fps) {

        }

        @Override
        public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
            return false;
        }

        @Override
        public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
            return true;
        }

        @Override
        public void onAudioDeviceChanged(int device) {

        }

        @Override
        public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {

        }

        @Override
        public void onSessionStats(AVChatSessionStats sessionStats) {

        }

        @Override
        public void onLiveEvent(int event) {

        }
    };

    /**
     * 注册/注销网络通话被叫方的响应（接听、拒绝、忙）
     */
    Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
        @Override
        public void onEvent(AVChatCalleeAckEvent ackInfo) {
            AVChatData info = NimCallHelper.getInstance().getAvChatData();
            Log.i("mylog555555555555", "call ack : " + info);
            if (info != null && info.getChatId() == ackInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                    CallSoundPlayer.instance().play(CallSoundPlayer.RingerType.PEER_BUSY);
                    NimCallHelper.getInstance().closeSessions(CallExitCode.PEER_BUSY);
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                    NimCallHelper.getInstance().closeRtc();
                    NimCallHelper.getInstance().closeSessions(CallExitCode.REJECT);
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                    NimCallHelper.getInstance().setCallEstablished(true);
                    canSwitchCamera = true;
                }
            }
            Log.i("mylog555555555555", "call ack : " + info);
        }
    };

    /**
     * 注册/注销网络通话控制消息（音视频模式切换通知）
     */
    Observer<AVChatControlEvent> callControlObserver = new Observer<AVChatControlEvent>() {
        @Override
        public void onEvent(AVChatControlEvent notification) {
            if (AVChatManager.getInstance().getCurrentChatId() != notification.getChatId()) {
                return;
            }
            switch (notification.getControlCommand()) {
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO:
//                avChatUI.incomingAudioToVideo();
//                break;
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_AGREE:
//                onAudioToVideo();
//                break;
//            case AVChatControlCommand.SWITCH_AUDIO_TO_VIDEO_REJECT:
//                avChatUI.onCallStateChange(CallStateEnum.AUDIO);
//                Toast.makeText(AVChatActivity.this, R.string.avchat_switch_video_reject, Toast.LENGTH_SHORT).show();
//                break;
//            case AVChatControlCommand.SWITCH_VIDEO_TO_AUDIO:
//                onVideoToAudio();
//                break;
                case AVChatControlCommand.NOTIFY_VIDEO_OFF:
                    isPeerVideoOff = true;
                    if (!localPreviewInSmallSize) {
                        mIvCallSmallCover.setVisibility(View.VISIBLE);
                    }
                    break;
                case AVChatControlCommand.NOTIFY_VIDEO_ON:
                    isPeerVideoOff = false;
                    if (!localPreviewInSmallSize) {
                        mIvCallSmallCover.setVisibility(View.GONE);
                    }
                    break;
                default:
                    Log.i(TAG, "对方发来指令值：" + notification.getControlCommand());
                    break;
            }
        }
    };

    /**
     * 注册/注销网络通话对方挂断的通知
     */
    Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
        @Override
        public void onEvent(AVChatCommonEvent avChatHangUpInfo) {
            AVChatData info = NimCallHelper.getInstance().getAvChatData();
            Log.i("mylog555555555555", "hang up info : " + info);
            if (info != null && info.getChatId() == avChatHangUpInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                NimCallHelper.getInstance().closeRtc();
                NimCallHelper.getInstance().closeSessions(CallExitCode.HANGUP);
            }
            Log.i("mylog555555555555", "after hang up info : " + info);
        }
    };

    /**
     * 注册/注销同时在线的其他端对主叫方的响应
     */
    Observer<AVChatOnlineAckEvent> onlineAckObserver = new Observer<AVChatOnlineAckEvent>() {
        @Override
        public void onEvent(AVChatOnlineAckEvent ackInfo) {
            AVChatData info = NimCallHelper.getInstance().getAvChatData();
            Log.i("mylog555555555555", "online ack : " + info);
            if (info != null && info.getChatId() == ackInfo.getChatId()) {
                CallSoundPlayer.instance().stop();
                String client = null;
                switch (ackInfo.getClientType()) {
                    case ClientType.Web:
                        client = "Web";
                        break;
                    case ClientType.Windows:
                        client = "Windows";
                        break;
                    case ClientType.Android:
                        client = "Android";
                        break;
                    case ClientType.iOS:
                        client = "iOS";
                        break;
                    case ClientType.MAC:
                        client = "Mac";
                        break;
                    default:
                        break;
                }
                if (client != null) {
                    String option = ackInfo.getEvent() == AVChatEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE ? "接听！" : "拒绝！";
                    ToastUtils.showShort("通话已在" + client + "端被" + option);
                }
                NimCallHelper.getInstance().closeSessions(-1);
            }
            Log.i("mylog555555555555", "online ack : " + info);
        }
    };

    Observer<Integer> timeoutObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            NimCallHelper.getInstance().hangUp();
            CallSoundPlayer.instance().stop();
        }
    };

    Observer<Integer> autoHangUpForLocalPhoneObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {
            CallSoundPlayer.instance().stop();
            NimCallHelper.getInstance().closeSessions(CallExitCode.PEER_BUSY);
        }
    };

    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                CallSoundPlayer.instance().stop();
                finish();
            }
        }
    };

    private int mSeconds = 0;

    private void startTimer() {
        mSeconds = 0;
        Handlers.runOnUiThread(refreshCallTime);
    }

//    private void stopTimer() {
//        Handlers.ui().removeCallbacks(refreshCallTime);
//        if (mSeconds > 0) {
//            final int minutes = mSeconds / 60 + 1;
//            if (minutes >= 0) {
//                final String bid = Box.getUserId();
//
//                if ((!TextUtils.isEmpty(mPeerAccount)
//                        && !mPeerAccount.startsWith("docter_"))
//                        || (mCallData != null
//                        && !TextUtils.isEmpty(mCallData.getAccount())
//                        && !mCallData.getAccount().startsWith("docter_"))) {
//                    return;
//                }
//                UserEntity user = Box.getSessionManager().getUser();
//                final String doid = user.doid;
//                if (TextUtils.isEmpty(doid)) {
//                    ToastUtils.showShort("请先签约医生");
//                    return;
//                }
//                Box.getRetrofit(CallAPI.class)
//                        .charge(doid, DeviceUtils.getIMEI(), minutes, Box.getUserId())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new CommonObserver<String>() {
//                            @Override
//                            public void onNext(String response) {
//                                ToastUtils.showShort(minutes + "分钟");
//                                if (TextUtils.isEmpty(response)) {
//                                    return;
//                                }
//                                try {
//                                    JSONObject mResult = new JSONObject(response);
//                                    emitEvent("skip2AppraiseActivity", mResult.getInt("daid"), doid);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//            }
//        }
//    }

    private final Runnable refreshCallTime = new Runnable() {
        @Override
        public void run() {
            Handlers.ui().postDelayed(refreshCallTime, 1000);
            if (!mTvCallTime.isShown()) {
                mTvCallTime.setVisibility(View.VISIBLE);
            }
            mTvCallTime.setText(formatCallTime(mSeconds));
            mSeconds++;
        }
    };

    private static String formatCallTime(int seconds) {
        final int hh = seconds / 60 / 60;
        final int mm = seconds / 60 % 60;
        final int ss = seconds % 60;
        return (hh > 9 ? "" + hh : "0" + hh) +
                (mm > 9 ? ":" + mm : ":0" + mm) +
                (ss > 9 ? ":" + ss : ":0" + ss);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ic_call_switch_camera) {
            NimCallHelper.getInstance().switchCamera();
        } else if (i == R.id.iv_call_toggle_camera) {
            boolean selected = mIvCallToggleCamera.isSelected();
            AVChatManager.getInstance().muteLocalVideo(!selected);
            mIvCallToggleCamera.setSelected(!selected);
            mIvCallSmallCover.setVisibility(!selected ? View.GONE : View.VISIBLE);
        } else if (i == R.id.iv_call_toggle_mute) {
            boolean established = NimCallHelper.getInstance().isCallEstablished();
            if (established) { // 连接已经建立
                boolean muted = AVChatManager.getInstance().isLocalAudioMuted();
                AVChatManager.getInstance().muteLocalAudio(!muted);
                mIvCallToggleMute.setSelected(!muted);
            }
        } else if (i == R.id.iv_finish) {
            Toast.makeText(NimCallActivity.this, "正在停止通话", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    closeSession();
                }
            }, 1000);
            NimCallHelper.getInstance().hangUp();
        } else if (i == R.id.iv_call_hang_up) {
            if (isClosed) {
                findViewById(R.id.iv_call_hang_up).setVisibility(View.GONE);
                return;
            }
            isClosed = true;
            Toast.makeText(NimCallActivity.this, "正在停止通话", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NimCallHelper.getInstance().hangUp();
//                isClosed = true;
//                closeSession();
                }
            }, 2000);
        } else if (i == R.id.tv_call_receive) {
            NimCallHelper.getInstance().receive();
        } else if (i == R.id.tv_call_refuse) {
            NimCallHelper.getInstance().refuse();
        }
    }


    private boolean isClosed = false;


    public void closeSession() {
        if (mIcCallSwitchCamera != null) {
            mIcCallSwitchCamera.setEnabled(false);
        }
        if (mIvCallToggleCamera != null) {
            mIvCallToggleCamera.setEnabled(false);
        }
        if (mIvCallToggleMute != null) {
            mIvCallToggleMute.setEnabled(false);
        }
        if (mTvCallRefuse != null) {
            mTvCallRefuse.setEnabled(false);
        }
        if (mTvCallReceive != null) {
            mTvCallReceive.setEnabled(false);
        }
        if (mIvCallHangUp != null) {
            mIvCallHangUp.setEnabled(false);
        }
        if (mLargeRenderer != null && mLargeRenderer.getParent() != null) {
            ((ViewGroup) mLargeRenderer.getParent()).removeView(mLargeRenderer);
        }
        if (mSmallRenderer != null && mSmallRenderer.getParent() != null) {
            ((ViewGroup) mSmallRenderer.getParent()).removeView(mSmallRenderer);
        }
        mLargeRenderer = null;
        mSmallRenderer = null;
        finish();
    }

    @Override
    public void loadDataSuccess(Object... objects) {

    }

    @Override
    public void loadDataError(Object... objects) {

    }

    @Override
    public void loadDataEmpty() {

    }

    @Override
    public void onNetworkError() {

    }
}