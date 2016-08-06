package com.keenbrace.util;

import java.text.DecimalFormat;

import com.github.mikephil.charting.utils.ValueFormatter;

public class LeftValueFormatter implements ValueFormatter {
    private DecimalFormat mFormat;

    @Override
    public String getFormattedValue(float value) {
        mFormat = new DecimalFormat("###,###,###,##0");
        String v = mFormat.format(value);
        int len = v.length();
        if (len < 3) {
            for (int i = 0; i < 3 - len; i++) {
                v += " ";
            }
        }
        return v;
    }


}
