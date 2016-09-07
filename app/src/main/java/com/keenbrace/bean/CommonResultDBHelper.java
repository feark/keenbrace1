package com.keenbrace.bean;

//这个文件需要重写 ken

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.keenbrace.AppContext;
import com.keenbrace.greendao.CommonResult;
import com.keenbrace.greendao.CommonResultDao;
import com.keenbrace.greendao.User;
import com.keenbrace.greendao.UserDao;
import com.keenbrace.greendao.DaoSession;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zrq on 16/1/12.
 */
public class CommonResultDBHelper {

    private static Context mContext;
    private static CommonResultDBHelper instance;
    private CommonResultDao commonResultDao;

    private CommonResultDBHelper() {
    }

    public static CommonResultDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CommonResultDBHelper();
            if (mContext == null) {
                mContext = context;
            }
            DaoSession daoSession = AppContext.getDaoSession(mContext); // 数据库对象
            instance.commonResultDao=daoSession.getCommonResultDao();
        }
        return instance;
    }

    //插入新的表
    public long insertCommonResult(CommonResult commonResult)
    {
        //作为ID
        return  commonResultDao.insert(commonResult);
    }

    //更新表
    public void updateCommonResult(CommonResult commonResult)
    {
        commonResultDao.update(commonResult);
    }

    public List<CommonResult> queryCommonResult()
    {
        return commonResultDao.loadAll();
    }

    public HashMap<String, String> querySumRunResult()
    {
        HashMap<String, String> values = new HashMap<String, String>();

        SQLiteDatabase db = AppContext.getDaoSession(mContext).getDatabase();
        Cursor cursor = null;
        try {

            cursor = db
                    .rawQuery(
                            "select sum(duration),avg(cadence),avg(stride),sum(mileage) from KEEN_BRACE",
                            new String[]{});
            if (cursor.moveToNext()) {
                values.put("duration", cursor.getString(0));//
                values.put("cadence", cursor.getString(1));//
                values.put("stride", cursor.getString(2));//
                values.put("mileage", cursor.getString(3));//

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
