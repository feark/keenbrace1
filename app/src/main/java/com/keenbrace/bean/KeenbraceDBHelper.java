package com.keenbrace.bean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.keenbrace.AppContext;
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.greendao.KeenBraceDao;
import com.keenbrace.greendao.RunWaring;
import com.keenbrace.greendao.RunWaringDao;
import com.keenbrace.greendao.User;
import com.keenbrace.greendao.UserDao;
import com.keenbrace.greendao.DaoSession;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zrq on 16/1/12.
 */
public class KeenbraceDBHelper {

    private static Context mContext;
    private static KeenbraceDBHelper instance;
    private UserDao userDao;
    private RunWaringDao runWaringDao;
    private KeenBraceDao keenBraceDao;

    private KeenbraceDBHelper() {
    }

    public static KeenbraceDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new KeenbraceDBHelper();
            if (mContext == null) {
                mContext = context;
            }
            DaoSession daoSession = AppContext.getDaoSession(mContext); // 数据库对象
            instance.userDao = daoSession.getUserDao();
            instance.runWaringDao = daoSession.getRunWaringDao();
            instance.keenBraceDao = daoSession.getKeenBraceDao();
        }
        return instance;
    }

    public long insertKeenBreace(KeenBrace keenBrace) {
        return keenBraceDao.insert(keenBrace);
    }

    public long insertUser(User user) {
        return userDao.insertOrReplace(user);
    }

    public void upateUser(User user) {
        userDao.update(user);
    }

    public User queryUserByLoginName(String loginName) {
        return userDao.queryBuilder().where(UserDao.Properties.LoginName.eq(loginName)).unique();
    }

    public long insertKeenBrace(KeenBrace keenBrace) {
        return keenBraceDao.insert(keenBrace);
    }

    public void updateKeenBrace(KeenBrace keenBrace) {
        keenBraceDao.update(keenBrace);
    }


    public void deleteKeenBrace(long runId)
    {
        keenBraceDao.deleteByKey(runId);
    }
    public void deleteRunWaring(long rwid)
    {
        runWaringDao.deleteByKey(rwid);
    }
    public List<KeenBrace> queryKeenBraces() {
        return keenBraceDao.loadAll();
    }

    public long insertRunWaring(RunWaring runWaring) {
        return runWaringDao.insert(runWaring);
    }

    public List<RunWaring> queryRunWaringByRunId(long runid) {
        return runWaringDao.queryBuilder().where(RunWaringDao.Properties.RunId.eq(runid)).list();
    }

    public HashMap<String, String> querySumBle() {
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
    public void deleteRunWarings(long runid)
    {
        SQLiteDatabase db = AppContext.getDaoSession(mContext).getDatabase();
        db.delete("KEEN_BRACE"," _id=?",new String[]{runid+""});
        db.delete("RUN_WARING"," RUN_ID=?",new String[]{runid+""});
        db.close();
    }
}
