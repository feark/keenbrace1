package com.keenbrace.exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.keenbrace.AppContext;


//import com.aspire.android.network.Constants;
//import com.aspire.android.network.MyApplication;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    // CrashHandler 实例
    private static CrashHandler INSTANCE = new CrashHandler();

    // 程序的 Context 对象
    private Context mContext;

    private String mAccount;
    // 系统默认的 UncaughtException 处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个 CrashHandler 实例
     */
    private CrashHandler() {
    }

    /**
     * 获取 CrashHandler 实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;

        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }

            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    public final static String CATCH_UNCAUGHT_EXCEPTION = "CATCH_UNCAUGHT_EXCEPTION";
    public final static String UNCAUGHT_EXCEPTION_LOG_PATH = "UNCAUGHT_EXCEPTION_LOG_PATH";
    public final static String UNCAUGHT_EXCEPTION_TIME = "UNCAUGHT_EXCEPTION_TIME";

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException(final Throwable ex) {

        if (ex == null) {
            return false;
        }
        // 使用 Toast 来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(AppContext.getInstance(),"应用程序异常退出!", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        String crashInfo = saveCrashInfo2String(ex);

        String savePath = saveCrashInfo2File(crashInfo);
        if (savePath != null) {
            SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance()
                    );
            Editor edit = preferenceManager.edit();
            edit.putBoolean(CATCH_UNCAUGHT_EXCEPTION, true);
            edit.putString(UNCAUGHT_EXCEPTION_LOG_PATH, savePath);
            edit.putLong(UNCAUGHT_EXCEPTION_TIME, System.currentTimeMillis());
            edit.commit();

        }

        //
        // try {
        // String appName = mContext.getResources().getString(R.string.app_name);
        // String version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        // Intent sendIntent = new Intent(Intent.ACTION_SEND);
        // sendIntent.setType("text/plain");
        // sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "ceshihaoma");
        // sendIntent.putExtra(Intent.EXTRA_SUBJECT, appName + " " + version + " crash log");
        // sendIntent.putExtra(Intent.EXTRA_TEXT, crashInfo);
        // PendingIntent pi = PendingIntent.getActivity(mContext, 0,
        // Intent.createChooser(sendIntent, "Email " + appName + " crash log via:"), 0);
        // pi.send();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // 上传错误日志到服务器
        // ui线程抛出异常时候无法正确弹出框
        // WaitforFinish waitforFinish = new WaitforFinish();
        // UploadFileToFtpServer.getInstance().addUploadWlanTask(zipFilePath, true, null, waitforFinish);
        // waitforFinish.waitFor(1000 * 60 * 15);
        // new Thread() {
        // @Override
        // public void run() {
        // Looper.prepare();
        // Toast.makeText(MyApplication.instance(), "d上传完毕", Toast.LENGTH_LONG).show();
        // Looper.loop();
        // }
        // }.start();

        // 提示框结束
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

//            mAccount = prefs.getString(Constants.SHARED_WIFI_ACCOUNT, "");
//            if (TextUtils.isEmpty(mAccount))
//                mAccount = prefs.getString(Constants.SHARED_EDU_ACCOUNT, "");

            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
//                infos.put("province", prefs.getString(Constants.SHARED_WIFI_PROVINCE, ""));
//                infos.put("brand", prefs.getString(Constants.SHARED_WIFI_BRAND, ""));
//                infos.put("version", prefs.getString(Constants.SHARED_WIFI_VERSION, ""));
//                infos.put("system", prefs.getString(Constants.SHARED_WIFI_SYSTEM, ""));
//                infos.put("account", mAccount);
//                infos.put("password", prefs.getString(Constants.SHARED_WIFI_PASSWORD, ""));
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }

    }

    /**
     * 保存错误信息到文件中 *
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2String(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }

    private String saveCrashInfo2File(String carshInfo) {
        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + mAccount + ".log";
            String path = "/sdcard/acep/crash/";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(carshInfo.getBytes());
                fos.close();
            }

            return path + fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }
}
