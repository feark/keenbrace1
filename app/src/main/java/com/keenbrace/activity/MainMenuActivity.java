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

    FragmentRun fragmentHistory;
    FragmentMapGoogle fragmentMapGoogle;
    FragmentMap fragmentMap;
    FragmentReport fragmentReport;
    RelativeLayout rl_map, rl_performance,rl_report;
    private Fragment mLastFragment;

    //------------------------------------------------------
    //运动参数相关
    Boolean isAnyMove = false;

    int[] indiCaseCount = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //------------------------------------------------------

    //根据不同种类的运动显示不同的动画或图片 ken
    int sport_type = 0;
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

    int distance_old;
    byte[] data;
    private Timer updateTimer;
    private UpdateUiTimerTask updateUiTimerTask;
    private final static int ACQ_TASK_TIMER_PERIOD = 200;
    boolean isAdd = false;
    long bakjldTimes = 0;
    private TextToSpeech tts;

    CommonResult commonResult;

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
        fragmentReport = new FragmentReport();
        changeFragment(this, fragmentReport, null, null);

        if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
            fragmentMap = new FragmentMap();
            changeFragment(this, fragmentMap, null, null);
        }else {
            fragmentMapGoogle = new FragmentMapGoogle();
            changeFragment(this, fragmentMapGoogle, null, null);
        }

        fragmentHistory = new FragmentRun();
        //得到运动类型 再传到fragment
        sport_type = this.getIntent().getIntExtra("sport_type",0);
        data2Fragment.putInt("sport_type", sport_type);

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
                this.setActionBarTitle(getString(R.string.tx_pullup));
        }

        fragmentHistory.setArguments(data2Fragment);
        changeFragment(this, fragmentHistory, null, null);

        if(sport_type != UtilConstants.sport_running)
        {
            //不是跑步的话两个按钮就变成上一种 或 下一种运动
            switch2map.setImageResource(R.mipmap.next_training);
            switch2map.setVisibility(View.VISIBLE);

            switch2runner.setImageResource(R.mipmap.prev_training);
            switch2runner.setVisibility(View.VISIBLE);
        }

        tv_nowday = (TextView) findViewById(R.id.tv_nowday);
        tv_nowtime = (TextView) findViewById(R.id.tv_nowtime);
        tv_nowtime.setText(new Date().toGMTString());
        transferUtility = Util.getTransferUtility(this);

        tv_duration = (TextView) findViewById(R.id.tv_duration);

        //开始1秒的循环
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
            if (BluetoothConstant.mBluetoothLeService != null
                    && BluetoothConstant.mwriteCharacteristic != null) {
                //BluetoothConstant.mBluetoothLeService.startRun(1, (byte)sport_type, d);
                handler.sendEmptyMessageDelayed(199, 1000);
                handler.sendEmptyMessage(5);
                updateTimer = new Timer();
                updateUiTimerTask = new UpdateUiTimerTask();
                updateTimer.schedule(updateUiTimerTask, 0,
                        ACQ_TASK_TIMER_PERIOD);
                bakjldTimes = System.currentTimeMillis();
            }

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
        Intent intent = new Intent();

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

                    if (fragmentHistory == null)
                        fragmentHistory = new FragmentRun();
                    changeFragment(this, fragmentHistory, null, null);
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

                Date d = new Date();

                commonResult.setStartTime(d.getTime());
                commonResult.setDuration(new Long(0L));
                commonResult.setType(sport_type);

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
                if (fragmentHistory != null)
                    fragmentHistory.updateBleId(commonResult.getId());

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

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if(sport_type == UtilConstants.sport_running) {
                handler.sendEmptyMessage(19);
            }

            handler.postDelayed(runnable, 1000);

        }

    };


    long milli_second = 0;
    int[] steprates = new int[]{0, 0, 0, 0, 0};

    int delay_times;

    int anino;  //动画的编号

    int countdown_times;
    String countdown_str;
    //int test_reps = 1;
    int indiCountdown = 100;

    byte speedPerMin[] = new byte[600];
    int fiveSecondCount = 0;
    int minuteCount = 0;
    float speedSum = 0.0f;

    int du_hour = 0, du_min = 0, du_sec = 0;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //延时的方法 ken
                    if (delay_times > 0) {
                        //每秒检查一次有没有在运动
                        delay_times--;
                        handler.sendEmptyMessageDelayed(1, 1000);



                        /*  testing
                        String s;
                        s = "" + test_reps;
                        tts.speak(s,
                                TextToSpeech.QUEUE_FLUSH, null);
                        test_reps++;

                        if (mLastFragment == fragmentHistory) {
                            fragmentHistory.updateParaBox(test_reps, 10, 30, 30);
                        }
                        */
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
                    if (mLastFragment == fragmentHistory) {
                        if(sport_type == UtilConstants.sport_running) {
                            fragmentHistory.updateAnimation(anino);
                        }
                        else {
                            fragmentHistory.updateAnimation(0);
                        }
                    }
                    break;

                case 3:
                    //倒计时语音
                    if (countdown_times > 0) {
                        countdown_times--;

                        fragmentHistory.DuringCountDown(0, countdown_times);

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
                        fragmentHistory.CountDownEnd();
                    }
                    break;

                case 5:
                    //用这种形式实现自循环
                    if (isStartRun) {
                        int distance_gap = distance - distance_old;

                        //每5秒读一次距离
                        float speed = (float)distance_gap / 5;

                        speedSum = speedSum + speed;
                        fiveSecondCount++;

                        //5秒一次 12次1秒
                        if(fiveSecondCount == 12)
                        {
                            speedSum = speedSum / 12;
                            fiveSecondCount = 0;
                            speedPerMin[minuteCount] = (byte)(speedSum * 10); //放大10倍
                            minuteCount++;
                            speedSum = 0;
                        }

                        //用这种方法更新速度 如果地图不动值就是错的
                        fragmentMap.updateSpeed(speed * 3600);
                        fragmentHistory.updateSpeed(speed * 3600);
                        distance_old = distance;
                        handler.sendEmptyMessageDelayed(5, 5000);
                        //打印调试信息
                        com.umeng.socialize.utils.Log.e("speed-------------------"
                                + speed);
                    }
                    break;
                case 6:

                    //fragmentReport.addLineEntry(power_valid);
                    //fragmentReport.addBarEntry(osc, (int) ya, steprate);
                    //
                    if (updateStep != step_true) {

                        if (mLastFragment == fragmentHistory)
                            fragmentHistory.updateView(wrCount, steprate, stride,
                                    bkstep, milli_second, distance);
                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateView(wrCount, steprate, stride,
                                    bkstep, milli_second, distance);
                    } else {

                        if (mLastFragment == fragmentHistory)
                            fragmentHistory.updateView(wrCount, steprate, stride,
                                    step_true, milli_second, distance);
                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateView(wrCount, steprate, stride,
                                    step_true, milli_second, distance);
                    }
                    break;

                case 7: //测试用 leave
                    if (mLastFragment == fragmentHistory) {
                        fragmentHistory.updateParaBox(reps, 0, 0, 0);
                    }
                    break;

                case 8:
                    //更新跑步以外运动的参数框
                    if (mLastFragment == fragmentHistory) {
                        fragmentHistory.updateParaBox(reps, muscleDec, commDuration, stability);
                    }
                    break;

                case 19:    //每秒一次循环
                    tv_nowtime.setText(new Date().toGMTString());

                    if(isStartRun) {
                        milli_second += 1000;

                        //实时更新运动的总时长
                        du_sec++;
                        if(du_sec >= 60)
                        {
                            du_min++;
                            if(du_min >= 60){
                                du_hour++;
                                du_min = 0;
                            }
                            du_sec = 0;
                        }

                        String str_du = String.format("%02d:%02d:%02d",du_hour,du_min,du_sec);
                        tv_duration.setText("Duration :  "+str_du);

                        //if (mLastFragment == fragmentHistory)
                        //fragmentHistory.updateTime(milli_second / 1000, blue, yellow, red);

                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateTime(str_du);

                        if (mLastFragment == fragmentMapGoogle)
                            fragmentMapGoogle.updateTime(str_du);

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
                                    count++;
                                }
                            }
                            steprate = totalStep / 5;

                        }

                        old_steprate = steprate;
                        if (steprate < 150) {
                            //if (rw8 != null) {
                            //步频太低
                            if (steprate > 0) {
                                straight_spine++;

                                if (straight_spine < 5) {
                                    anino = 1;
                                    //语音
                                    indiCaseCount[anino]++;

                                    if (indiCaseCount[anino] > 10) {
                                        updateVoice(R.string.tx_increase_cadence);
                                        //动画和提示框信息
                                        handler.sendEmptyMessage(2);

                                        indiCaseCount[anino] = 0;
                                    }
                                } else {
                                    //动画和提示框信息
                                    anino = 0;

                                    indiCaseCount[anino]++;

                                    if (indiCaseCount[anino] > 10) {
                                        //语音
                                        updateVoice(R.string.tx_straight_spine);

                                        handler.sendEmptyMessage(2);
                                        straight_spine = 0;

                                        indiCaseCount[anino] = 0;
                                    }
                                }
                            }
                            //}
                        } else if (steprate == 0) {
                            //站着没动 显示停止的小人
                            anino = 12;

                            indiCaseCount[anino]++;

                            if (indiCaseCount[anino] > 5) {
                                handler.sendEmptyMessage(2);

                                indiCaseCount[anino] = 0;
                            }
                        } else if (steprate > 150) {
                            if (anino != 10) {
                                indiCountdown--;

                                if (indiCountdown == 0) {
                                    anino = 10;
                                    indiCountdown = 100;
                                }
                            } else {
                                anino = 10;

                                indiCaseCount[anino]++;

                                if (indiCaseCount[anino] > 3) {
                                    handler.sendEmptyMessage(2);

                                    indiCaseCount[anino] = 0;
                                }
                            }
                        }

                        bpi = 0;


                        step_history[second] = step_true;
                        second++; //

                        if (second >= 4)
                            second = 0;

                        if (power_valid != 10) {
                            power_record[power_index] = power_valid;
                            power_index++;
                        }

                        if (power_index >= 200) {
                            power_index = 0;
                            power_flag = true;
                        }
                    }
                    break;

                case 199:
                    int st = 0;
                    if(sport_type == UtilConstants.sport_running)
                        st = 0;

                    if(sport_type == UtilConstants.sport_squat)
                        st = 3;

                    if(sport_type == UtilConstants.sport_pullup)
                        st = 5;

                    if(sport_type == UtilConstants.sport_dumbbell)
                        st = 6;

                    if(sport_type == UtilConstants.sport_plank)
                        st = 9;

                    if (BluetoothConstant.mBluetoothLeService != null
                            && BluetoothConstant.mwriteCharacteristic != null)
                        BluetoothConstant.mBluetoothLeService.startRun(1, (byte)st,
                                new Date());

                    application.setIsSendBleEnd(false);
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

    ;

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




    int bpi = 0;
    int bsi = 0;
    int progress = 0;

    int step = 0, stride = 0, distance = 0, index= 0, steprate = 0, osc = 0, pressure = 0, tgt = 0;
    int[] bs = new int[5];
    //List<RunWaring> rws = new ArrayList<RunWaring>();
    int count;
    //int jzdfbOne, jzdfbTwo;
    int second; //
    int[] step_history = new int[4]; //
    int[] power_record = new int[200];
    int power_index;
    boolean power_flag = false;
    int power_valid = 0;

    boolean iangle_flag = false;

    int step_true = 0;
    int old_step = 0;
    int updateStep = 0;
    int bkstep = 0;
    float PI = 3.1415926f;

    int old_steprate = 0;

    int warn_lock = 0;

    //----------------------- inherit alarms
    int big_stride_count = 0;
    int bend_kneeNelbow = 0;
    int straight_spine = 0;

    void updateView() {

        int z = ByteHelp.ByteArrayToShort(new byte[]{data[1], data[2]});
        int h = ByteHelp.ByteArrayToShort(new byte[]{data[3], data[4]});
        pressure = (int) Math.sqrt(z * z + h * h);

        int twist = ByteHelp.byteArrayToInt(new byte[]{data[5], data[6]});    //旋转
        int flip = ByteHelp.byteArrayToInt(new byte[]{data[7], data[8]});   //翻转

        step = ByteHelp.ByteArrayToShort(new byte[]{data[9], data[10]});    //步数

        int iangle = ByteHelp.byteArrayToInt(new byte[]{data[11], data[12]});   //夹角

        osc = data[13];

        int stability = data[14];

        int tgt = data[15];

        stride = data[16] & 0xFF;

        int power = data[17] & 0xFF;

        int strike = data[18] & 0xFF;


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


        if (stride > 130 && steprate > 120) {
            if (step_history[3] != step_history[0]) {
                warn_lock = 1;

                //步幅太大 达到危险
                if(stride > 150) {
                    big_stride_count++;
                }

                if(big_stride_count < 5) {
                    anino = 2;
                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] > 30) {
                        //语音
                        updateVoice(R.string.tx_decrease_stride_l);
                        //动画和提示框信息
                        handler.sendEmptyMessage(2);

                        indiCaseCount[anino] = 0;
                    }
                }
                else
                {
                    anino = 8;
                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] > 60) {
                        //语音
                        updateVoice(R.string.tx_land_underneath);
                        //动画和提示框信息

                        handler.sendEmptyMessage(2);

                        big_stride_count = 0;
                        indiCaseCount[anino] = 0;
                    }
                }
            }
            else
            {
                warn_lock = 0;
            }
        }


        if (step_history[3] != step_history[0]) {
            /*
            if(stability < 6)
            {
                anino = 2;

                indiCaseCount[anino]++;

                if(indiCaseCount[anino] > 10) {
                    //语音
                    updateVoice(R.string.tx_enhance_stability);
                    //动画和提示框信息

                    handler.sendEmptyMessage(2);

                    indiCaseCount[anino] = 0;
                }
            }
            */

            if (tgt == 100) {
                count++;

                if (count >= 10 && steprate > 130) {
                    if(steprate < 160) {
                        anino = 6;

                        indiCaseCount[anino]++;

                        if (indiCaseCount[anino] > 15) {
                            //触地时间过长
                            updateVoice(R.string.tx_increase_cadence);

                            handler.sendEmptyMessage(2);
                            indiCaseCount[anino] = 0;
                        }

                    }
                }

                /* 暂没有有效得到iangle夹角的方法 不报这个
                if (iangle > 130 && steprate < 180) {
                    anino = 7;

                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] > 20) {
                        updateVoice(R.string.tx_flex_knee);

                        handler.sendEmptyMessage(2);
                        //脚伸太直
                        iangle_flag = true;

                        indiCaseCount[anino] = 0;
                    }
                }
                */

                if (pressure > UtilConstants.Weight * 9.8f * 6) {
                    //膝盖压力太大
                    if (steprate > 130)
                    {
                        bend_kneeNelbow++;

                        if( bend_kneeNelbow < 6) {
                            anino = 3;
                            indiCaseCount[anino]++;

                            if(indiCaseCount[anino] > 10) {
                                //语音
                                updateVoice(R.string.tx_land_softer);
                                //动画和提示框信息

                                handler.sendEmptyMessage(2);

                                indiCaseCount[anino] = 0;
                            }
                        }
                        else
                        {
                            anino = 9;
                            indiCaseCount[anino]++;

                            if(indiCaseCount[anino] > 10) {
                                //语音
                                updateVoice(R.string.tx_bend_knee_n_elbow);
                                //动画和提示框信息

                                handler.sendEmptyMessage(2);

                                bend_kneeNelbow = 0;

                                indiCaseCount[anino] = 0;
                            }
                        }
                    }
                }

                //这两个还没实现
                //内旋
                if (twist > 80 && flip < -50) {
                    //if (steprate > 100)
                        //rw4 = getWarning(4, rw4);
                }

                //内旋
                if (twist < -80 && flip > 50) {
                    //if (steprate > 100)
                        //rw5 = getWarning(5, rw5);
                }

            } else {
                count = 0;
            }

            if (true == iangle_flag) {
                iangle_flag = false;


                int power_aver = 0;
                for (int n = 0; n < 200; n++) {
                    power_aver += power_record[n];
                }
                power_aver = power_aver / 200;

                if (true == power_flag) {
                    if (power_aver < 60) {
                        //股四头肌疲劳
                        if (steprate > 130)
                        {
                            anino = 11;
                            indiCaseCount[anino]++;

                            if(indiCaseCount[anino] > 10) {
                                //语音
                                updateVoice(R.string.tx_take_rest);
                                //动画和提示框信息

                                handler.sendEmptyMessage(2);
                                indiCaseCount[anino] = 0;
                            }
                        }
                            //rw6 = getWarning(6, rw6);
                    }
                }
            }
        }

        /*
        if(warn_lock == 0) {
            if (osc > 25) {
                //垂直摆动太大
                if (steprate > 130)
                {
                    anino = 11;

                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] > 10) {
                        //语音
                        updateVoice(R.string.tx_lower_gravity);
                        //动画和提示框信息

                        handler.sendEmptyMessage(2);
                        indiCaseCount[anino] = 0;
                    }
                }
            }
        }
        */

        bak = System.currentTimeMillis();

        index++;


        float mapDistance = 0f;
        if(UtilConstants.MapType==UtilConstants.MAP_GAODE)
            mapDistance = fragmentMap.getDistance();
        else
            mapDistance = fragmentMapGoogle.getDistance();

        if(mapDistance != 0) {
            //if (distance >= 5000) {
                //distance += mapDistance;
            //} else {
                if (updateStep != step_true) {
                    distance += stride_dis;
                }
           // }
        }
        else
        {
            if (updateStep != step_true) {
                distance += stride_dis;
            }
        }

        updateStep = step_true;
        old_step = step;
        handler.sendEmptyMessage(6);

    }

    //data接收到蓝牙数据包 在这里发出语音 ken
    void updateVoice(int no)
    {
        tts.speak((String)getText(no),
                TextToSpeech.QUEUE_FLUSH, null);
    }

    static int old_reps = 0;
    static int reps = 0;
    int commDuration;
    int bias;
    int stability;
    int muscleDec;
    String strCommResult;
    int xAngle, yAngle, zAngle, xAcc, yAcc, zAcc;

    void CheckPacket() {

        switch (data[0])
        {
            //实时数据包
            case (byte)0xa5:

                if(data[19] != 0x03)
                {
                    break;
                }

                try{
                    if(fos != null) {
                        fos.write(data, 0, 20);
                    }
                }catch (IOException e) {
                }

                updateView();
            break;

            //跑步以外的其他运动参数及提示
            case (byte)0xab:

                if(data[15] != 0x03)
                {
                    break;
                }

                try{
                    if(fos != null) {
                        fos.write(data, 0, 16);
                    }
                }catch (IOException e) {
                }

                //动作次数
                reps = ByteHelp.byteArrayToInt(new byte[]{data[2], data[3]});
                if(reps == 0)
                {
                    isAnyMove = false;
                }
                else if((reps - old_reps) > 5)
                {
                    break;
                }
                else
                {
                    //将每一个计数读出来
                    //如果是平板支撑 一直变化的rep说明没处在运动姿势 leave
                    if(sport_type == UtilConstants.sport_plank)
                    {
                        reps++;
                    }
                    else
                    {
                        if(reps != old_reps)
                        {
                            commDuration = ByteHelp.byteArrayToInt(new byte[]{data[4], data[5]});

                            if(sport_type == UtilConstants.sport_dumbbell) {
                                if (commDuration < 1) {
                                    reps = reps / 2;
                                }
                            }

                            strCommResult = "" + reps;
                            tts.speak(strCommResult,
                                    TextToSpeech.QUEUE_FLUSH, null);

                            //播动画
                            handler.sendEmptyMessage(2);
                        }
                    }

                }

                commDuration = ByteHelp.byteArrayToInt(new byte[]{data[4], data[5]});

                bias = (int)data[6];
                stability = (int)data[7];
                muscleDec = (int)data[8];

                //X angle
                xAngle = (int)data[9];
                if(xAngle > 0x80)
                {
                    xAngle = xAngle - 0xFF - 1;
                }
                //X acc
                xAcc = (int)data[10];

                //X angle
                yAngle = (int)data[11];
                if(yAngle > 0x80)
                {
                    yAngle = yAngle - 0xFF - 1;
                }
                //X acc
                yAcc = (int)data[12];

                //X angle
                zAngle = (int)data[13];
                if(zAngle > 0x80)
                {
                    zAngle = zAngle - 0xFF - 1;
                }
                //X acc
                zAcc = (int)data[14];


                old_reps = reps;

                //更新参数
                handler.sendEmptyMessage(8);

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
        handler.removeCallbacks(runnable);

        if (BluetoothConstant.mBluetoothLeService != null
                && BluetoothConstant.mwriteCharacteristic != null)
            BluetoothConstant.mBluetoothLeService.startRun(0, (byte)sport_type, new Date());

        application.setIsSendBleEnd(true);

        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
        }
        isStartRun = false;

        try {
            fos.close();
            fos = null;
        } catch (IOException e) {
        }

        commonResult.setDuration(milli_second);
        commonResult.setMileage(distance);
        commonResult.setCadence(steprate);
        commonResult.setStep((long) step_true);

        commonResult.setSpeedPerMinute(speedPerMin);
        commonResult.setMinuteCount(minuteCount);

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
                                    handler.removeCallbacks(runnable);
                                    btn_pause.setVisibility(View.GONE);
                                    btn_start.setVisibility(View.VISIBLE);

                                    if (BluetoothConstant.mBluetoothLeService != null
                                            && BluetoothConstant.mwriteCharacteristic != null)
                                        BluetoothConstant.mBluetoothLeService
                                                .startRun(0, (byte) sport_type, new Date());

                                    application.setIsSendBleEnd(true);

                                    isStartRun = false;
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
        if(!application.getIsSendBleEnd())
        {
            if (BluetoothConstant.mBluetoothLeService != null
                    && BluetoothConstant.mwriteCharacteristic != null)
                BluetoothConstant.mBluetoothLeService.startRun(0, (byte)sport_type, new Date());
        }

        //testing
        /*
        countdown_times = 40;
        fragmentHistory.CountDownBegin();
        handler.sendEmptyMessage(3);
        */
    }
}
