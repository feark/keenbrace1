/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keenbrace.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.keenbrace.bean.Constant;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.util.SharePreferUtil;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;

    public String getmBluetoothDeviceAddress() {
        return mBluetoothDeviceAddress;
    }

    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    public int getmConnectionState() {
        return mConnectionState;
    }

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public final static String BLEAPI_GATT_FOUNDDEVICE = "com.bleapi.BLEAPI_GATT_FOUNDDEVICE";
    public final static String ACTION_GATT_CONNECTED = "com.keenbrace.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.keenbrace.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.keenbrace.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.keenbrace.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.keenbrace.EXTRA_DATA";
    public final static String EXTRA_DATA_BYTE = "com.keenbrace.EXTRA_DATA_BYTE";
    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
            .fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            scanLeDevice(false);
            scanLeDevice(true);
            handler.postDelayed(this, 1000 * 5);
        }
    };
    private boolean mScanning;

    public void scanLeDevice(final boolean enable) {
        if (enable) {
            if (mScanning == false) {
                Log.e(TAG, "BEGIN scane devices scanLeDevice:=");
                mScanning = true;

                // Toast.makeText(BluetoothLeService.this,
                // "DEBUGMSG:START SEARCH DEVICE", Toast.LENGTH_SHORT).show();
                /*
				 * UUID u1 =
				 * UUID.fromString("8728d000-f000-8509-a000-000000000000");
				 * final UUID[] myUUID = { u1 };
				 */
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else {
            if (mScanning == true) {
                mScanning = false;

                // Toast.makeText(BluetoothLeService.this,
                // "DEBUGMSG:END SEARCH DEVICE", Toast.LENGTH_SHORT).show();
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
        // invalidateOptionsMenu();
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {

            //Log.e(TAG, "get device name:=" + device.getName());

            if (null != device.getName() && !"".equals(device.getName()) && device.getName().contains("KeenBrace_Sports")) {
                final Intent intentAction = new Intent(
                        BluetoothLeService.BLEAPI_GATT_FOUNDDEVICE);
                intentAction.putExtra("device", device);
                intentAction.putExtra("macaddress", device.getAddress());
                BluetoothConstant.devRssiValues.put(device.getAddress(), rssi);
                sendBroadcast(intentAction);
            }
        }
    };

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                BluetoothConstant.mConnected = true;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:"
                        + mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                BluetoothConstant.mConnected = false;
                broadcastUpdate(intentAction);
                BluetoothConstant.mdevice = null;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            BluetoothGattCharacteristic readCharacteristic = null;
            BluetoothGattCharacteristic writeCharacteristic = null;

            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> serviceList = gatt.getServices();
                if (!serviceList.isEmpty()) {
                    for (Iterator<BluetoothGattService> iter = serviceList
                            .iterator(); iter.hasNext(); ) {
                        BluetoothGattService service = iter.next();
                        String uuid = service.getUuid().toString();
                        if (uuid.startsWith(BluetoothConstant.BLESERVICE_ADDRESS_HEAD)) {
                            List<BluetoothGattCharacteristic> charList = service
                                    .getCharacteristics();
                            if (!charList.isEmpty()) {
                                for (Iterator<BluetoothGattCharacteristic> it = charList
                                        .iterator(); it.hasNext(); ) {
                                    BluetoothGattCharacteristic cha = it.next();
                                    uuid = cha.getUuid().toString();
                                    if (uuid.startsWith(BluetoothConstant.BLCHARACTERISTIC_WRITE_ADDRESS_HEAD)) {
                                        writeCharacteristic = cha;
                                    } else if (uuid
                                            .startsWith(BluetoothConstant.BLCHARACTERISTIC_READ_ADDRESS_HEAD)) {

                                        readCharacteristic = cha;
                                        setCharacteristicNotification(
                                                readCharacteristic, true);
                                        List<BluetoothGattDescriptor> dds = readCharacteristic
                                                .getDescriptors();

                                        if (null != readCharacteristic) {
                                            BluetoothGattDescriptor descriptor = readCharacteristic
                                                    .getDescriptor(BluetoothConstant.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
                                            if (null != descriptor) {
                                                if (descriptor
                                                        .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
                                                    writeDescriptor(descriptor);
                                                }

                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (writeCharacteristic != null && UtilConstants.user != null) {

                int sex = 0;
                if ("male".equals(UtilConstants.user.getGender())) {
                    sex = 0;
                } else
                    sex = 1;
                int w = Constant.user.getWeight();
                int h =Constant.user.getHeight();
                String hex = "51" + format(sex) + format(w) + format(h);
                writeCharacteristic.setValue(toByte(hex));
                if (gatt.writeCharacteristic(writeCharacteristic)) {

                }
            }
            BluetoothConstant.mreadCharacteristic = readCharacteristic;
            BluetoothConstant.mwriteCharacteristic = writeCharacteristic;
            broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
        }

        public String format(int n) {
            String str = Integer.toHexString(n);
            int l = str.length();
            if (l == 1)
                return "0" + str;
            else
                return str.substring(l - 2, l);
        }

        public byte[] toByte(String hexString) {
            int len = hexString.length() / 2;
            byte[] result = new byte[len];
            for (int i = 0; i < len; i++)
                result[i] = Integer.valueOf(
                        hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
            return result;
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);

                if (BluetoothConstant.mreadCharacteristic != null
                        && characteristic == BluetoothConstant.mreadCharacteristic) {
                    setCharacteristicNotification(
                            BluetoothConstant.mreadCharacteristic, true);

                    if (null != BluetoothConstant.mreadCharacteristic) {
                        BluetoothGattDescriptor descriptor = BluetoothConstant.mreadCharacteristic
                                .getDescriptor(BluetoothConstant.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
                        if (null != descriptor) {
                            if (descriptor
                                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
                                writeDescriptor(descriptor);
                            }

                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(
                        data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                // intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
                // stringBuilder.toString());
                Log.e(TAG, stringBuilder.toString());

                intent.putExtra(EXTRA_DATA, stringBuilder.toString());
            }
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            // intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(
                        data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n"
                        + stringBuilder.toString());
            }
        }

        intent.putExtra(EXTRA_DATA_BYTE, characteristic.getValue());
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        scanLeDevice(false);
        scanLeDevice(true);
        handler.postDelayed(runnable, 1000 * 5);
        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The
     * connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG,
                    "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        if (mBluetoothDeviceAddress != null
                && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG,
                    "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter
                .getRemoteDevice(address);
        BluetoothConstant.mdevice = device;
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        disconnect();
        close();
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        BluetoothConstant.mDeviceAddress = address;

        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        BluetoothConstant.mdevice = null;
        BluetoothConstant.mConnected = false;

    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
        BluetoothConstant.mdevice = null;
        BluetoothConstant.mConnected = false;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification. False otherwise.
     */
    public void setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic
                    .getDescriptor(UUID
                            .fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor
                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }

    public void writeDescriptor(BluetoothGattDescriptor descriptor) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "writeDescriptor()--> BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.writeDescriptor(descriptor);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public void startRun(int t, byte sport_type, Date d) {

        byte[] sends = new byte[9];

        sends[0] = 0x55;
        if (t == 1) {
            sends[1] = 0x01;
        }
        else {
            sends[1] = 0x00;
        }

        sends[2] = (byte) (d.getYear() - 100);
        sends[3] = (byte) d.getMonth();
        sends[4] = (byte) d.getDay();
        sends[5] = (byte) d.getHours();
        sends[6] = (byte) d.getMinutes();
        sends[7] = (byte) d.getSeconds();

        sends[8] = sport_type;

        BluetoothConstant.mwriteCharacteristic.setValue(sends);
        writeCharacteristic(BluetoothConstant.mwriteCharacteristic);
        BluetoothGattDescriptor descriptor = BluetoothConstant.mreadCharacteristic
                .getDescriptor(BluetoothConstant.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
        if (null != descriptor) {
            if (descriptor
                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
                writeDescriptor(descriptor);
            }
        }
    }

    public void RequestHistoryData(long times) {
        byte[] sends = new byte[8];

        sends[0] = 0x56;

        sends[1] = 0x01;

        Date d = new Date();
        d.setTime(times);
        sends[2] = (byte) d.getHours();
        sends[3] = (byte) d.getMinutes();
        sends[4] = (byte) d.getSeconds();
        BluetoothConstant.mwriteCharacteristic.setValue(sends);
        writeCharacteristic(BluetoothConstant.mwriteCharacteristic);
        BluetoothGattDescriptor descriptor = BluetoothConstant.mreadCharacteristic
                .getDescriptor(BluetoothConstant.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
        if (null != descriptor) {
            if (descriptor
                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
                writeDescriptor(descriptor);
            }
        }
    }

    @Override
    public void onDestroy() {
        scanLeDevice(false);
    }
}
