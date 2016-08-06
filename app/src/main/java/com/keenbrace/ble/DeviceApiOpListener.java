package com.keenbrace.ble;

public interface DeviceApiOpListener {
    public void onConnectionCompleted(boolean bSuccess);

    public void onCommandCompleted(DeviceCommand devCmd, boolean bSuccess);
}
