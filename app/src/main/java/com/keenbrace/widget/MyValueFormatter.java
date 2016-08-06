package com.keenbrace.widget;

import java.text.DecimalFormat;

import com.github.mikephil.charting.utils.ValueFormatter;

public class MyValueFormatter implements ValueFormatter {


    @Override
    public String getFormattedValue(float value) {
        return String.valueOf((int) value);
    }

}
