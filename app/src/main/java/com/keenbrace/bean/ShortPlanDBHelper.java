package com.keenbrace.bean;

import android.content.Context;

import com.keenbrace.AppContext;
import com.keenbrace.greendao.DaoSession;
import com.keenbrace.greendao.ShortPlan;
import com.keenbrace.greendao.ShortPlanDao;

import java.util.List;

/**
 * Created by ken on 16-11-21.
 */

public class ShortPlanDBHelper {
    private static Context mContext;
    private static ShortPlanDBHelper instance;
    private ShortPlanDao shortPlanDao;

    private ShortPlanDBHelper(){

    }

    public static ShortPlanDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ShortPlanDBHelper();
            if (mContext == null) {
                mContext = context;
            }
            DaoSession daoSession = AppContext.getDaoSession(mContext); // 数据库对象
            instance.shortPlanDao=daoSession.getShortPlanDao();
        }
        return instance;
    }

    //插入新的表
    public long insertShortPlan(ShortPlan shortPlan)
    {
        //作为ID
        return  shortPlanDao.insert(shortPlan);
    }

    //更新表
    public void updateShortPlan(ShortPlan shortPlan)
    {
        shortPlanDao.update(shortPlan);
    }

    public List<ShortPlan> queryShortPlan()
    {
        return shortPlanDao.loadAll();
    }

}
