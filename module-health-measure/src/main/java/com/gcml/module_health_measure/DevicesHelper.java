package com.gcml.module_health_measure;

import com.gcml.devices.base.BluetoothBean;
import com.gcml.devices.utils.BluetoothConstants;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.bean.BluetoothParams;

import java.util.ArrayList;
import java.util.List;

public class DevicesHelper {
    public static List<BluetoothBean> bloodpressure() {
        List<BluetoothBean> brandMenus = new ArrayList<>();
        BluetoothBean band = KVUtils.getEntity(BluetoothConstants.SP.SP_SAVE_BLOODPRESSURE, BluetoothBean.class);
        BluetoothBean yuyue = new BluetoothBean();
        BluetoothBean self = new BluetoothBean();


        yuyue.setName("鱼跃血压计");
        yuyue.setImage(R.drawable.ic_bloodpressure_yuyue);
        yuyue.setDeviceType(BluetoothParams.TYPE_BLOODPRESSURE);
        yuyue.setBluetoothName("Yuwell");

        self.setName("自家血压计");
        self.setImage(R.drawable.ic_bloodpressure_self);
        self.setDeviceType(BluetoothParams.TYPE_BLOODPRESSURE);
        self.setBluetoothName("eBlood-Pressure");

        if (band == null) {
            yuyue.setBluetoothAddress(null);
            self.setBluetoothAddress(null);
        } else {
            boolean yuwellBand = band.getBluetoothName().startsWith("Yuwell");
            boolean selfBand = band.getBluetoothName().startsWith("eBlood");
            yuyue.setBand(yuwellBand);
            self.setBand(selfBand);
            yuyue.setBluetoothAddress(band.getBluetoothAddress());
            self.setBluetoothAddress(band.getBluetoothAddress());

        }
        brandMenus.add(yuyue);
        brandMenus.add(self);
        return brandMenus;
    }
}
