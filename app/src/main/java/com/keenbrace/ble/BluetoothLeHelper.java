package com.keenbrace.ble;

import java.util.UUID;
import java.util.List;
import java.util.Queue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.content.Intent;
import android.content.Context;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattCharacteristic;


@SuppressLint("NewApi")
public class BluetoothLeHelper {
    private final static String TAG = BluetoothLeHelper.class.getSimpleName();

    private final static String Service_UUID_Begin = "0000fff0";
    private final static String Command_UUID_Begin = "0000fff1";
    private final static String Notification_UUID_Begin = "0000fff4";
    private final static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    public final static String Key_Conn_Stage = "Conn_Stage";

    private final static int Op_Stage_None = 0;
    private final static int Op_Stage_Conn = 1;
    private final static int Op_Stage_Service = 2;
    private final static int Op_Stage_Beep = 3;
    private final static int Op_Stage_ReadROM = 4;
    private final static int Op_Stage_Speed = 5;
    private final static int Op_Stage_OpenAD = 6;
    private final static int Op_Stage_Transfer = 7;

    private static BluetoothLeHelper helper;

    public BluetoothLeHelper(Context context, BluetoothManager bleManager, BluetoothAdapter bleAdapter) {
        m_Context = context;
        m_BluetoothAdapter = bleAdapter;
        m_BluetoothManager = bleManager;
        m_CmdQueue = new LinkedList<Pair<DeviceCommand, DeviceApiOpListener>>();
    }

    public static void init(Context context, BluetoothManager bleManager, BluetoothAdapter bleAdapter) {
        helper = new BluetoothLeHelper(context, bleManager, bleAdapter);
    }

    public static BluetoothLeHelper getBluetoothLeHelper() {
        return helper;
    }

    // all these callback called by framework native thread
    private final BluetoothGattCallback m_GattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            boolean bConn = false;

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery.");

