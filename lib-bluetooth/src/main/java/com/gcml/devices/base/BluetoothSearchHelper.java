package com.gcml.devices.base;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.gcml.devices.BluetoothStore;
import com.gcml.devices.utils.Gol;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

/**
 * 根据蓝牙名字搜索蓝牙
 */
public class BluetoothSearchHelper {
    private MySearch search;
    private SearchListener searchListener;
    private boolean isOnSearching = false;
    private boolean isClear = false;
    private long searchTime = 0L;
    private boolean isFindOne = false;

    @SuppressLint("CheckResult")
    public void searchClassic(int periodMill, int times, SearchListener listener, String... names) {
        searchListener = listener;
        final SearchRequest request = new SearchRequest
                .Builder()
                .searchBluetoothClassicDevice(periodMill, times)
                .build();
        if (search == null) {
            search = new MySearch(names);
        }
        isOnSearching = true;
        BluetoothStore.getClient().search(request, search);
    }

    public void searchBle(int periodMill, int times, SearchListener listener, String... names) {
        searchListener = listener;
        SearchRequest request = new SearchRequest
                .Builder()
                .searchBluetoothLeDevice(periodMill, times)
                .build();
        if (search == null) {
            search = new MySearch(names);
        }
        isOnSearching = true;
        BluetoothStore.getClient().search(request, search);
    }

    class MySearch implements SearchResponse {
        private String[] bleNames;

        public MySearch(String[] names) {
            bleNames = names;
        }

        @Override
        public void onSearchStarted() {
            searchTime = System.currentTimeMillis();
            synchronized (BluetoothSearchHelper.this) {
                if (isClear) {
                    BluetoothStore.getClient().stopSearch();
                    return;
                }
            }
            Gol.i("BaseBluetooth>>>>>>=====>>>bluetooth start>>>>");
            isOnSearching = true;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(true);
            }
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            Log.i("BluetoothSearching", device.getName() + ">>>======>>>>" + device.getAddress());
            if (!isClear) {
                if (searchListener != null) {
                    searchListener.onNewDeviceFinded(device.device);
                }
                synchronized (BluetoothSearchHelper.this) {
                    for (String name : bleNames) {
                        String deviceName = device.getName();
                        if (TextUtils.isEmpty(deviceName)) {
                            break;
                        }
                        if (searchListener != null && deviceName.contains(name)) {
                            isFindOne = true;
                            searchListener.obtainDevice(device.device);
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void onSearchStopped() {
            Gol.i(">>>>>=======>>>>bluetooth stopped>>>Thread:" + Thread.currentThread().getName());
            Gol.i("bluetooth searched " + (System.currentTimeMillis() - searchTime) + " millisecond");
            searchTime = 0L;
            isOnSearching = false;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(false);
            }
            if (searchListener != null && !isClear && !isFindOne) {
                searchListener.noneFind();
            }
        }

        @Override
        public void onSearchCanceled() {
            Gol.i(">>>>>=======>>>>bluetooth canceled");
            Gol.i("bluetooth searched " + (System.currentTimeMillis() - searchTime) + " millisecond");
            searchTime = 0L;
            isOnSearching = false;
            if (searchListener != null && !isClear) {
                searchListener.onSearching(false);
            }
        }
    }

    public synchronized void stop() {
        Gol.i("BluetoothSearchHelper>>>>===>>>stop");
        isOnSearching = false;
        BluetoothStore.getClient().stopSearch();
        isFindOne = false;
    }

    public synchronized void clear() {
        Gol.i("BluetoothSearchHelper>>>>===>>>clear");
        isClear = true;
        isOnSearching = false;
        BluetoothStore.getClient().stopSearch();
        isFindOne = false;
        searchListener = null;
        search = null;
    }
}
