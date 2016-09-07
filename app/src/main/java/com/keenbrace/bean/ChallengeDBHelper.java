package com.keenbrace.bean;

import android.content.Context;

import com.keenbrace.AppContext;
import com.keenbrace.greendao.Challenge;
import com.keenbrace.greendao.ChallengeDao;
import com.keenbrace.greendao.DaoSession;

import java.util.List;

/**
 * Created by ken on 16-9-7.
 */
public class ChallengeDBHelper {
    private static Context mContext;
    private static ChallengeDBHelper instance;
    private ChallengeDao challengeDao;

    private ChallengeDBHelper(){

    }

    public static ChallengeDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ChallengeDBHelper();
            if (mContext == null) {
                mContext = context;
            }
            DaoSession daoSession = AppContext.getDaoSession(mContext); // 数据库对象
            instance.challengeDao=daoSession.getChallengeDao();
        }
        return instance;
    }

    //插入新的表
    public long insertChallenge(Challenge challenge)
    {
        //作为ID
        return  challengeDao.insert(challenge);
    }

    //更新表
    public void updateChallenge(Challenge challenge)
    {
        challengeDao.update(challenge);
    }

    public List<Challenge> queryChallenge()
    {
        return challengeDao.loadAll();
    }

}
