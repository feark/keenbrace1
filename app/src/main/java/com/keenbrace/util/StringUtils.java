package com.keenbrace.util;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
	public final static String LP_FILEPATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "lifepower"
			+ File.separator;
	public final static String HRV_FILEPATH_OTG = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "hrv"
			+ File.separator+"otg"+ File.separator;
	public final static String OTG_FILEPATH="/storage/usbotg/";

	public static String getEcgNubmerCode() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}

	public static String getCurrentDate() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static String secToTime(int time) {

		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			}
		}
		return timeStr;
	}
	public static String secToTime2(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "0�?";
		else {
			if(time>60){
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + "分钟" + unitFormat(second)+"�?";
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99小时59�?59�?";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分钟"
						+ unitFormat(second)+"�?";
			}}else
			{
				timeStr=unitFormat(time)+"�?";
			}
		}
		return timeStr;
	}
	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr =  Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}
	public static String formatToM(long b)
	{
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.00");
		return df.format(b/1048576.00+0.01)+"M";

	}
	public static String getVcode()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dd= dateFormat.format(new Date());
		return dd+"123456";
		//������ʱ����+���ֵ
	}
	public static int partToInt(String value)
	{
		try {
			if(!"".equals(value))
			{
				return Integer.parseInt(value);
			}
		} catch (Exception e) {
			
		}
		return 0;
	}
	public static float parseFloat(String value)
	{
		try {
			if(!"".equals(value))
			{
				return Float.parseFloat(value);
			}
		} catch (Exception e) {
			
		}
		return 0;
	}
}
