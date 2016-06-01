package com.keenbrace.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class DeviceUtil {

	  public static String getIMEI(Context context) {
	        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	        String imei = tm.getDeviceId();
	        return TextUtils.isEmpty(imei) ? "-1" : imei;
	    }

	


}
