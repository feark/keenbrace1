package com.keenbrace.ble;

public enum DeviceCommand {
    DevComm_Empty,
    DevComm_Beep,
    DevComm_ReadRomVer,
    DevComm_Int7MS,
    DevComm_Int20MS,
    DevComm_Int40MS,
    DevComm_OpenAD,
    DevComm_CloseAD;

    public static int getCmdCode(DeviceCommand cmd) {
        switch (cmd) {
            case DevComm_Empty:
                return 0x80;
            case DevComm_Beep:
                return 0x81;
            case DevComm_ReadRomVer:
                return 0x16;
            case DevComm_Int7MS:
                return 0x0c;
            case DevComm_Int20MS:
                return 0x82;
            case DevComm_Int40MS:
                return 0x83;
            case DevComm_OpenAD:
                return 0x44;
            case DevComm_CloseAD:
                return 0x45;
            default:
                return 0;
        }
    }

}
