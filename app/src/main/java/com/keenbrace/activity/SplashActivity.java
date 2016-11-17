package com.keenbrace.activity;

import android.os.Bundle;
import android.widget.TextView;


import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.UserDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.core.utils.PreferenceHelper;
import com.keenbrace.greendao.HistoryRecord;
import com.keenbrace.greendao.User;
import com.parse.Parse;

import butterknife.Bind;


public class SplashActivity extends BaseActivity {





    @Override
    protected int getLayoutId() {
        return R.layout.activity_logo;
    }





    public void waitIn(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    //停在开始logo页的时间
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                if(PreferenceHelper.readBoolean(AppContext.getInstance(),
                        UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_LOGIN, false)){ //已经登录过
                    //在这里查询是否已经登录过 并按登录名 查找出数据库中的user数据 ken
                    String account=  PreferenceHelper.readString(AppContext.getInstance(),UtilConstants.SHARE_PREF, UtilConstants.KEY_ACCOUNT, "");

                    User user= UserDBHelper.getInstance(SplashActivity.this).queryUserByLoginName(account);
                    //在这个位置把数据库新建的user赋给全局变量
                    Constant.user=user;

                    //已经登录账号的历史记录
                    HistoryRecord historyRecord = UserDBHelper.getInstance(SplashActivity.this).queryHistoryByLoginName(account);
                    Constant.historyRecord = historyRecord;

                    readyGoThenKill(MainActivity.class);
                }else{
                    //未登录过 去登录
                    readyGoThenKill(LoginActivity.class);
                }
            }
        }).start();
    }

    @Override
    public void initView() {

        waitIn();
    }

    @Override
    public void initData() {
        //初始化服务器后台
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("mVk8EhlQ0G1NUzcOIeZZdmcZeC4k8jj64TCzprlc")
                        .clientKey("45jjlLqFoIPGcquSw4IUbqCWKZgSpRMFMkkH4PeP")
                        .server("https://parseapi.back4app.com/").build()
        );
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
    }
}