                if (gatt.discoverServices()) {
                    bConn = true;
                    m_OpStage = Op_Stage_Service;
                }
            }

            if (!bConn) {
                Log.i(TAG, "Disconnected from GATT server.");

            }

            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            boolean bRet = true;

            switch (m_OpStage) {
                case Op_Stage_Beep:
                    if ((m_WriteCharacteristic == null) || (m_ReadCharacteristic == null) || (status != BluetoothGatt.GATT_SUCCESS)) {
                        bRet = false;
                    } else {
                        bRet = gatt.setCharacteristicNotification(m_ReadCharacteristic, true);
                        BluetoothGattDescriptor desc = m_ReadCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
                        if (bRet)
                            bRet = desc.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                        if (bRet) bRet = gatt.writeDescriptor(desc);
                    }
                    break;
                case Op_Stage_ReadROM:
                    if ((m_WriteCharacteristic == null) || (m_ReadCharacteristic == null) || (status != BluetoothGatt.GATT_SUCCESS)) {
                        bRet = false;
                    }
                    break;
                case Op_Stage_Speed:
                    if ((m_WriteCharacteristic == null) || (m_ReadCharacteristic == null) || (status != BluetoothGatt.GATT_SUCCESS)) {
                        bRet = false;
                    } else {
                        bRet = m_WriteCharacteristic.setValue(DeviceCommand.getCmdCode(DeviceCommand.DevComm_OpenAD), BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                        if (bRet) bRet = gatt.writeCharacteristic(m_WriteCharacteristic);
                        if (bRet) m_OpStage = Op_Stage_OpenAD;
                    }
                    break;
                case Op_Stage_OpenAD:
                    if ((m_WriteCharacteristic == null) || (m_ReadCharacteristic == null) || (status != BluetoothGatt.GATT_SUCCESS)) {
                        bRet = false;
                    } else {
                        m_bConnected = true;
                        m_OpStage = Op_Stage_Transfer;
                    }
                    break;
                case Op_Stage_Transfer:

                    Pair<DeviceCommand, DeviceApiOpListener> cmdPair = null;

                    synchronized (m_QueueCritObj) {
                        cmdPair = m_CmdQueue.poll();
                        if ((cmdPair != null) && (cmdPair.second != null)) {
                            cmdPair.second.onCommandCompleted(cmdPair.first, status == BluetoothGatt.GATT_SUCCESS);
                        }

                        while (!m_CmdQueue.isEmpty()) {
                            cmdPair = m_CmdQueue.peek();
                            if (cmdPair == null) {
                                break;
                            }

                            int code = DeviceCommand.getCmdCode(cmdPair.first);
                            if ((m_WriteCharacteristic.setValue(code, BluetoothGattCharacteristic.FORMAT_UINT8, 0)) ||
                                    (gatt.writeCharacteristic(m_WriteCharacteristic))) {
                                break;
                            }

                            cmdPair = m_CmdQueue.poll();
                            if (cmdPair.second != null) {
                                cmdPair.second.onCommandCompleted(cmdPair.first, false);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            boolean bRet = true;

            switch (m_OpStage) {
                case Op_Stage_Beep:
                    if ((m_WriteCharacteristic == null) || (m_ReadCharacteristic == null)) {
                        bRet = false;
                    }
                    if (bRet)
                        bRet = m_WriteCharacteristic.setValue(DeviceCommand.getCmdCode(DeviceCommand.DevComm_ReadRomVer), BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                    if (bRet) bRet = gatt.writeCharacteristic(m_WriteCharacteristic);
                    if (bRet) {
                        m_OpStage = Op_Stage_ReadROM;
                    }
                    break;
                default:
                    break;
            }


            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            BluetoothGattCharacteristic readCharacteristic = null;
            BluetoothGattCharacteristic writeCharacteristic = null;

            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> serviceList = gatt.getServices();
                if (!serviceList.isEmpty()) {
                    for (Iterator<BluetoothGattService> iter = serviceList.iterator(); iter.hasNext(); ) {
                        BluetoothGattService service = iter.next();
                        String uuid = service.getUuid().toString();
                        if (uuid.startsWith(Service_UUID_Begin)) {
                            List<BluetoothGattCharacteristic> charList = service.getCharacteristics();
                            if (!charList.isEmpty()) {
                                for (Iterator<BluetoothGattCharacteristic> it = charList.iterator(); it.hasNext(); ) {
                                    BluetoothGattCharacteristic cha = it.next();
                                    uuid = cha.getUuid().toString();
                                    if (uuid.startsWith(Command_UUID_Begin)) {
                                        writeCharacteristic = cha;
                                    } else if (uuid.startsWith(Notification_UUID_Begin)) {
                                        readCharacteristic = cha;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if ((readCharacteristic != null) || (writeCharacteristic != null)) {
                m_ReadCharacteristic = readCharacteristic;
                m_WriteCharacteristic = writeCharacteristic;

                writeCharacteristic.setValue(DeviceCommand.getCmdCode(DeviceCommand.DevComm_Beep), BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                if (gatt.writeCharacteristic(writeCharacteristic)) {
                    m_OpStage = Op_Stage_Beep;
                    return;
                }
                m_ReadCharacteristic = null;
                m_WriteCharacteristic = null;
            }


        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

            byte[] data = characteristic.getValue();

            m_WriteCharacteristic.setValue(DeviceCommand.getCmdCode(DeviceCommand.DevComm_Int7MS), BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            if (gatt.writeCharacteristic(m_WriteCharacteristic)) {
                boolean bConn = true;
                m_OpStage = Op_Stage_Speed;
            }

        }

    };

    private int m_OpStage;


    private boolean m_bConnected;
    private String m_BluetoothAddr;

    private Context m_Context;
    private BluetoothGatt m_BLeGatt;
    private BluetoothAdapter m_BluetoothAdapter;
    private BluetoothManager m_BluetoothManager;

    private final Object m_QueueCritObj = new Object();
    private Queue<Pair<DeviceCommand, DeviceApiOpListener>> m_CmdQueue;

    private BluetoothGattCharacteristic m_ReadCharacteristic;
    private BluetoothGattCharacteristic m_WriteCharacteristic;

    private boolean checkROMVer() {
        return false;
    }

    private static String getConnStageDesc(int stage) {
        switch (stage) {
            case Op_Stage_None:
                return "None";
            case Op_Stage_Conn:
                return "Connecting";
            case Op_Stage_Service:
                return "Retrieving Service";
            case Op_Stage_Beep:
                return "Beeping";
            case Op_Stage_ReadROM:
                return "Reading ROM";
            case Op_Stage_Speed:
                return "Adjusting Speed";
            case Op_Stage_Transfer:
                return "ECG Transfering";
            default:
                return "Unknown";
        }
    }


    private byte calculateCheckSum(byte[] data, int offset, int end) {
        byte checksum = 0;
        for (int i = offset; i < end; ++i) {
            checksum += (data[i]);
        }
        return checksum;
    }


    private void decodeOnLineStatus(byte[] RawData, int RawOffset) {

    }


    private void markLostPacket() {

    }

    public boolean initialize() {


        m_BLeGatt = null;
        m_bConnected = false;
        m_BluetoothAddr = null;

        return true;
    }

    public boolean isDeviceConnected() {
        return m_bConnected;
    }

    public String getBluetoothAddr() {
        return m_BluetoothAddr;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return m_BluetoothAdapter;
    }

    public BluetoothManager getBluetoothManager() {
        return m_BluetoothManager;
    }


    public boolean checkValidDevice(BluetoothDevice device) {
        if (device == null) {
            return false;
        }
        String name = device.getName();
        if (name == null) {
            return false;
        }
        return name.equals("EL-198 ECG RECORDER");
    }

    public boolean connect(String addr) {
        boolean bReConn = ((m_BluetoothAddr != null) && (m_BLeGatt != null));
        if ((addr == null) && (!bReConn)) {
            close();
            return false;
        }

        if (bReConn) {
            if ((addr != null) && (!addr.equals(m_BluetoothAddr))) {
                bReConn = false;
            }
        }

        if (bReConn) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");

            m_OpStage = Op_Stage_Conn;
            return m_BLeGatt.connect();
        }

        close();
        final BluetoothDevice device = m_BluetoothAdapter.getRemoteDevice(addr);
        if (device == null) {
            Log.w(TAG, "Device not found. Unable to connect.");
            return false;
        }

        Log.d(TAG, "Trying to create a new connection.");

        m_BluetoothAddr = addr;
        m_OpStage = Op_Stage_Conn;
        m_BLeGatt = device.connectGatt(m_Context, false, m_GattCallback);

        return true;
    }

    public void disconnect() {
        if (m_BLeGatt != null) {
            m_BLeGatt.disconnect();
        }

        m_bConnected = false;

    }

    public void close() {
        if (m_BLeGatt != null) {
            m_BLeGatt.disconnect();
            m_BLeGatt.close();
        }
        m_bConnected = false;

        m_BLeGatt = null;
        m_BluetoothAddr = null;

    }

    public boolean writeCommandCode(DeviceCommand cmd, DeviceApiOpListener apiOpListener) {
        boolean bRet = false;
        if ((m_BLeGatt != null) && (m_WriteCharacteristic != null) && (m_bConnected)) {
            synchronized (m_QueueCritObj) {
                if (m_CmdQueue.isEmpty()) {
                    bRet = m_WriteCharacteristic.setValue(DeviceCommand.getCmdCode(cmd), BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                    if (bRet) bRet = m_BLeGatt.writeCharacteristic(m_WriteCharacteristic);
                }
                if (bRet) {
                    Pair<DeviceCommand, DeviceApiOpListener> cmdPair = Pair.create(cmd, apiOpListener);
                    bRet = m_CmdQueue.offer(cmdPair);
                }
            }
        }
        return bRet;
    }


}
