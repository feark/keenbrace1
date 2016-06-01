package com.keenbrace.util;

public class FftUtil {
    static {
        System.loadLibrary("KeenBrace");
    }

    public static native int getJrll(int power);
}
