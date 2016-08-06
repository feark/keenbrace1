package com.keenbrace.util;

import java.io.File;

import android.os.Environment;

public class StringUtil {
    public final static String ROOT_FILEPATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "keenbrace"
            + File.separator;
    public final static String IMAGE_FILEPATH = ROOT_FILEPATH + "images" + File.separator;

    public static String formatToLC(String lc) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.000");
        Integer ll = 0;
        try {
            ll = Integer.parseInt(lc);
        } catch (NumberFormatException e) {

        }
        if (ll >= 100000) {
            return df.format(ll / 100000.0f) + "km";
        }
        return df.format(ll / 100.0f) + "m";
        //return lc;

    }
}
