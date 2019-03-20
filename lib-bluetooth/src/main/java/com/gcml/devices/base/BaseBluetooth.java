package com.gcml.devices.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothDevice;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.SupportActivity;
import android.text.TextUtils;

import com.gcml.devices.BluetoothStore;
import com.gcml.devices.R;
import com.gcml.devices.utils.BluetoothConstants;
import com.gcml.devices.utils.Gol;
import com.gcml.devices.utils.T;
import com.gzq.lib_core.bean.BluetoothParams;
import com.gzq.lib_core.utils.KVUtils;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseBluetooth implements LifecycleObserver {
    private final BluetoothBean brandMenu;
    private boolean isOnSearching = false;
    private boolean isOnDestroy = false;
    private boolean isConnected = false;
    private SupportActivity activity;
    private ConnectListener connectListener;
    private SearchListener searchListener;
    protected IBluetoothView baseView;
    /**
     * 每次搜索的时间
     */
    private static final int PER_SEARCH_TIME = 5000;
    /**
     * 总共搜索的次数
     */
    private static final String TAG = "BaseBluetooth";
    private static final int NUMBER_SEARCHED = 6;
    private BluetoothSearchHelper searchHelper;
    private BluetoothConnectHelper connectHelper;
    protected String targetName = null;
    protected String targetAddress = null;

    @SuppressLint("RestrictedApi")
    public BaseBluetooth(IBluetoothView owner, BluetoothBean brandMenu) {
        this.baseView = owner;
        this.brandMenu = brandMenu;
        if (brandMenu == null) throw new NullPointerException();
        if (owner instanceof Activity) {
            activity = ((SupportActivity) owner);
        } else if (owner instanceof Fragment) {
            activity = ((Fragment) owner).getActivity();
        }
        this.activity.getLifecycle().addObserver(this);

        BluetoothBean sp = KVUtils.getEntity(BluetoothParams.KEY_DEVICE_HAS_BAND, BluetoothBean.class);
        if (sp != null) {
            targetName = sp.getBluetoothName();
            targetAddress = sp.getBluetoothAddress();
        }
    }

    /**
     * 进行搜索
     *
     * @param address
     */
    public void startDiscovery(String address) {
        if (!isOnSearching) {
            Set<String> strings = obtainBrands().keySet();
            start(BluetoothType.BLUETOOTH_TYPE_BLE, address, strings.toArray(new String[strings.size()]));
        }
    }

    /**
     * 直接连接
     *
     * @param device
     */
    public void startConnect(final BluetoothDevice device) {
        if (isOnSearching()) {
            stopSearch();
        }
        if (isConnected && !TextUtils.isEmpty(targetAddress)) {
            //如果是已经和其他设备连接，则先断开已有连接，1秒以后再和该设备连接
            BluetoothStore.getClient().disconnect(targetAddress);
            new BluetoothHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startDiscovery(device.getAddress());
                }
            }, 1000);
        } else {
            startDiscovery(device.getAddress());
        }
    }

    /**
     * 停止搜索
     */
    public void stopDiscovery() {
        if (isOnSearching && searchHelper != null) {
            searchHelper.stop();
        }
    }

    public void start(final BluetoothType type, final String mac, final String... names) {
        if (activity == null) {
            throw new IllegalArgumentException("activity==null");
        }
        RxPermissions rxPermissions = new RxPermissions(activity);
        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            rxPermissions
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .observeOn(Schedulers.newThread())
                    .subscribe(new DefaultObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                doAccept(type, mac, names);
                            } else {
                                doRefuse();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            doAccept(type, mac, names);
        }

    }

    private void doRefuse() {
        T.showLong("操作蓝牙需要打开蓝牙权限");
    }

    @WorkerThread
    private void doAccept(BluetoothType type, String mac, String[] names) {
        if (!BluetoothUtils.isBluetoothEnabled()) {
            BluetoothUtils.openBluetooth();
            SystemClock.sleep(2000);
        }
        if (!BluetoothUtils.isBluetoothEnabled()) {
            T.showLong("蓝牙未打开或者不支持蓝牙");
            return;
        }
        if (TextUtils.isEmpty(mac) && (names == null || names.length == 0)) {
            throw new IllegalArgumentException("params is abnormal");
        }
        if (connectHelper == null) {
            connectHelper = new BluetoothConnectHelper();
        }
        if (!TextUtils.isEmpty(mac)) {
            if (isSelfConnect(targetName, mac)) {
                return;
            }
            connect(mac);
            return;
        }
        if (searchHelper == null) {
            searchHelper = new BluetoothSearchHelper();
        }

        if (searchListener == null) {
            searchListener = new MySearchListener();
        }

        if (type == BluetoothType.BLUETOOTH_TYPE_CLASSIC) {
            searchHelper.searchClassic(PER_SEARCH_TIME, NUMBER_SEARCHED, searchListener, names);
        } else {
            searchHelper.searchBle(PER_SEARCH_TIME, NUMBER_SEARCHED, searchListener, names);
        }
    }

    protected void connect(String mac) {
        if (connectListener == null) {
            connectListener = new MyConnectListener();
        }
        if (!isOnDestroy && connectHelper != null) {
            connectHelper.connect(mac, connectListener);
        }
    }

    protected void stopSearch() {
        if (searchHelper != null) {
            searchHelper.stop();
        }
    }

    class MySearchListener implements SearchListener {
        private BluetoothDevice device;

        @Override
        public void onSearching(boolean isOn) {
            isOnSearching = isOn;
        }

        @Override
        public void onNewDeviceFinded(BluetoothDevice newDevice) {
            newDeviceFinded(newDevice);
        }

        @Override
        public void obtainDevice(BluetoothDevice device) {
            this.device = device;
            obtainTargetDevicede(this.device);
            if (isSelfConnect(device.getName(), device.getAddress())) {
                return;
            }
            connect(this.device.getAddress());
        }

        @Override
        public void noneFind() {
            BaseBluetooth.this.noneFind();
        }
    }

    class MyConnectListener implements ConnectListener {


        @Override
        public void success(BluetoothDevice device) {
            isConnected = true;
            if (!TextUtils.isEmpty(device.getName())) {
                targetName = device.getName();
            }
            targetAddress = device.getAddress();
            //本地缓存
            saveSP(targetName, targetAddress);
            //存入全局变量
            BindDeviceBean bindDeviceBean = new BindDeviceBean();
            bindDeviceBean.setBluetoothName(targetName);
            bindDeviceBean.setBluetoothMac(targetAddress);
            HashMap<String, String> brands = obtainBrands();
            if (brands != null) {
                bindDeviceBean.setBluetoothBrand(brands.get(targetName));
            }
            BluetoothStore.bindDevice.postValue(bindDeviceBean);
            if (baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_connected));
            }
            if (baseView instanceof FragmentActivity) {
                baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_connected));
            }
            connectSuccessed(targetName, targetAddress);
        }

        @Override
        public void failed() {
            isConnected = false;
            if (baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_connect_fail));
            }
            if (baseView instanceof FragmentActivity) {
                baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_connect_fail));
            }
            connectFailed();
        }

        @Override
        public void disConnect(String address) {
            isConnected = false;
            if (baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_disconnected));
            }
            if (baseView instanceof FragmentActivity) {
                baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_disconnected));
            }
            disConnected(address);
            //3秒之后尝试重连
            new BluetoothHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isOnDestroy && targetAddress != null) {
                        connect(targetAddress);
                    }
                }
            }, 3000);
        }

    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(LifecycleOwner owner) {

    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(LifecycleOwner owner) {
        if (isOnSearching) {
            Gol.i("BaseBluetooth>>>>====>>>isOnSearching==" + isOnSearching);
            isOnSearching = false;
            if (searchHelper != null) {
                Gol.i("BaseBluetooth>>>>==searchHelper:" + searchHelper + "==>>>searchHelper.clear();");
                searchHelper.clear();
            } else {
                BluetoothStore.getClient().stopSearch();
                Gol.e("BaseBluetooth>>>>======>>>>searchHelper.clear() has not carry out");
            }
        }
        //Fragment中使用需要提前释放部分资源，因为Fragment走到onDestroy的时机很晚
        if (owner instanceof Fragment) {
            if (connectHelper != null) {
                connectHelper.clear();
            }
            connectHelper = null;
            connectListener = null;
            searchListener = null;
        }
    }

    @SuppressLint("RestrictedApi")
    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        Gol.i("BaseBluetooth" + BaseBluetooth.this + ">>>>======>>>>onDestroy");
        isOnDestroy = true;
        BluetoothStore.getClient().stopSearch();
        searchHelper = null;
        if (connectHelper != null) {
            connectHelper.clear();
        }
        connectHelper = null;
        if (activity != null) {
            activity.getLifecycle().removeObserver(this);
        }
        activity = null;
        connectListener = null;
        searchListener = null;

    }

    public boolean isOnSearching() {
        return isOnSearching;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public SupportActivity getActivity() {
        return activity;
    }

    @CallSuper
    protected synchronized void newDeviceFinded(BluetoothDevice device) {
        boolean isOurDevices = false;
        Iterator<String> iterator = DeviceBrand.BLOODOXYGEN.keySet().iterator();
        while (iterator.hasNext()) {
            if (TextUtils.isEmpty(device.getName())) {
                break;
            }
            if (iterator.next().contains(device.getName())) {
                isOurDevices = true;
            }
        }
        if (isOurDevices) {
            baseView.discoveryNewDevice(device);
        }
    }

    @CallSuper
    protected void noneFind() {
        if (baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
            baseView.updateState(BluetoothStore.getString(R.string.unfind_devices));
        }
    }

    protected void obtainTargetDevicede(BluetoothDevice device) {
    }

    protected boolean isSelfConnect(String name, String address) {
        return false;
    }

    protected abstract void connectSuccessed(String name, String address);

    protected abstract void connectFailed();

    protected abstract void disConnected(String address);

    protected void saveSP(String bluetoothName, String bluetoothAddress) {
        BluetoothBean bluetoothBean = new BluetoothBean();
        bluetoothBean.setDeviceType(this.brandMenu.getDeviceType());
        bluetoothBean.setBluetoothAddress(bluetoothAddress);
        bluetoothBean.setBluetoothName(bluetoothName);
        bluetoothBean.setDeviceType(this.brandMenu.getDeviceType());
        switch (brandMenu.getDeviceType()) {
            case BluetoothParams.TYPE_BLOODPRESSURE:
                KVUtils.putEntity(BluetoothConstants.SP.SP_SAVE_BLOODPRESSURE, brandMenu);
                break;
            case BluetoothParams.TYPE_BLOODOXYGEN:
                KVUtils.putEntity(BluetoothConstants.SP.SP_SAVE_BLOODOXYGEN, brandMenu);
                break;
            case BluetoothParams.TYPE_BLOODSUGAR:
                KVUtils.putEntity(BluetoothConstants.SP.SP_SAVE_BLOODSUGAR, brandMenu);
                break;
            case BluetoothParams.TYPE_ECG:
                KVUtils.putEntity(BluetoothConstants.SP.SP_SAVE_ECG, brandMenu);
                break;
            case BluetoothParams.TYPE_TEMPERATURE:
                KVUtils.putEntity(BluetoothConstants.SP.SP_SAVE_TEMPERATURE, brandMenu);
                break;
            case BluetoothParams.TYPE_THREE:
                KVUtils.putEntity(BluetoothConstants.SP.SP_SAVE_THREE_IN_ONE, brandMenu);
                break;
            case BluetoothParams.TYPE_WEIGHT:
                KVUtils.putEntity(BluetoothConstants.SP.SP_SAVE_WEIGHT, brandMenu);
                break;
        }

    }

    protected abstract HashMap<String, String> obtainBrands();
}
