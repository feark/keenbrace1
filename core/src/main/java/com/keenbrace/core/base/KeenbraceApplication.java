package com.keenbrace.core.base;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.File;


public class KeenbraceApplication extends Application{

	private static String PREF_NAME = "creativelocker.pref";

	static Context ctx;
	static Resources res;

	byte[] bleData;
	boolean isSendBleEnd = false;
	boolean isStartWorkout = false;
	
    private static boolean sIsAtLeastGB;

    static {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		    sIsAtLeastGB = true;
		}
    }
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ctx = getApplicationContext();
		res = ctx.getResources();

		//创建好文件路径
		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "keenbrace");
		if (!f.exists()) {
			f.mkdir();
		}
	}
	
	public static synchronized KeenbraceApplication context(){
		return (KeenbraceApplication) ctx;
	}
	
	public static Resources resources(){
		return res;
	}
	
   @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(Editor editor) {
		if (sIsAtLeastGB) {
		    editor.apply();
		} else {
		    editor.commit();
		}
    }

	public static void set(String key, boolean value) {
		Editor editor = getPreferences().edit();
		editor.putBoolean(key, value);
		apply(editor);
	}

	public static void set(String key, String value) {
		Editor editor = getPreferences().edit();
		editor.putString(key, value);
		apply(editor);
	}

	public static void set(String key,int value){
		Editor editor = getPreferences().edit();
		editor.putInt(key, value);
		apply(editor);
	}


	public static boolean get(String key, boolean defValue) {
		return getPreferences().getBoolean(key, defValue);
	}

	public static String get(String key, String defValue) {
		return getPreferences().getString(key, defValue);
	}

	public static int get(String key, int defValue) {
		return getPreferences().getInt(key, defValue);
	}

	public static long get(String key, long defValue) {
		return getPreferences().getLong(key, defValue);
	}

	public static float get(String key, float defValue) {
		return getPreferences().getFloat(key, defValue);
	}

	public static SharedPreferences getPreferences() {
		SharedPreferences pre = context().getSharedPreferences(PREF_NAME,
			Context.MODE_PRIVATE);
		return pre;
	}

	public static SharedPreferences getPreferences(String prefName) {
		return context().getSharedPreferences(prefName,
			Context.MODE_PRIVATE);
	}

	public static int[] getDisplaySize() {
		return new int[] { getPreferences().getInt("screen_width", 480),
			getPreferences().getInt("screen_height", 854) };
	}

	public static void saveDisplaySize(Activity activity) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
			.getMetrics(displaymetrics);
		Editor editor = getPreferences().edit();
		editor.putInt("screen_width", displaymetrics.widthPixels);
		editor.putInt("screen_height", displaymetrics.heightPixels);
		editor.putFloat("density", displaymetrics.density);
		editor.commit();
	}

	public static String string(int id) {
	return res.getString(id);
	}

	public static String string(int id, Object... args) {
	return res.getString(id, args);
	}

	//---------------------------------------全局变量 ken
	public void setBleData(byte[] data)
	{
		this.bleData = data;
	}

	public byte[] getBleData()
	{
		return this.bleData;
	}

	//是否已经发送了停止运动的蓝牙协议
	public void setIsSendBleEnd(boolean v)
	{
		this.isSendBleEnd = v;
	}

	public boolean getIsSendBleEnd()
	{
		return this.isSendBleEnd;
	}

	public void setIsStartWorkout(boolean v)
	{
		this.isStartWorkout = v;
	}

	public boolean getIsStartWorkout()
	{
		return this.isStartWorkout;
	}
}
