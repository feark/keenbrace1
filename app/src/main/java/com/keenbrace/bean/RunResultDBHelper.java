package com.keenbrace.bean;

//这个文件需要重写 ken

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.keenbrace.AppContext;
import com.keenbrace.greendao.RunResult;
import com.keenbrace.greendao.RunResultDao;
import com.keenbrace.greendao.User;
import com.keenbrace.greendao.UserDao;
import com.keenbrace.greendao.DaoSession;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zrq on 16/1/12.
 */
public class RunResultDBHelper {

    private static Context mContext;
    private static RunResultDBHelper instance;
    private UserDao userDao;
    private RunResultDao runResultDao;

    private RunResultDBHelper() {
    }

    public static RunResultDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RunResultDBHelper();
            if (mContext == null) {
                mContext = context;
            }
            DaoSession daoSession = AppContext.getDaoSession(mContext); // 数据库对象
            instance.userDao=daoSession.getUserDao();
            instance.runResultDao=daoSession.getRunResultDao();
        }
        return instance;
    }
    public long insertRunResult(RunResult run_result)
    {
        return  runResultDao.insert(run_result);
    }
    public long insertUser(User user)
    {
        return userDao.insert(user);
    }
    public void upateUser(User user)
    {
        userDao.update(user);
    }
    public User queryUserByLoginName(String loginName)
    {
        return  userDao.queryBuilder().where(UserDao.Properties.LoginName.eq(loginName)).unique();
    }

    public long insertKeenBrace(RunResult run_result)
    {
        return  runResultDao.insert(run_result);
    }
    public void updateRunResult(RunResult run_result)
    {
        runResultDao.update(run_result);
    }
    public List<RunResult> queryRunResult()
    {
        return runResultDao.loadAll();
    }

    public HashMap<String, String> querySumBle()
    {
        HashMap<String, String> values = new HashMap<String, String>();

        SQLiteDatabase db = AppContext.getDaoSession(mContext).getDatabase();
        Cursor cursor = null;
        try {

            cursor = db
                    .rawQuery(
                            "select sum(timelength),avg(cadence),avg(stride),sum(mileage),sum(sumwarings) from KEEN_BRACE",
                            new String[]{});
            if (cursor.moveToNext()) {
                values.put("timelength", cursor.getString(0));//
                values.put("cadence", cursor.getString(1));//
                values.put("stride", cursor.getString(2));//
                values.put("mileage", cursor.getString(3));//
                values.put("sumwarings", cursor.getString(4));//
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return values;
    }
}
