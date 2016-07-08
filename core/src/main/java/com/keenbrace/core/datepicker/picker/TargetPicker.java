package com.keenbrace.core.datepicker.picker;

import android.app.Activity;

/**
 * Created by ken on 16-7-3.
 */
public class TargetPicker extends OptionPicker {
    /**
     * Instantiates a new Sex picker.
     *
     * @param activity the activity
     */
    public TargetPicker(Activity activity) {
        super(activity, new String[]{
                "Triceps",
                "Biceps",
                "Shoulder",
                "Forearm",
                "Chest",
                "Back",
                "Abs/Core",
                "Glutes",
                "Upper Leg",
                "Lower Leg",
                "Cardio",
                "Whole"
        });
    }
}
