package com.gcml.devices.ecg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothGatt;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.borsam.ble.BorsamConfig;
import com.borsam.borsamnetwork.bean.AddRecordResult;
import com.borsam.borsamnetwork.bean.BorsamResponse;
import com.borsam.borsamnetwork.bean.Config;
import com.borsam.borsamnetwork.bean.LoginResult;
import com.borsam.borsamnetwork.bean.RegisterResult;
import com.borsam.borsamnetwork.bean.UploadFileResult;
import com.borsam.borsamnetwork.http.BorsamHttpUtil;
import com.borsam.borsamnetwork.http.Converter;
import com.borsam.borsamnetwork.http.HttpCallback;
import com.borsam.borsamnetwork.util.PatientApi;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.gcml.devices.BluetoothStore;
import com.gcml.devices.R;
import com.gcml.devices.base.BluetoothHandler;
import com.gcml.devices.base.IBluetoothView;
import com.gcml.devices.dialog.FDialog;
import com.gcml.devices.utils.BluetoothConstants;
import com.gcml.devices.utils.D;
import com.gcml.devices.utils.SPUtil;
import com.gcml.devices.utils.SU;
import com.gcml.devices.utils.T;
import com.gcml.devices.utils.THU;
import com.gcml.devices.utils.TU;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class BoShengECGPresenter implements LifecycleObserver {
    private FragmentActivity activity;
    private IBluetoothView baseView;
    private String name;
    private String address;

    private BleDevice lockedDevice;
    private List<Integer> points;
    private TimeCount timeCount;
    private static boolean isMeasureEnd = false;
    private List<byte[]> bytesResult;
    private BluetoothHandler weakHandler;
    private static final int MESSAGE_DEAL_BYTERESULT = 1;
    private FDialog mLoadingDialog;
    private boolean isLoginBoShengSuccess = false;

    public void showProgressLoading() {
        mLoadingDialog = FDialog.build()
                .setSupportFM(activity.getSupportFragmentManager())
                .setLayoutId(R.layout.dialog_layout_loading)
                .setDimAmount(1)
                .setOutCancel(false)
                .show();
    }

    public void dismissProgressLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    private final Handler.Callback weakRunnable = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_DEAL_BYTERESULT:
                    THU.executeByIo(new THU.SimpleTask<byte[]>() {
                        @Nullable
                        @Override
                        public byte[] doInBackground() {
                            int length_byte = 0;
                            for (int i = 0; i < bytesResult.size(); i++) {
                                length_byte += bytesResult.get(i).length;

                            }
                            byte[] all_byte = new byte[length_byte];
                            int countLength = 0;
                            for (int i = 0; i < bytesResult.size(); i++) {
                                byte[] b = bytesResult.get(i);
                                System.arraycopy(b, 0, all_byte, countLength, b.length);
                                countLength += b.length;
                            }
                            return all_byte;
                        }

                        @Override
                        public void onSuccess(@Nullable byte[] result) {
                            if (result != null) {
                                if (result.length == 0) {
                                    baseView.updateState("数据异常，请清洁仪器后，再次测量");
                                    return;
                                }
                                uploadDatas(result);
                            }
                        }
                    });
                    break;
            }
            return false;
        }
    };
    private Disposable disposable;

    @SuppressLint("RestrictedApi")
    public BoShengECGPresenter(FragmentActivity activity, IBluetoothView baseView, String name, String address, BoShengUserBean userBean) {
        this.activity = activity;
        this.baseView = baseView;
        this.name = name;
        this.address = address;
        this.activity.getLifecycle().addObserver(this);

        initParam();
        initNet();
        getNetConfig(userBean.getUserPhone(), userBean.getUserBirthday(), userBean.getUserName(), userBean.getUserSex());
        connect();

    }

    private void connect() {
        BleManager.getInstance().connect(address, bleGattCallback);
    }

    private final BleGattCallback bleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            lockedDevice = bleDevice;
            baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_connected));
            SPUtil.put(BluetoothConstants.SP.SP_SAVE_ECG, name + "," + address);

            BleManager.getInstance().notify(bleDevice, BorsamConfig.COMMON_RECEIVE_ECG_SUUID.toString(),
                    BorsamConfig.COMMON_RECEIVE_ECG_CUUID.toString(), new BleNotifyCallback() {
                        @Override
                        public void onNotifySuccess() {
                            timeCount.start();
                        }

                        @Override
                        public void onNotifyFailure(BleException exception) {
                        }

                        @Override
                        public void onCharacteristicChanged(byte[] data) {
                            if (!isMeasureEnd) {
                                bytesResult.add(data);
                                baseView.updateData(ByteUtils.byteToString(data));
                            }
                        }
                    });
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            if (baseView instanceof Activity) {
                baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_disconnected));
            } else if (baseView instanceof Fragment) {
                if (((Fragment) baseView).isAdded()) {
                    baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_disconnected));
                }
            }
            isMeasureEnd = true;
            timeCount.cancel();
        }
    };

    private void initNet() {
        BorsamHttpUtil.getInstance().initialization(BluetoothConstants.BoSheng.BoSheng_APP_ID,
                BluetoothConstants.BoSheng.BoSheng_URL, BluetoothConstants.BoSheng.BoSheng_PASSWORD,
                new Converter() {
                    @Override
                    public <T> T converter(String json, Class<T> clazz) {
                        return new Gson().fromJson(json, clazz);
                    }
                });
        BorsamHttpUtil.getInstance().setLogEnable(false);

    }

    private void initParam() {
        bytesResult = new ArrayList<>();
        points = new ArrayList<>();
        weakHandler = new BluetoothHandler(weakRunnable);
        timeCount = new TimeCount(30000, 1000, baseView, weakHandler);

        BleManager.getInstance().init(activity.getApplication());
    }


    private void getNetConfig(final String phone, final String birth, final String name, final String sex) {
        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.getConfig())
                .enqueue(new HttpCallback<BorsamResponse<Config>>() {
                    @Override
                    public void onSuccess(BorsamResponse<Config> configBorsamResponse) {
                        if (configBorsamResponse.getCode() == 0) {
                            //这里必须设置
                            PatientApi.config = configBorsamResponse.getEntity();
                            //注册
                            registAccount(D.hideMobilePhone4(phone),
                                    BluetoothConstants.BoSheng.BoSheng_USER_PASSWORD, birth, name, sex);
                        } else {
                            T.showShort("get config error");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                    }
                });
    }

    private void registAccount(final String username, final String password, final String birth, final String name, final String sex) {
        if (D.isNullString(username) || D.isNullString(password)) {
            return;
        }

        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.register(username, password))
                .enqueue(new HttpCallback<BorsamResponse<RegisterResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<RegisterResult> registerResultBorsamResponse) {
                        if (registerResultBorsamResponse == null) {
                        } else {
                            RegisterResult entity = registerResultBorsamResponse.getEntity();
                            if (entity == null) {
                                //该账号已经注册过
                                login(username, password);
                            } else {
                                //注册成功后进行两个操作：1.登录；2：修改个人信息
                                login(username, password);
                                int birthday = (int) (TU.string2Milliseconds(birth, new SimpleDateFormat("yyyyMMdd")) / 1000);
                                int sexInt = 0;
                                if (sex.equals("男")) {
                                    sexInt = 2;
                                } else if (sex.equals("女")) {
                                    sexInt = 1;
                                }
                                alertPersonInfo(name, "", sexInt, birthday);
                            }
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                    }
                });
    }

    //博声登录
    private void login(String username, String password) {
        if (D.isNullString(username) || D.isNullString(password)) {
            return;
        }
        BorsamHttpUtil.getInstance()
                .add("BoShengECGPresenter", PatientApi.login(username, password)).enqueue(
                new HttpCallback<BorsamResponse<LoginResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<LoginResult> loginResultBorsamResponse) {
                        if (loginResultBorsamResponse.getCode() != 0) {
                            T.showShort(loginResultBorsamResponse.getMsg());
                        } else {
                            PatientApi.userId = loginResultBorsamResponse.getEntity().getUser().getId();
                            PatientApi.token = loginResultBorsamResponse.getEntity().getToken();
                            isLoginBoShengSuccess = true;
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onFailure(int responseCode, String responseMessage) {
                    }
                });
    }

    //修改个人信息 （性别 0:未设置 1:女 2:男 3:其他）
    private void alertPersonInfo(String firstName, String sencondName, int sex, int birthday) {
        BorsamHttpUtil.getInstance()
                .add("BoShengECGPresenter", PatientApi.modifyPatient(firstName, sencondName, sex, birthday))
                .enqueue(new HttpCallback<BorsamResponse>() {
                    @Override
                    public void onSuccess(BorsamResponse borsamResponse) {
                    }

                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onFailure(int i, String s) {
                    }
                });

    }

    //上传数据到博声后台
    private void uploadDatas(byte[] stream) {
        if (!isLoginBoShengSuccess) {
            T.showShort("分析数据失败");
            return;
        }
        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.uploadFile(SU.bytes2InputStream(stream)))
                .enqueue(new HttpCallback<BorsamResponse<UploadFileResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<UploadFileResult> uploadFileResultBorsamResponse) {
                        String file_no = uploadFileResultBorsamResponse.getEntity().getFile_no();
                        addRecord(file_no, (int) (System.currentTimeMillis() / 1000), 1, "");
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        T.showShort("分析数据失败");
                    }
                });
    }

    //增加记录  文件编号、测量时间，设备类型（1.单导，2.双导）、当前健康状况
    private void addRecord(final String fileNo, int testTime, int type, String condition) {
        BorsamHttpUtil.getInstance().add("BoShengECGPresenter", PatientApi.addRecord(fileNo, testTime, type, condition))
                .enqueue(new HttpCallback<BorsamResponse<AddRecordResult>>() {
                    @Override
                    public void onSuccess(BorsamResponse<AddRecordResult> addRecordResultBorsamResponse) {
                        AddRecordResult entity = addRecordResultBorsamResponse.getEntity();
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        BoShengResultBean boShengResultBean = new Gson().fromJson(entity.getExt(), BoShengResultBean.class);
                        baseView.updateData(fileNo, entity.getFile_report(), boShengResultBean.getStop_light() + "", boShengResultBean.getFindings(), boShengResultBean.getAvgbeats().get(0).getHR() + "");
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        baseView.updateData(fileNo, null, null);
                    }
                });

    }


    @SuppressLint("RestrictedApi")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (lockedDevice != null) {
            BleManager.getInstance().stopNotify(lockedDevice, BorsamConfig.COMMON_RECEIVE_ECG_SUUID.toString(),
                    BorsamConfig.COMMON_RECEIVE_ECG_CUUID.toString());
        }
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        isMeasureEnd = false;
        if (weakHandler != null) {
            weakHandler.removeCallbacksAndMessages(null);
            weakHandler = null;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = null;

        if (activity != null) {
            activity.getLifecycle().removeObserver(this);
        }
        activity = null;
    }

    class TimeCount extends CountDownTimer {
        private IBluetoothView fragment;
        private BluetoothHandler weakHandler;

        TimeCount(long millisInFuture, long countDownInterval, IBluetoothView fragment, BluetoothHandler weakHandler) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.fragment = fragment;
            this.weakHandler = weakHandler;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            isMeasureEnd = true;
            fragment.updateData("tip", "测量结束");
            showProgressLoading();
            weakHandler.sendEmptyMessage(MESSAGE_DEAL_BYTERESULT);
        }


        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            fragment.updateData("tip", "距离测量结束还有" + millisUntilFinished / 1000 + "s");
        }
    }
}
