package com.keenbrace;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.keenbrace.core.base.KeenbraceApplication;
import com.keenbrace.greendao.DaoMaster;
import com.keenbrace.greendao.DaoSession;
import com.keenbrace.services.BluetoothConstant;
import com.keenbrace.services.BluetoothLeService;
import com.keenbrace.upload.UploadService;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Properties;


public class AppContext extends KeenbraceApplication implements UncaughtExceptionHandler{
    
	private static AppContext instance;

	private static UncaughtExceptionHandler defaultUncaught;



	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		instance = this;
		
		defaultUncaught = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		
		init();
	}
	
	private void init(){
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

	}
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
									   IBinder service) {
			BluetoothConstant.mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!BluetoothConstant.mBluetoothLeService.initialize()) {
				Log.e("", "Unable to initialize Bluetooth");
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			BluetoothConstant.mBluetoothLeService = null;
		}
	};

	public static AppContext getInstance(){
		return instance;
	}

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }


	public  static DaoSession getDaoSession(Context context){
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "holter.db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		return daoMaster.newSession();
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 向本地写入错误日志.
//				WLog.writeError(ex);
			}
		}).start();

		if (defaultUncaught != null) {
			defaultUncaught.uncaughtException(thread, ex);
			// TODO 使用默认处理关闭程序
		}
	}
}
