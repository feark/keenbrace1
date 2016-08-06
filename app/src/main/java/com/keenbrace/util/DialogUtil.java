package com.keenbrace.util;


import android.app.TimePickerDialog;
import android.content.Context;

import java.util.Calendar;

public class DialogUtil {
	public static void showTimePickDialog(Context context, TimePickerDialog.OnTimeSetListener otsl) {
		Calendar calendar = Calendar.getInstance();
		TimePickerDialog dialog = new TimePickerDialog(context, otsl,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				true);   //是否为二十四制
		dialog.show();
	}

}
