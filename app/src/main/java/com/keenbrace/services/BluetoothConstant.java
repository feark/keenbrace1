package com.keenbrace.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;


public class BluetoothConstant {
    public static final String SCALE_TYPE = "bf";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    public static int recReciveCount = 0;
    public static BluetoothLeService mBluetoothLeService;
    public static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothDevice mdevice;
    public static boolean mConnected = false;

    public static List<String> deviceMacs = new ArrayList<String>();

    public static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    public static final long SCAN_PERIOD = 5000;

    public final String LIST_NAME = "NAME";
    public final String LIST_UUID = "UUID";

    public static String mDeviceAddress = null;
    public static BluetoothGattCharacteristic mwriteCharacteristic = null;// send message ble charaacteristic
    public static BluetoothGattCharacteristic mreadCharacteristic = null;// send message ble charaacteristic

    public static final String BLESERVICE_ADDRESS_HEAD = "0000ffe"; //ble service address
    public static final String BLCHARACTERISTIC_WRITE_ADDRESS_HEAD = "0000ffe3"; //ble receive address
    public static final String BLCHARACTERISTIC_READ_ADDRESS_HEAD = "0000ffe4";    //ble send address

    public static final UUID BLCHARACTERISTIC_SEND_ADDRESS = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public final static String ACTION_USERBIND_SUCCESS = "com.health.user.bind.success";
    public final static String ACTION_USERDELETE_SUCCESS = "com.health.user.delete.success";
    public final static String ACTION_USERUPDATE_SUCCESS = "com.health.user.update.success";
    public final static String EXTRA_DATA = "com.health.bluetooth.le.EXTRA_DATA";
    public static java.util.HashMap<String, Integer> devRssiValues = new java.util.HashMap<String, Integer>();
}
