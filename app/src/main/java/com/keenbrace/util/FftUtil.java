package com.keenbrace.util;

public class FftUtil {
    static {
        System.loadLibrary("KeenBrace_Sports");
    }

    public static native int getJrll(int power);
}
