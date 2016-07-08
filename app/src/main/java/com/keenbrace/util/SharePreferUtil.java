package com.keenbrace.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferUtil {

	private static final String SHARE_PREFERENCE_UTIL = "share_preference_util";

	private static final String USERNAME = "UserName";
	private static final String PASSWORD = "PassWord";
	public static final String FULLNAME = "FullName";
	public static final String AGE = "age";
	public static final String GENDER = "gender";
	public static final String UCODE = "uCode";
	public static final String ISAUTOLOGIN = "isAutoLogin";
	public static final String ISRBPWD = "isRbpwd";
	public static final String CONN_DEVICE = "device";

	public static void setUser(Context context, String name, String pwd) {
		SharedPreferences sp = context.getSharedPreferences(
				SHARE_PREFERENCE_UTIL, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(USERNAME, name);
		editor.putString(PASSWORD, pwd);
		editor.commit();
	}

	public static void setParamValue(Context context, String name, String value) {
		SharedPreferences sp = context.getSharedPreferences(
				SHARE_PREFERENCE_UTIL, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(name, value);
		editor.commit();
	}

	public static String getParamValue(Context context, String param) {
		SharedPreferences sp = context.getSharedPreferences(
				SHARE_PREFERENCE_UTIL, Context.MODE_WORLD_READABLE);
		return sp.getString(param, "");
	}

	public static String getUserName(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHARE_PREFERENCE_UTIL, Context.MODE_WORLD_READABLE);
		return sp.getString(USERNAME, "");
	}

	public static String getPassWord(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHARE_PREFERENCE_UTIL, Context.MODE_WORLD_READABLE);
		return sp.getString(PASSWORD, "");
	}
	public static String getIsRbpwd(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHARE_PREFERENCE_UTIL, Context.MODE_WORLD_READABLE);
		return sp.getString(ISRBPWD, "");
	}








	public static void setParamValue(Context context, String name, int value) {
		SharedPreferences sp = context.getSharedPreferences(
				SHARE_PREFERENCE_UTIL, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(name, value);
		editor.commit();
	}

	public static int getParamIntValue(Context context, String param) {
		SharedPreferences sp = context.getSharedPreferences(
				SHARE_PREFERENCE_UTIL, Context.MODE_WORLD_READABLE);
		return sp.getInt(param, 0);
	}
	public static String getDevice(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHARE_PREFERENCE_UTIL, Context.MODE_WORLD_READABLE);
		return sp.getString(CONN_DEVICE, "");
	}
}
