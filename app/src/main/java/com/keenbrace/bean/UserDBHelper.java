package com.keenbrace.bean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.keenbrace.AppContext;
import com.keenbrace.greendao.HistoryRecord;
import com.keenbrace.greendao.HistoryRecordDao;
import com.keenbrace.greendao.LongPlan;
import com.keenbrace.greendao.LongPlanDao;
import com.keenbrace.greendao.ShortPlan;
import com.keenbrace.greendao.ShortPlanDao;
import com.keenbrace.greendao.User;
import com.keenbrace.greendao.UserDao;
import com.keenbrace.greendao.DaoSession;

import java.util.HashMap;
import java.util.List;

//长短计划 历史 也放在这个文件里

/**
 * Created by ken on 16-7-12.
 */
public class UserDBHelper {
    private static Context mContext;
    private static UserDBHelper instance;
    private UserDao userDao;
    private ShortPlanDao shortPlanDao;
    private LongPlanDao longPlanDao;
    private HistoryRecordDao historyRecordDao;

    private UserDBHelper(){
    }

    public static UserDBHelper getInstance(Context context){
        if(instance == null){
            instance = new UserDBHelper();
            if(mContext == null){
                mContext = context;
            }

            DaoSession daosession = AppContext.getDaoSession(mContext);
            instance.userDao = daosession.getUserDao();
            instance.shortPlanDao = daosession.getShortPlanDao();
            instance.longPlanDao = daosession.getLongPlanDao();
            instance.historyRecordDao = daosession.getHistoryRecordDao();
        }
        return instance;
    }

    //User ---------------------------------------
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

    public User queryUser(long id)
    {
        return userDao.load(id);
    }

    //Short Plan ---------------------------------------
    public long insertShortPlan(ShortPlan shortPlan)
    {
        return shortPlanDao.insert(shortPlan);
    }

    public  void  updateShortPlan(ShortPlan shortPlan){
        shortPlanDao.update(shortPlan);
    }

    //根据用户登录名得到训练计划
    public ShortPlan queryShortPlanByPlanName(String planName)
    {
        return  shortPlanDao.queryBuilder().where(ShortPlanDao.Properties.ShortPlanName.eq(planName)).unique();
    }

    //更新计划
    public void updateShortPlan(String planName)
    {

    }

    //Long Plan ---------------------------------------
    public long insertLongPlan(LongPlan longPlan)
    {
        return longPlanDao.insert(longPlan);
    }

    public  void  updateLongPlan(LongPlan longPlan){
        longPlanDao.update(longPlan);
    }

    public LongPlan queryLongPlanByLoginName(String planName)
    {
        return  longPlanDao.queryBuilder().where(LongPlanDao.Properties.LongPlanName.eq(planName)).unique();
    }

    //History ---------------------------------------
    public long insertHistory(HistoryRecord historyRecord)
    {
        return historyRecordDao.insert(historyRecord);
    }

    public void updateHistoryRecord(HistoryRecord historyRecord){
        historyRecordDao.update(historyRecord);
    }

    public HistoryRecord queryHistoryByLoginName(String loginName){
        return  historyRecordDao.queryBuilder().where(HistoryRecordDao.Properties.LoginName.eq(loginName)).unique();
    }
}
