package com.keenbrace.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.CommonResultDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.core.base.KeenbraceApplication;
import com.keenbrace.greendao.CommonResult;
import com.keenbrace.greendao.CommonResultDao;
import com.keenbrace.greendao.ShortPlan;
import com.keenbrace.services.BluetoothConstant;
import com.keenbrace.services.BluetoothLeService;
import com.keenbrace.util.ByteHelp;
import com.keenbrace.util.Image;
import com.keenbrace.util.StringUtil;
import com.keenbrace.util.Util;
import com.keenbrace.widget.GifView;

public class MainMenuActivity extends BaseActivity implements
        OnClickListener, OnLongClickListener,TextToSpeech.OnInitListener {

    KeenbraceApplication application;

    FragmentRun fragmentRun;
    FragmentMapGoogle fragmentMapGoogle;
    FragmentMap fragmentMap;
    RelativeLayout rl_map, rl_performance,rl_report;
    private Fragment mLastFragment;

    //------------------------------------------------------
    //运动参数相关
    Boolean isAnyMove = false;
    float runningSpeed = 0.0f;

    int[] indiCaseCount = {0, 0, 0, 0, 0,
                           0, 0, 0, 0, 0,
                           0, 0, 0, 0, 0,
                           0, 0, 0, 0, 0};

    int goodFormCount = 0;
    int[] goodFormTalk = {  R.string.tx_encourage_1,
                            R.string.tx_encourage_2,
                            R.string.tx_encourage_7,
                            R.string.tx_encourage_12,
                            R.string.tx_encourage_14,
                            R.string.tx_goodrunform,
                         };
    int goodTalkSwitch = 0;

    int walkCount = 0;
    int[] walkTalk = {  R.string.tx_suggest_1,
                        R.string.tx_suggest_2,
                        R.string.tx_suggest_3,
                        R.string.tx_suggest_4,
                        R.string.tx_suggest_5,
                        R.string.tx_humor_0,};
    int walkTalkSwitch = 0;

    int runSwitch = 0;
    //------------------------------------------------------

    //根据不同种类的运动显示不同的动画或图片 ken
    int sport_type = 0;
    int startFrom = 0;
    Bundle data2Fragment = new Bundle();

    Button btn_start, btn_pause, btn_end, btn_continue;
    LinearLayout ly_tab;
    ImageView iv_map, iv_performance, iv_report, iv_trainer;
    ImageView switch2map;
    ImageView switch2runner;


    TextView tv_nowday, tv_nowtime;
    TextView tv_duration;
    private TransferUtility transferUtility;
    int wrCount = 0;


    byte[] data;
    private Timer updateTimer;
    private UpdateUiTimerTask updateUiTimerTask;
    private final static int ACQ_TASK_TIMER_PERIOD = 200;
    private TextToSpeech tts;

    CommonResult commonResult;

    //---------------------------------------------------
    //  algorithm data-structure

    //触发的条件与提醒之前的概率关系
    //不同的事件触发之后数组里面的概率值也应该重新计算
    //与提示字符串信息一一对应
    int[] probability = {0,0,0,0,0,
                         0,0,0,0,0,
                         0,0,0,0,0,
                         0,0,0,0,0};

    int lastVoiceEvent = UtilConstants.eventStill;
    //---------------------------------------------------

    //时间控制的变量-----------------------------
    long milli_second = 0;
    int second;

    int delay_times;
    int anino;  //动画的编号

    int countdown_times;
    String countdown_str;
    //int test_reps = 1;
    int animaCountdown;

    //跑步运动变量------------------------------
    int[] steprates = new int[]{0, 0, 0, 0, 0};

    byte speedPerMin[] = new byte[600];
    byte stabilityPerMin[] = new byte[600];
    byte kneePressPerMin[] = new byte[600];
    byte vertOsciPerMin[] = new byte[600];

    byte emgDecPerKm[] = new byte[50];
    byte cadencePerMinute[] = new byte[600];

    int distance_old;
    int aver_stride;
    long calorieSum;
    int fiveSecondCount = 0;
    int minuteCount = 0;

    float speedSum = 0.0f;
    int kneepressSum = 0;
    int vertOscSum = 0;
    int stabilitySum = 0;
    int cadenceSum = 0;
    float speed = 0.0f;

    int step = 0, stride = 0, distance = 0, index= 0, steprate = 0, osc = 0,
            pressure = 0, tgt = 0, twist, flip, power, strike, iangle;
    int[] bs = new int[5];

    int pressTimes;

    int touchGroundCount;

    int[] step_history = new int[4];

    int[] power_record = new int[200];
    int power_aver = 0;
    int power_index;
    boolean power_flag = false;
    int power_valid = 0;

    int step_true = 0;
    int old_step = 0;
    int updateStep = 0;
    int bkstep = 0;
    float PI = 3.1415926f;

    int old_steprate = 0;

    //用于控制每公里一次的行为
    int mileage_km = 0, oldMileageKm = 0;
    int km_count = 0;


    int big_stride_count = 0;

    //非跑步运动的变量---------------------------
    int setSum;
    byte repsPerSet[] = new byte[100];
    byte repsDuration[] = new byte[100];
    int load;
    int RM;
    long restTime;

    int duration_hour = 0, duration_min = 0, duration_sec = 0;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_manager;
    }

    @Override
    public void initView() {

        toolbar.inflateMenu(R.menu.main_menu);
        /*  //去除了蓝牙按钮
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (isStartRun) {
                    showTips();
                }
                return true;
            }
        });
        */
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartRun) {
                    showTips();
                }
                else
                {
                    finish();
                }
            }
        });


        //初始化后10秒检查有没有运动
        delay_times = 20;
        handler.sendEmptyMessageDelayed(1, 1000);

        ly_tab = (LinearLayout) findViewById(R.id.ly_tab);
        rl_map = (RelativeLayout) findViewById(R.id.rl_map);

        switch2map = (ImageView) findViewById(R.id.switch2map);
        switch2map.setOnClickListener(this);

        switch2runner = (ImageView)findViewById(R.id.switch2runner);
        switch2runner.setOnClickListener(this);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        rl_report = (RelativeLayout) findViewById(R.id.rl_report);
        rl_performance = (RelativeLayout) findViewById(R.id.rl_performance);

        rl_map.setOnClickListener(this);
        rl_report.setOnClickListener(this);
        rl_performance.setOnClickListener(this);

        btn_start.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_end = (Button) findViewById(R.id.btn_end);
        btn_end.setOnClickListener(this);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);

        updateViewIcon();
        //将当前界面分为4个Fragment
        if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
            fragmentMap = new FragmentMap();
            changeFragment(this, fragmentMap, null, null);
        }else {
            fragmentMapGoogle = new FragmentMapGoogle();
            changeFragment(this, fragmentMapGoogle, null, null);
        }

        fragmentRun = new FragmentRun();
        //得到运动类型 再传到fragment
        sport_type = this.getIntent().getIntExtra("sport_type",0);
        data2Fragment.putInt("sport_type", sport_type);


        //从哪个位置开始的
        startFrom = this.getIntent().getIntExtra("StartFrom",0);
        data2Fragment.putInt("StartFrom", startFrom);

        if(startFrom == UtilConstants.fromChallenge)
        {
            //从挑战开始
            //隐掉左右切换按钮
            switch2map.setVisibility(View.GONE);
            switch2runner.setVisibility(View.GONE);

            //加载挑战的几项运动 进入计时自动切换模式
        }

        //从训练计划开始的
        if(startFrom == UtilConstants.fromPlan)
        {
            //读取计划的内容

        }

        //导航条标题
        if(sport_type == UtilConstants.sport_running)
        {
            this.setActionBarTitle(getString(R.string.tx_running));
        }
        else
        {
            if(sport_type == UtilConstants.sport_squat)
                this.setActionBarTitle(getString(R.string.tx_squat));

            if(sport_type == UtilConstants.sport_dumbbell)
                this.setActionBarTitle(getString(R.string.tx_dumbbell));

            if(sport_type == UtilConstants.sport_plank)
                this.setActionBarTitle(getString(R.string.tx_plank));

            if(sport_type == UtilConstants.sport_pullup)
                this.setActionBarTitle(getString(R.string.tx_pushup));

            if(sport_type == UtilConstants.sport_pushup)
                this.setActionBarTitle(getString(R.string.tx_pushup));

            if(sport_type == UtilConstants.sport_bicyclesitup)
                this.setActionBarTitle(getString(R.string.tx_bicyclesitup));

            if(sport_type == UtilConstants.sport_closestancesquat)
                this.setActionBarTitle(getString(R.string.tx_dumbbellsquat));
        }

        fragmentRun.setArguments(data2Fragment);
        changeFragment(this, fragmentRun, null, null);

        if(sport_type != UtilConstants.sport_running)
        {
            //不是跑步的话两个按钮就变成上一种 或 下一种运动
            //switch2map.setImageResource(R.mipmap.next_training);
            switch2map.setVisibility(View.GONE);

            //switch2runner.setImageResource(R.mipmap.prev_training);
            switch2runner.setVisibility(View.GONE);
        }

        tv_nowday = (TextView) findViewById(R.id.tv_nowday);
        tv_nowtime = (TextView) findViewById(R.id.tv_nowtime);
        tv_nowtime.setText(new Date().toGMTString());
        transferUtility = Util.getTransferUtility(this);

        tv_duration = (TextView) findViewById(R.id.tv_duration);

        //启动线程
        handler.post(runnable);
    }

    public void updateViewIcon() {
        iv_map = (ImageView) findViewById(R.id.iv_map);
        iv_performance = (ImageView) findViewById(R.id.iv_performance);
        iv_report = (ImageView) findViewById(R.id.iv_report);
        iv_trainer = (ImageView) findViewById(R.id.iv_trainer);
        iv_report.setOnClickListener(this);
        iv_trainer.setOnClickListener(this);

    }

    @Override
    public void initData() {
        commonResult = new CommonResult();
    }

    public void updateBG() {
        iv_map.setImageResource(R.mipmap.menu_map);
        iv_performance.setImageResource(R.mipmap.menu_performance);
        iv_report.setImageResource(R.mipmap.menu_report);


        iv_trainer.setBackgroundResource(R.mipmap.trainer);
        // iv_animate.setImageResource(R.mipmap.animate);
        // iv_message.setImageResource(R.mipmap.bottom_message);
    }


    public void checkBleConnect()
    {
        Date d = new Date();
        if (BluetoothConstant.mConnected) {
            BluetoothConstant.mBluetoothLeService.emgSwitch(1);

            handler.sendEmptyMessageDelayed(199, 800);
            runSwitch = 1;
            //handler.sendEmptyMessageDelayed(7, 500);
            handler.sendEmptyMessage(5);
            updateTimer = new Timer();
            updateUiTimerTask = new UpdateUiTimerTask();
            updateTimer.schedule(updateUiTimerTask, 0,
                    ACQ_TASK_TIMER_PERIOD);


        }
        else{
            Toast.makeText(
                    this,
                    "You are not connected to the device, will not be able to monitor the status of your movement",
                    Toast.LENGTH_SHORT).show();
        }

    }


    File file;
    FileOutputStream fos = null;
    long bak;
    boolean isStartRun;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.switch2map:
                if(sport_type == UtilConstants.sport_running) {
                    if (isStartRun) {
                        btn_pause.setVisibility(View.VISIBLE);
                    } else {
                        btn_start.setVisibility(View.VISIBLE);

                    }

                    //隐藏地图按钮
                    switch2map.setVisibility(View.GONE);
                    switch2runner.setVisibility(View.VISIBLE);


                    btn_end.setVisibility(View.GONE);
                    btn_continue.setVisibility(View.GONE);
                    updateBG();
                    iv_map.setImageResource(R.mipmap.menu_map_sel);
                    if (UtilConstants.MapType == UtilConstants.MAP_GAODE) {
                        if (fragmentMap == null)
                            fragmentMap = new FragmentMap();
                        changeFragment(this, fragmentMap, null, null);
                    } else {
                        if (fragmentMapGoogle == null)
                            fragmentMapGoogle = new FragmentMapGoogle();
                        changeFragment(this, fragmentMapGoogle, null, null);
                    }
                }
                else {
                    //不是跑步时作为下一种运动的按钮 leave

                }
                break;

            case R.id.switch2runner:
                if (isStartRun) {
                    btn_pause.setVisibility(View.VISIBLE);
                } else {
                    btn_start.setVisibility(View.VISIBLE);

                }

                if(sport_type == UtilConstants.sport_running) {
                    switch2map.setVisibility(View.VISIBLE);
                    switch2runner.setVisibility(View.GONE);

                    btn_end.setVisibility(View.GONE);
                    btn_continue.setVisibility(View.GONE);
                    updateBG();

                    if (fragmentRun == null)
                        fragmentRun = new FragmentRun();
                    changeFragment(this, fragmentRun, null, null);
                }
                else{
                    //上一種運动 leave
                }
                break;

            case R.id.btn_start:

                btn_start.setVisibility(View.GONE);
                btn_pause.setVisibility(View.VISIBLE);

                //数据文件名
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.US);
                String fname = sdf.format(new Date());

                checkBleConnect();

                if (file == null)
                    file = new File(StringUtil.ROOT_FILEPATH + File.separator + fname
                            + ".ble");
                try {
                    if (fos == null)
                        fos = new FileOutputStream(file);
                    else
                        fos = new FileOutputStream(file, true);

                } catch (FileNotFoundException e) {
                }

                //已经开始运动后就不说这句
                if(isStartRun == false) {
                    tts.speak("beginning workout",
                            TextToSpeech.QUEUE_FLUSH, null);
                }

                isStartRun = true;
                application.setIsStartWorkout(isStartRun);

                //告诉fragment已经开始运动
                fragmentRun.setIsStartRun(isStartRun);

                Date d = new Date();

                commonResult.setStartTime(d.getTime());
                commonResult.setDuration(new Long(0L));
                commonResult.setType(sport_type);
                //把数据文件名保存进数据库
                commonResult.setDataFileName(fname);

                if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
                    commonResult.setStartlatitude(fragmentMap.getLatitude());
                    commonResult.setStartlongitude(fragmentMap.getLongitude());
                    commonResult.setEndlatitude(fragmentMap.getLatitude());
                    commonResult.setEndlongitude(fragmentMap.getLongitude());
                }else {
                    commonResult.setStartlatitude(fragmentMapGoogle.getLatitude());
                    commonResult.setStartlongitude(fragmentMapGoogle.getLongitude());
                    commonResult.setEndlatitude(fragmentMapGoogle.getLatitude());
                    commonResult.setEndlongitude(fragmentMapGoogle.getLongitude());
                }

                //在数据库中插入一个新的项 ken 注意ID是在插入数据库时生成的
                commonResult.setId(CommonResultDBHelper.getInstance(this).insertCommonResult(commonResult));
                if (fragmentRun != null)
                    fragmentRun.updateBleId(commonResult.getId());

                break;

            case R.id.btn_pause:
                tts.speak("pausing workout",
                        TextToSpeech.QUEUE_FLUSH, null);
                btn_end.setVisibility(View.VISIBLE);
                btn_continue.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.GONE);
                break;


            case R.id.btn_back:
                showTips();
                break;

            case R.id.btn_end:
                tts.speak("workout complete",
                        TextToSpeech.QUEUE_FLUSH, null);
                endRun();
                break;
            case R.id.btn_continue:
                tts.speak("resuming workout",
                        TextToSpeech.QUEUE_FLUSH, null);
                continueRun();
                break;

        }

    }

    //创建了一个线程 每秒一次循环
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            //训练计划的管理

            //跑步运动的定时线程管理
            //if(sport_type == UtilConstants.sport_running) {
                handler.sendEmptyMessage(19);

                handler.postDelayed(runnable, 1000);
            //}

        }

    };

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //延时的方法 ken
                    if (delay_times > 0) {
                        //每秒检查一次有没有在运动
                        delay_times--;
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                    else
                    {
                        //检查是否有运动 没运动就语音提示一下
                        if(!isAnyMove) {
                            /*
                            tts.speak((String) getText(R.string.tx_running_welcome),
                                    TextToSpeech.QUEUE_FLUSH, null);
                            */
                        }
                    }
                    break;

                //检测到有动作 播放动画
                case 2:
                    if (mLastFragment == fragmentRun) {
                        if(sport_type == UtilConstants.sport_running)
                        {
                            if(anino < UtilConstants.eventStill)
                            {
                                //让这个提示动画保持7秒
                                animaCountdown = 7;
                            }

                            if(animaCountdown > 0)  //7秒内不接受静止或一般跑的动画
                            {
                                if(anino == UtilConstants.eventStill)   //静止
                                {
                                    break;
                                }
                                else if(anino == UtilConstants.eventNormalRun)  //一般跑
                                {
                                    break;
                                }
                                else if(anino == UtilConstants.eventWalk)
                                {
                                    break;
                                }
                            }

                            fragmentRun.updateAnimation(anino, steprate);
                        }
                        else {
                            fragmentRun.updateAnimation(0, 0);
                        }
                    }
                    break;

                case 3:
                    //倒计时语音
                    if (countdown_times > 0) {
                        countdown_times--;

                        fragmentRun.DuringCountDown(0, countdown_times);

                        if(countdown_times <= 10)
                        {
                            countdown_str = "" + countdown_times;

                            tts.speak(countdown_str,
                                    TextToSpeech.QUEUE_FLUSH, null);
                        }

                        handler.sendEmptyMessageDelayed(3, 1000);
                    }
                    else
                    {
                        fragmentRun.CountDownEnd();
                    }
                    break;

                case 5:
                    if(sport_type == UtilConstants.sport_running) {
                        runningSlowAnalysis();

                        handler.sendEmptyMessageDelayed(5, 5000);  //定时5秒
                    }
                    break;
                case 6:
                    //fragmentReport.addLineEntry(power_valid);
                    //fragmentReport.addBarEntry(osc, (int) ya, steprate);
                    //
                    if (updateStep != step_true) {

                        if (mLastFragment == fragmentRun)
                            fragmentRun.updateView(wrCount, steprate, stride,
                                    bkstep, milli_second, distance);
                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateView(wrCount, steprate, stride,
                                    bkstep, milli_second, distance);
                    } else {

                        if (mLastFragment == fragmentRun)
                            fragmentRun.updateView(wrCount, steprate, stride,
                                    step_true, milli_second, distance);
                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateView(wrCount, steprate, stride,
                                    step_true, milli_second, distance);
                    }
                    break;

                case 7:
                    if (BluetoothConstant.mBluetoothLeService != null
                            && BluetoothConstant.mwriteCharacteristic != null) {
                        BluetoothConstant.mBluetoothLeService.startRun(runSwitch, (byte) sport_type, new Date());
                    }
                    break;

                case 8:
                    //更新跑步以外运动的参数框
                    if (mLastFragment == fragmentRun) {
                        fragmentRun.updateParaBox(reps, musclePeakPeak, commDuration, stability);

                        fragmentRun.addLineEntry(zAngle);
                    }
                    break;

                case 9:
                    if (BluetoothConstant.mBluetoothLeService != null
                            && BluetoothConstant.mwriteCharacteristic != null) {
                        BluetoothConstant.mBluetoothLeService
                                .startRun(0, (byte) sport_type, new Date());
                    }
                    break;

                case 19:    //每秒一次循环
                    tv_nowtime.setText(new Date().toGMTString());

                    if(isStartRun) {
                        milli_second += 1000;

                        if(animaCountdown > 0)
                        {
                            if(anino == UtilConstants.eventCadence)
                            {
                                handler.sendEmptyMessage(2);
                            }
                        }

                        animaCountdown--;

                        //secondsPerKm++;

                        //实时更新运动的总时长
                        duration_sec++;
                        if(duration_sec >= 60)
                        {
                            duration_min++;
                            if(duration_min >= 60){
                                duration_hour++;
                                duration_min = 0;
                            }
                            duration_sec = 0;
                        }

                        /*
                        if (duration_sec == 2) {
                            if (step_true == 0) {
                                if (BluetoothConstant.mBluetoothLeService != null
                                        && BluetoothConstant.mwriteCharacteristic != null) {
                                    BluetoothConstant.mBluetoothLeService.emgSwitch(1);

                                    runSwitch = 1;
                                    handler.sendEmptyMessageDelayed(7, 300);
                                    application.setIsSendBleEnd(false);
                                }
                            }
                        }
                        */

                        String str_duration = String.format("%02d:%02d:%02d",duration_hour,duration_min,duration_sec);
                        tv_duration.setText("Duration :  "+str_duration);

                        //if (mLastFragment == fragmentRun)
                        //fragmentRun.updateTime(milli_second / 1000, blue, yellow, red);

                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateTime(str_duration);

                        if (mLastFragment == fragmentMapGoogle)
                            fragmentMapGoogle.updateTime(str_duration);

                        runningSecondAnalysis();

                        voiceCoachScheduler();
                    }
                    break;

                case 199:
                    if (BluetoothConstant.mBluetoothLeService != null
                            && BluetoothConstant.mwriteCharacteristic != null) {
                        BluetoothConstant.mBluetoothLeService.startRun(1, (byte)sport_type,
                                new Date());
                    }

                    break;
            }
        }
    };

    private void changeFragment(FragmentActivity activity,
                                Fragment addFragment, String tag, Fragment removeFragment) {
        FragmentTransaction fragmentTransaction = activity
                .getSupportFragmentManager().beginTransaction();//
        if (mLastFragment == addFragment) {
            return;
        }

        if (addFragment != null) {
            if (!addFragment.isAdded())//
            {
                fragmentTransaction.add(R.id.content, addFragment, tag);
            }

            if (addFragment.isHidden()) {
                fragmentTransaction.show(addFragment);
            }
        }

        if (mLastFragment != null && mLastFragment != addFragment)
        {
            // fragmentTransaction.detach(mLastFragment);
            fragmentTransaction.hide(mLastFragment);
        }

		/*
         * if (removeFragment != null)// �Ƴ� {
		 * fragmentTransaction.remove(removeFragment); }
		 */

		/*
		 * if (addFragment.isDetached()) {
		 * fragmentTransaction.attach(addFragment); }
		 */

        mLastFragment = addFragment;

        fragmentTransaction.commitAllowingStateLoss();
    }


    private final Object m_CritObj = new Object();

    private class UpdateUiTimerTask extends TimerTask {
        @Override
        public void run() {
            synchronized (m_CritObj) {
                //获取蓝牙接收到的数据包
                data = application.getBleData();
                if (data != null && data.length > 0)
                    //解释数据包 ken
                    CheckPacket();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tts = new TextToSpeech(this, this);
        //registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
        //unregisterReceiver(mGattUpdateReceiver);
    }


    void updateView() {

        int z = ByteHelp.ByteArrayToShort(new byte[]{data[1], data[2]});
        int h = ByteHelp.ByteArrayToShort(new byte[]{data[3], data[4]});
        pressure = (int) Math.sqrt(z * z + h * h);
        pressTimes = (int)((float)pressure / (UtilConstants.Weight * 9.8f));

        twist = ByteHelp.byteArrayToInt(new byte[]{data[5], data[6]});    //旋转
        flip = ByteHelp.byteArrayToInt(new byte[]{data[7], data[8]});   //翻转

        step = ByteHelp.ByteArrayToShort(new byte[]{data[9], data[10]});    //步数

        iangle = ByteHelp.byteArrayToInt(new byte[]{data[11], data[12]});   //夹角

        osc = (data[13]  & 0xff);

        stability = (data[14]  & 0xff);

        tgt = (data[15]  & 0xff);

        stride = data[16] & 0xFF;

        power = data[17] & 0xFF;

        strike = data[18] & 0xFF;


        //步数不能出现负数
        if (step < 0) {
            step = old_step;
        }

        if((step - old_step) > 5)
        {
            step = old_step;
        }

        if (step != old_step) {
            step_true = step + old_step;
            bkstep = step_true;
        }

        if (updateStep == step_true) {
            step_true = step + old_step;
        }


        if (power < 10) {
            power_valid = 10;
        }

        if (power > power_valid) {
            power_valid = power;
        }

        //判断跑步的姿态正确性
        runningFormCheck();
    }

    static int old_reps = 0;
    static int reps = 0;
    int commDuration;
    int bias;
    int stability;
    int musclePeakPeak;
    String strCommResult;
    int xAngle, yAngle, zAngle, xAcc, yAcc, zAcc;

    void CheckPacket() {

        switch (data[0]) {
            case (byte) 0xa9:
                break;

            //实时数据包
            case (byte) 0xa5:

                com.umeng.socialize.utils.Log.e("run data-------------------"
                        + data[0] + "_______" + data[1]);

                if (data.length == 20)
                {
                    if (data[19] != 0x03) {
                        break;
                    }

                    try {
                        if (fos != null) {
                            fos.write(data, 0, 20);
                        }
                    } catch (IOException e) {
                    }

                    updateView();
                }
            break;

            //跑步以外的其他运动参数及提示
            case (byte)0xab:
                if (data.length == 16) {
                    if (data[15] != 0x03) {
                        break;
                    }

                    try {
                        if (fos != null) {
                            fos.write(data, 0, 16);
                        }
                    } catch (IOException e) {
                    }

                    //动作次数
                    reps = ByteHelp.byteArrayToInt(new byte[]{data[2], data[3]});
                    if (reps == 0) {
                        isAnyMove = false;
                    } else if ((reps - old_reps) > 5) {
                        break;
                    } else {
                        //将每一个计数读出来
                        //如果是平板支撑 一直变化的rep说明没处在运动姿势 leave
                        if (sport_type == UtilConstants.sport_plank) {
                            reps++;
                        } else {
                            if (reps != old_reps) {
                                commDuration = ByteHelp.byteArrayToInt(new byte[]{data[4], data[5]});

                                if (sport_type == UtilConstants.sport_dumbbell) {
                                    if (commDuration < 1) {
                                        reps = reps / 2;
                                    }
                                }

                                com.umeng.socialize.utils.Log.e("reps-------------------"
                                        + reps + "_______" + old_reps);

                                strCommResult = "" + reps;
                                tts.speak(strCommResult,
                                        TextToSpeech.QUEUE_FLUSH, null);

                                //播动画
                                handler.sendEmptyMessage(2);
                            }
                        }

                    }

                    commDuration = ByteHelp.byteArrayToInt(new byte[]{data[4], data[5]});

                    bias = (data[6] & 0xff);
                    stability = (data[7] & 0xff);
                    musclePeakPeak = (data[8] & 0xff);

                    //X angle
                    xAngle = (int) data[9];
                    if (xAngle > 0x80) {
                        xAngle = xAngle - 0xFF - 1;
                    }
                    //X acc
                    xAcc = (int) data[10];

                    //X angle
                    yAngle = (int) data[11];
                    if (yAngle > 0x80) {
                        yAngle = yAngle - 0xFF - 1;
                    }
                    //X acc
                    yAcc = (int) data[12];

                    //X angle
                    zAngle = (int) data[13];
                    if (zAngle > 0x80) {
                        zAngle = zAngle - 0xFF - 1;
                    }
                    //X acc
                    zAcc = (int) data[14];

                    old_reps = reps;

                    //更新参数
                    handler.sendEmptyMessage(8);
                }
            break;

        }
    }


    public void continueRun() {
        btn_end.setVisibility(View.GONE);
        btn_pause.setVisibility(View.VISIBLE);
        btn_continue.setVisibility(View.GONE);
    }

    public void endRun() {

        isStartRun = false;
        application.setIsStartWorkout(isStartRun);

        handler.removeCallbacks(runnable);

        if (BluetoothConstant.mBluetoothLeService != null
                && BluetoothConstant.mwriteCharacteristic != null) {
            //BluetoothConstant.mBluetoothLeService.startRun(0, (byte) sport_type, new Date());

            BluetoothConstant.mBluetoothLeService.emgSwitch(0);

            //过300m秒后再发送停止运动
            handler.sendEmptyMessageDelayed(9, 300);
        }

        application.setIsSendBleEnd(true);

        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
        }
        isStartRun = false;
        application.setIsStartWorkout(isStartRun);

        try {
            fos.close();
            fos = null;
        } catch (IOException e) {
        }

        //跑步的结果数据
        commonResult.setDuration(milli_second); //时间
        commonResult.setMileage(distance);      //里程
        commonResult.setCadence(steprate);      //步频
        commonResult.setStep((long) step_true); //步数
        commonResult.setSpeedPerMinute(speedPerMin);
        commonResult.setMinuteCount(minuteCount);
        commonResult.setCadencePerKm(cadencePerMinute);         //每公里步频
        commonResult.setStride(aver_stride);               //平均步幅
        commonResult.setKneePress(kneePressPerMin);            //膝盖压力
        commonResult.setVertOsci(vertOsciPerMin);
        commonResult.setEmgDecrease(emgDecPerKm);
        commonResult.setCalorie(calorieSum);
        commonResult.setStability(stabilityPerMin);

        //非跑步的结果数据
        commonResult.setSet(setSum);  //做了多少组
        commonResult.setReps(repsPerSet); //做了多少下
        commonResult.setRep_duration(repsDuration); //每一下的时间长短
        commonResult.setLoad(load); //负重
        commonResult.setRM(RM);
        commonResult.setRestTime(restTime); //休息时间


        //runResult.setEmgDecrease();
        //runResult.setFileName(file.getAbsolutePath());    //ken
        commonResult.setEndTime(bak);
        if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
            commonResult.setLatLngs(fragmentMap.getMap());

            commonResult.setEndlatitude(fragmentMap.getLatitude());
            commonResult.setEndlongitude(fragmentMap.getLongitude());
        }else
        {
            commonResult.setLatLngs(fragmentMapGoogle.getMaps());

            commonResult.setEndlatitude(fragmentMapGoogle.getLatitude());
            commonResult.setEndlongitude(fragmentMapGoogle.getLongitude());
        }

        //将本次的运动结果更新数据库 ken
        CommonResultDBHelper.getInstance(this).updateCommonResult(commonResult);

        //CommonResultDBHelper.getInstance(this).

        btn_end.setVisibility(View.GONE);
        btn_pause.setVisibility(View.GONE);
        btn_continue.setVisibility(View.GONE);
        btn_start.setVisibility(View.VISIBLE);

        /*
        TransferObserver observer = transferUtility.upload(
                UtilConstants.BUCKET_NAME, file.getName(), file);
                */

        //数据传到结果页
        Intent intent = new Intent();
        intent.putExtra("CommonResult", commonResult);
        intent.putExtra("sport_type", sport_type);
        intent.setClass(this, ViewRecordActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View arg0) {

        return false;
    }

    private void showTips() {
        if (!isStartRun) {
            finish();
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getText(R.string.remind))
                .setMessage(getText(R.string.sureEnd))
                .setPositiveButton(getText(R.string.sure),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                try {
                                    isStartRun = false;
                                    application.setIsStartWorkout(isStartRun);
                                    handler.removeCallbacks(runnable);
                                    btn_pause.setVisibility(View.GONE);
                                    btn_start.setVisibility(View.VISIBLE);

                                    if (BluetoothConstant.mBluetoothLeService != null
                                            && BluetoothConstant.mwriteCharacteristic != null) {

                                        BluetoothConstant.mBluetoothLeService.emgSwitch(0);

                                        //要有个延时再发
                                        handler.sendEmptyMessageDelayed(9, 300);
                                    }

                                    application.setIsSendBleEnd(true);

                                    isStartRun = false;
                                    application.setIsStartWorkout(isStartRun);
                                    try {
                                        fos.close();
                                        fos = null;
                                    } catch (IOException e) {
                                    }
                                    commonResult.setDuration(milli_second);
                                    //keenBrace_sports.setSumscore(80);
                                    // ble.setCadence((int)
                                    // bpChart.getAverage());
                                    // ble.setStride((int)
                                    // bfChart.getAverage());
                                    commonResult.setMileage(distance);
                                    //runResult.setFileName(file.getAbsolutePath());
                                    commonResult.setEndTime(bak);
                                    if (UtilConstants.MapType == UtilConstants.MAP_GAODE)
                                        commonResult.setLatLngs(fragmentMap.getMap());
                                    else
                                        commonResult.setLatLngs(fragmentMapGoogle.getMaps());
                                    //runResult.setSumwarings(wrCount);
                                    CommonResultDBHelper.getInstance(MainMenuActivity.this).updateCommonResult(commonResult);
                                } catch (Exception e) {

                                }
                                /*
                                if (BluetoothConstant.mConnected
                                        && BluetoothConstant.mBluetoothLeService != null)
                                    BluetoothConstant.mBluetoothLeService
                                            .close();
                                */
                                finish();
                            }
                        })
                .setNegativeButton(getText(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                return;
                            }
                        }).create(); // �����Ի���
        alertDialog.show(); // ��ʾ�Ի���
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showTips();

                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "not speek",
                        Toast.LENGTH_SHORT).show();
            }
        }

        //得到运动类型 再传到fragment
        sport_type = this.getIntent().getIntExtra("sport_type", 0);
        data2Fragment.putInt("sport_type", sport_type);

        application = (KeenbraceApplication) getApplication();

        //开始一个新运动的时候先检查原来的旧运动有没有正确结束
        //没结束的补一个结束 结束是不管哪种运动的
        if (BluetoothConstant.mBluetoothLeService != null
                && BluetoothConstant.mwriteCharacteristic != null) {
            BluetoothConstant.mBluetoothLeService.emgSwitch(0);
        }

        runSwitch = 0;
        handler.sendEmptyMessageDelayed(7, 300);



        //testing
        /*
        countdown_times = 40;
        fragmentRun.CountDownBegin();
        handler.sendEmptyMessage(3);
        */
    }

    //==================================================================================
    //每一种事件对应一个警报有不同概率 然后根据贝叶斯定理 把概率最高的一个语音读出来
    //语音教练调度器 字符串的ID 包含的条件集 按照这个条件集应该说什么
    //建立一个数组
    private void runningFormCheck()
    {
        float Height = 175;
        if (UtilConstants.user != null
                && !"".equals(UtilConstants.user.getHeight()))
            Height = UtilConstants.user.getHeight();
        float legLen = 0.0f;

        if (UtilConstants.user != null
                && "0".equals(UtilConstants.user.getGender())) {
            legLen = (float) (Height / (4.57 + (Height - 180) * 0.01625) + (Height - 5) / 3.74);
        } else {
            legLen = (float) (Height / (4.57 + (Height - 180) * 0.01625) + (Height - 3) / 3.74);
        }

        //每步的距离
        int stride_dis = (int) (legLen * Math.sin((stride / 2) * PI / 180));

        bak = System.currentTimeMillis();

        index++;


        float mapDistance = 0f;
        if(UtilConstants.MapType==UtilConstants.MAP_GAODE)
            mapDistance = fragmentMap.getDistance();
        else
            mapDistance = fragmentMapGoogle.getDistance();

        if(mapDistance != 0) {

            if (updateStep != step_true) {
                distance += stride_dis;
            }
        }
        else
        {
            if (updateStep != step_true) {
                distance += stride_dis;
            }
        }

        //每公里行为 每公里保存一次的参数 每公里播报一次的语音
        mileage_km = (int)((float)distance / 100000.0f);

        //要用标志控制每整数公里只执行一次以下任务 leave
        if(mileage_km != 0){
            if(mileage_km != oldMileageKm){

                emgDecPerKm[km_count] = (byte)power_aver;

                //cadencePerKm[km_count] = (byte) ( stepPerKm /  secondsPerKm);  //一公里内的步数除以时间

                oldMileageKm = mileage_km;
                km_count++;

                //secondsPerKm = 0;
                //stepPerKm = 0;
            }
        }

        updateStep = step_true;
        old_step = step;
        handler.sendEmptyMessage(6);

        if(runningSpeed < 4)
        {
            return;
        }

        //步幅大 步频也要达到一定的前提下
        if (stride > 130 && steprate > 120) {
            if (step_history[3] != step_history[0]) {

                //步幅太大 达到危险
                if(stride > 150) {
                    big_stride_count++;
                }

                if(big_stride_count < 5) {
                    //提示步幅
                    anino = UtilConstants.eventStride;

                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] % 30 == 0) {
                        probabilityCalculator(UtilConstants.eventStride);
                    }
                }
                else    //stride的次数更多 往前伸腿太多
                {
                    //提示腿不要向前探
                    anino = UtilConstants.eventReach;
                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] % 30 == 0) {
                        probabilityCalculator(UtilConstants.eventReach);

                        big_stride_count = 0;


                        indiCaseCount[UtilConstants.eventCalf]++;
                        if(indiCaseCount[UtilConstants.eventCalf] % 5 == 0)
                        {
                            probabilityCalculator(UtilConstants.eventCalf);
                        }
                    }
                }
            }
        }

        if (step_history[3] != step_history[0]) {

            //在触地的前提下
            if (tgt == 100) {
                touchGroundCount++;

                if (touchGroundCount >= 10 && steprate > 130) {
                    if(steprate < 160) {
                        anino = UtilConstants.eventFoot;

                        indiCaseCount[anino]++;

                        //提示抬腿快点
                        if (indiCaseCount[anino] % 15 == 0) {
                            probabilityCalculator(UtilConstants.eventFoot);

                            //多次出现 给出更具体的建议
                            indiCaseCount[UtilConstants.eventToe]++;
                            if(indiCaseCount[UtilConstants.eventToe] % 5 == 0)
                            {
                                probabilityCalculator(UtilConstants.eventToe);
                            }
                        }

                    }
                }

                //提示身体的动力要往前
                if (iangle > 150 && steprate < 180) {
                    anino = UtilConstants.eventLean;

                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] % 20 == 0) {
                        probabilityCalculator(UtilConstants.eventLean);
                    }
                }

            } else {
                touchGroundCount = 0;
            }

            //在膝盖压力太大的前提下
            if (pressTimes > 5) {
                //步幅太大
                if (stride > 130 && stride < 150)
                {
                    //让他注意一下脚落地重心
                    anino = UtilConstants.eventGravity;
                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] % 10 == 0) {
                        probabilityCalculator(UtilConstants.eventGravity);
                    }
                }

                //步幅特别大
                if(stride > 150)
                {
                    //直接让他把脚收回来
                    anino = UtilConstants.eventCog;
                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] % 10 == 0) {
                        probabilityCalculator(UtilConstants.eventCog);
                    }
                }

                //提示稳定性
                if(stability > 220)
                {
                    anino = UtilConstants.eventStability;

                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] % 10 == 0) {
                        probabilityCalculator(UtilConstants.eventStability);

                        //多次出现 给出更具体的建议
                        indiCaseCount[UtilConstants.eventSwingArm]++;
                        if(indiCaseCount[UtilConstants.eventSwingArm] % 5 == 0)
                        {
                            probabilityCalculator(UtilConstants.eventSwingArm);
                        }

                    }
                }
            }

            //提示落地轻点
            if (pressTimes > 6) {
                anino = UtilConstants.eventLand;
                indiCaseCount[anino]++;

                if(indiCaseCount[anino] % 10 == 0) {
                    probabilityCalculator(UtilConstants.eventLand);

                }
            }

        }


    }

    //每秒一次的跑步姿势分析
    private void runningSecondAnalysis()
    {
        if(sport_type != UtilConstants.sport_running)
        {
            return;
        }

        bs[0] = bs[1];
        bs[1] = bs[2];
        bs[2] = bs[3];
        bs[3] = bs[4];
        if (updateStep != step_true)
            bs[4] = bkstep;
        else
            bs[4] = step_true;

        float a = Math.abs(bs[4] - bs[0]);
        float b = Math.abs(bs[4] - bs[1]);
        float c = Math.abs(bs[4] - bs[2]);
        float d = Math.abs(bs[4] - bs[3]);
        float pp = (a + b + c + d) * 6;
        steprate = (int) pp;

        //5秒一次
        if (milli_second > 5000) {

            steprate = (95 * steprate + 5 * old_steprate) / 100;
        }
        steprates[0] = steprates[1];
        steprates[1] = steprates[2];
        steprates[2] = steprates[3];
        steprates[3] = steprates[4];
        steprates[4] = steprate;

        if (milli_second > 5000) {
            int totalStep = 0;
            for (int i = 0; i < 5; i++) {
                if (steprates[i] > 0) {
                    totalStep += steprates[i];
                }
            }
            steprate = totalStep / 5;
        }

        old_steprate = steprate;

        if (steprate == 0)
        {
            //站着没动 显示停止的小人
            anino = UtilConstants.eventStill;

            indiCaseCount[anino]++;

            if (indiCaseCount[anino] % 5 == 0) {
                probabilityCalculator(UtilConstants.eventStill);
            }

        }

        step_history[second] = step_true;
        second++;

        if (second >= 4)
            second = 0;

        if (power_valid != 10) {
            power_record[power_index] = power_valid;
            power_index++;
        }

        if (power_index >= 200) {

            for (int n = 0; n < 200; n++) {
                power_aver += power_record[n];
            }
            power_aver = power_aver / 200;

            power_index = 0;
            power_flag = true;
        }
    }

    //对于跑步的5秒一次分析
    private void runningSlowAnalysis()
    {
        int runningOsc = 0;

        if(sport_type != UtilConstants.sport_running)
        {
            return;
        }

        //用这种形式实现自循环
        if (isStartRun) {
            int distance_gap = distance - distance_old;

            //每5秒读一次距离 5秒间距离除以时间等于速度
            speed = (float)distance_gap / 5;

            //速度
            speedSum = speedSum + speed;
            fiveSecondCount++;

            //膝盖压力
            kneepressSum = kneepressSum + pressTimes;

            //垂直摆动
            vertOscSum = vertOscSum + osc;

            //稳定度
            stabilitySum = stabilitySum + stability;

            cadenceSum = cadenceSum + steprate;

            //5秒一次 12次1分钟  --1分钟定时器
            if(fiveSecondCount == 12)
            {
                speedSum = speedSum / 12;
                fiveSecondCount = 0;
                speedPerMin[minuteCount] = (byte)(runningSpeed * 10); //放大10倍
                speedSum = 0;

                //步频
                cadenceSum = cadenceSum / 12;
                cadencePerMinute[minuteCount] = (byte)(cadenceSum & 0xff);
                //打印调试信息
                //com.umeng.socialize.utils.Log.e("cadenceSum-------------------"
                //        + cadenceSum);
                cadenceSum = 0;

                //每分钟统计的数值
                //膝盖压力
                kneepressSum = kneepressSum / 12;
                kneePressPerMin[minuteCount] = (byte)kneepressSum;
                kneepressSum = 0;

                //垂直摆动
                vertOscSum = vertOscSum / 12;
                runningOsc = vertOscSum;
                vertOsciPerMin[minuteCount] = (byte)vertOscSum;
                vertOscSum = 0;

                //稳定度
                stabilitySum = stabilitySum / 12;
                stabilityPerMin[minuteCount] = (byte)stabilitySum;
                stabilitySum = 0;

                minuteCount++;
            }

            //用这种方法更新速度 如果地图不动值就是错的
            fragmentMap.updateSpeed(speed * 3600);
            fragmentRun.updateSpeed(speed * 3600);
            distance_old = distance;

            //速度大于6前提下
            //步频低 让提高步频
            runningSpeed = speed * 36 / 1000;
            if(runningSpeed > 3.5){
                if (steprate < 160) {
                    anino = UtilConstants.eventCadence;
                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] % 6 == 0) {
                        probabilityCalculator(UtilConstants.eventCadence);
                    }
                }

                if(runningOsc >= 22)
                {
                    anino = UtilConstants.eventBounce;
                    indiCaseCount[anino]++;

                    //不要跳着跑
                    if(indiCaseCount[anino] % 10 == 0) {
                        probabilityCalculator(UtilConstants.eventBounce);

                        //告诉他具体发出的技巧
                        indiCaseCount[UtilConstants.eventDrive]++;
                        if(indiCaseCount[UtilConstants.eventDrive] % 5 == 0)
                        {
                            probabilityCalculator(UtilConstants.eventDrive);
                        }
                    }
                }

                if(runningOsc > 16 && runningOsc < 22)
                {
                    anino = UtilConstants.eventEnergy;
                    indiCaseCount[anino]++;

                    //告诉他能量要往前使
                    if(indiCaseCount[anino] % 10 == 0) {
                        probabilityCalculator(UtilConstants.eventEnergy);
                    }
                }
            }

            //有速度并且无提示动画就显示跑动的小人
            if(runningSpeed > 2.0) {

                if(runningSpeed > 5.0) {
                    goodFormCount++;

                    if (goodFormCount > 24) {
                        tts.speak((String) getText(goodFormTalk[goodTalkSwitch]),
                                TextToSpeech.QUEUE_FLUSH, null);
                        goodFormCount = 0;
                        goodTalkSwitch++;
                        if(goodTalkSwitch >= 6)
                        {
                            goodTalkSwitch = 0;
                        }
                    }
                }

                if(runningSpeed < 4.0)
                {
                    walkCount++;
                    if(walkCount > 36)  //3分钟
                    {
                        tts.speak((String) getText(walkTalk[walkTalkSwitch]),
                                TextToSpeech.QUEUE_FLUSH, null);
                        walkCount = 0;
                        walkTalkSwitch++;
                        if(walkTalkSwitch >= 6)
                        {
                            walkTalkSwitch = 0;
                        }
                    }

                    //走路 没在跑
                    anino = UtilConstants.eventWalk;
                    handler.sendEmptyMessage(2);
                }
                else
                {
                    anino = UtilConstants.eventNormalRun;
                    handler.sendEmptyMessage(2);
                }
            }

            //显示静步的小人
            if(runningSpeed == 0.0){
                anino = UtilConstants.eventStill;
                handler.sendEmptyMessage(2);
            }

            //起码跑了2公里
            if(mileage_km > 2)
            {
                if (true == power_flag)
                {   //有足够肌电数据
                    if (power_aver < 60)
                    {
                        //稳定性很差
                        if (stability > 30)
                        {
                            //股四头肌疲劳
                            anino = UtilConstants.eventRest;
                            indiCaseCount[anino]++;
                            //建议休息一下
                            if(indiCaseCount[anino] % 10 == 0)
                            {
                                probabilityCalculator(UtilConstants.eventRest);
                            }

                        }

                    }

                }

            }

        }//end of isStartRun

    }

    //语音的3个因素 说什么 什么条件说(条件集合)  什么时候说(先后或包含)
    //事件对提示信息触发的概率影响 首先要确定事件间的关联关系
    int voiceID = -1;
    int voiceTimer = 0;

    int[] bkIndiCaseCount = new int[20];

    //每秒调一次这个函数
    private void voiceCoachScheduler()
    {
        int maxEvent = -1;
        int secondEvent = -1;
        int maxProbability = -1;

        if(sport_type == UtilConstants.sport_running) {
            //遍历一次probability
            for (int n = 0; n <UtilConstants.eventStill; n++)
            {
                if(probability[n] > maxProbability)
                {
                    //找出最大和第二大的
                    secondEvent = maxEvent;
                    maxEvent = n;
                    maxProbability = probability[n];
                }
            }
        }

        if(maxEvent > 0)
        {
            if(maxEvent != lastVoiceEvent)
            {
                voiceID = UtilConstants.event2str[maxEvent];  //R.string.XXX
                lastVoiceEvent = maxEvent;
                probability[maxEvent] = 0;
            }
            else
            {
                if(secondEvent > 0)
                {
                    voiceID = UtilConstants.event2str[secondEvent];
                    lastVoiceEvent = secondEvent;
                    probability[secondEvent] = 0;
                }

            }
        }

        //最后还要检查一次事件是否还需报警
        //找出最大和第二大但时间不到10秒也不报 backup indiCaseCount
        voiceTimer++;

        //10秒定时
        if(voiceTimer >= 10)
        {
            voiceTimer = 0;
        }
        else
        {
            return;
        }

        //过去10秒间这个事件已经没再发生过
        if(bkIndiCaseCount[lastVoiceEvent] == indiCaseCount[lastVoiceEvent])
        {
            //backup indiCaseCount
            if(sport_type == UtilConstants.sport_running) {
                //遍历一次probability
                for (int n = 0; n <UtilConstants.eventStill; n++) {
                    bkIndiCaseCount[n] = indiCaseCount[n];
                }
            }
            return;
        }

        //backup indiCaseCount
        if(sport_type == UtilConstants.sport_running) {
            //遍历一次probability
            for (int n = 0; n <UtilConstants.eventStill; n++) {
                bkIndiCaseCount[n] = indiCaseCount[n];
            }
        }

        if(voiceID < 0)
        {
            return;
        }

        anino = lastVoiceEvent;

        goodFormCount = 0;

        tts.speak((String)getText(voiceID),
                TextToSpeech.QUEUE_FLUSH, null);

        //显示对应动画并持续5秒
        handler.sendEmptyMessage(2);

        voiceID = -1;
    }

    //
    private void probabilityCalculator(int eventID)
    {
        //属于跑步运动的eventID
        if(eventID <= UtilConstants.eventStill)
        {
            probability[eventID]++; //事件对应的计数

            //频率控制 同一提示不能太频繁的报
            //次数控制 多次报过的 要减少再报
            //优先级控制 高优先级的报了 低优先级的就要过更久才允许再报
            //顺序控制 两个不同的提示不要挨在一起报
            //过了没报的 排队到了要看看是否还需要报
        }

    }

    //动作识别  静止态 运动态 leave

    //执行具体的计划或挑战流程
    public void planComposer(int planID)
    {

    }
}
