package com.keenbrace.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.keenbrace.adapter.LeDeviceListAdapter;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.RunResultDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.greendao.RunResult;
import com.keenbrace.greendao.RunResultDao;
import com.keenbrace.services.BluetoothConstant;
import com.keenbrace.services.BluetoothLeService;
import com.keenbrace.util.ByteHelp;
import com.keenbrace.util.Image;
import com.keenbrace.util.StringUtil;
import com.keenbrace.util.Util;
import com.keenbrace.widget.GifView;

public class MainMenuActivity extends BaseActivity implements
        OnClickListener, OnLongClickListener,TextToSpeech.OnInitListener {
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

    int nowIndex = 0;
    ProgressDialog progressDialog;
    LeDeviceListAdapter leDeviceListAdapter;
    TextView tv_nowday, tv_nowtime;
    private TransferUtility transferUtility;
    int wrCount = 0;

    int distance_old;
    byte[] data;
    private Timer updateTimer;
    private UpdateUiTimerTask updateUiTimerTask;
    private final static int ACQ_TASK_TIMER_PERIOD = 200;
    boolean isAdd = false;
    long bakjldTimes = 0;
    int jldTimes;
    private TextToSpeech tts;
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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openDeives();
                return true;
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

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("");

        // mypDialog.setIcon(R.mipmap.w);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        leDeviceListAdapter = new LeDeviceListAdapter(this);

        tv_nowday = (TextView) findViewById(R.id.tv_nowday);
        tv_nowtime = (TextView) findViewById(R.id.tv_nowtime);
        tv_nowtime.setText(new Date().toGMTString());
        transferUtility = Util.getTransferUtility(this);
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

    }

    public void updateBG() {
        iv_map.setImageResource(R.mipmap.menu_map);
        iv_performance.setImageResource(R.mipmap.menu_performance);
        iv_report.setImageResource(R.mipmap.menu_report);


        iv_trainer.setBackgroundResource(R.mipmap.trainer);
        // iv_animate.setImageResource(R.mipmap.animate);
        // iv_message.setImageResource(R.mipmap.bottom_message);
    }



    File file;
    FileOutputStream fos = null;
    long bak;
    boolean isStartRun;
    RunResult runResult;

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
                    //不是跑步时作为下一种运动的按钮

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
                    
                }
                break;

            case R.id.btn_start:

                btn_start.setVisibility(View.GONE);
                btn_pause.setVisibility(View.VISIBLE);
                handler.post(runnable);
                Date d = new Date();

                if (BluetoothConstant.mConnected) {
                    if (BluetoothConstant.mBluetoothLeService != null
                            && BluetoothConstant.mwriteCharacteristic != null) {
                        BluetoothConstant.mBluetoothLeService.startRun(1, (byte)sport_type, d);
                        handler.sendEmptyMessageDelayed(199, 1000);
                        handler.sendEmptyMessage(5);
                        updateTimer = new Timer();
                        updateUiTimerTask = new UpdateUiTimerTask();
                        updateTimer.schedule(updateUiTimerTask, 0,
                                ACQ_TASK_TIMER_PERIOD);
                        bakjldTimes = System.currentTimeMillis();
                    }

                } else {
                    Toast.makeText(
                            this,
                            "You are not connected to the device, will not be able to monitor the status of your movement",
                            Toast.LENGTH_SHORT).show();
                }

                if (file == null)
                    file = new File(StringUtil.ROOT_FILEPATH + File.separator + bak
                            + ".ble");
                try {
                    if (fos == null)
                        fos = new FileOutputStream(file);
                    else
                        fos = new FileOutputStream(file, true);

                } catch (FileNotFoundException e) {
                }

                isStartRun = true;
                runResult = new RunResult();

                runResult.setStartTime(d.getTime());
                runResult.setDuration(new Long(0L));
                if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
                    runResult.setStartlatitude(fragmentMap.getLatitude());
                    runResult.setStartlongitude(fragmentMap.getLongitude());
                    runResult.setEndlatitude(fragmentMap.getLatitude());
                    runResult.setEndlongitude(fragmentMap.getLongitude());
                }else {
                    runResult.setStartlatitude(fragmentMapGoogle.getLatitude());
                    runResult.setStartlongitude(fragmentMapGoogle.getLongitude());
                    runResult.setEndlatitude(fragmentMapGoogle.getLatitude());
                    runResult.setEndlongitude(fragmentMapGoogle.getLongitude());
                }

                runResult.setId(RunResultDBHelper.getInstance(this).insertKeenBrace(runResult));
                if (fragmentHistory != null)
                    fragmentHistory.updateBleId(runResult.getId());
                /*
                RunWaring rw = new RunWaring();
                rw.setIndex(0+"");
                rw.setCreateTime(System.currentTimeMillis());
                */
                break;

            case R.id.btn_pause:
                tts.speak("pausing workout",
                        TextToSpeech.QUEUE_FLUSH, null);
                btn_end.setVisibility(View.VISIBLE);
                btn_continue.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.GONE);
                break;

            case R.id.btn_bluetooth:
                openDeives();
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
            handler.sendEmptyMessage(19);
            handler.postDelayed(runnable, 1000);

        }

    };
    int blue = 0, yellow = 0, red = 0;
    long mins = 0;
    int[] steprates = new int[]{0, 0, 0, 0, 0};

    int delay_times;

    int anino;  //动画的编号

    int countdown_times;
    String countdown_str;
    //int test_reps = 1;

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
                            tts.speak((String) getText(R.string.tx_running_welcome),
                                    TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    break;

                //检测到有动作 播放动画
                case 2:
                    if (mLastFragment == fragmentHistory) {

                        fragmentHistory.updateAnimation(anino);
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
                    //用这种形式实现循环
                    if (isStartRun) {
                        int lc_gap = distance - distance_old;
                        int speed = lc_gap / 5;
                        fragmentMap.updateSpeed(speed * 3600);
                        fragmentHistory.updateSpeed(speed * 3600);
                        distance_old = distance;
                        handler.sendEmptyMessageDelayed(5, 5000);
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
                                    bkstep, mins, distance);
                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateView(wrCount, steprate, stride,
                                    bkstep, mins, distance);
                    } else {

                        if (mLastFragment == fragmentHistory)
                            fragmentHistory.updateView(wrCount, steprate, stride,
                                    step_true, mins, distance);
                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateView(wrCount, steprate, stride,
                                    step_true, mins, distance);
                    }
                    break;

                case 7:
                    if (mLastFragment == fragmentHistory) {
                        fragmentHistory.updateParaBox(reps, 0, 0, 0);
                    }
                    break;
                case 8:
                    //这里是将TAB隐藏
                    /*
                    ly_tab.setBackgroundColor(Color.argb(alagn,61,196,233));
                    if(alagn>0) {
                        handler.sendEmptyMessageDelayed(8, 1000);
                        alagn=alagn-20;
                    }else
                    {
                        ly_tab.setVisibility(View.GONE);
                        alagn=255;
                    }
                    */
                    break;

                case 19:
                    mins += 1000;
                    if (steprate < 40) {
                        blue += 1;
                    } else if (steprate <= 190) {
                        yellow += 1;

                    } else if (steprate > 190) {
                        red += 1;
                    }

                    //if (mLastFragment == fragmentHistory)
                    //fragmentHistory.updateTime(mins / 1000, blue, yellow, red);

                    if (mLastFragment == fragmentMap)
                        fragmentMap.updateTime(mins);

                    if (mLastFragment == fragmentMapGoogle)
                        fragmentMapGoogle.updateTime(mins);

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
                    if (mins > 5000) {

                        steprate = (95 * steprate + 5 * old_steprate) / 100;
                    }
                    steprates[0] = steprates[1];
                    steprates[1] = steprates[2];
                    steprates[2] = steprates[3];
                    steprates[3] = steprates[4];
                    steprates[4] = steprate;

                    if (mins > 5000) {
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
                    if (steprate < 140 && steprate > 100) {
                        //if (rw8 != null) {
                        //步频太低
                        if (step_history[3] != step_history[0])
                        {
                            straight_spine++;

                            if(straight_spine < 5) {
                                anino = 1;
                                //语音
                                indiCaseCount[anino]++;

                                if(indiCaseCount[anino] > 10) {
                                    updateVoice(R.string.tx_increase_cadence);
                                    //动画和提示框信息
                                    handler.sendEmptyMessage(2);

                                    indiCaseCount[anino] = 0;
                                }
                            }
                            else
                            {
                                //动画和提示框信息
                                anino = 0;

                                indiCaseCount[anino]++;

                                if(indiCaseCount[anino] > 10) {
                                    //语音
                                    updateVoice(R.string.tx_straight_spine);

                                    handler.sendEmptyMessage(2);
                                    straight_spine = 0;

                                    indiCaseCount[anino] = 0;
                                }
                            }
                        }
                        //}
                    }
                    else if(steprate == 0)
                    {
                        anino = 12;

                        indiCaseCount[anino]++;

                        if(indiCaseCount[anino] > 3) {
                            handler.sendEmptyMessage(2);

                            indiCaseCount[anino] = 0;
                        }
                    }
                    else
                    {
                        anino = 10;

                        indiCaseCount[anino]++;

                        if(indiCaseCount[anino] > 3) {
                            handler.sendEmptyMessage(2);

                            indiCaseCount[anino] = 0;
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

                    break;

                case 199:
                    int ss = 0;
                    if(sport_type == UtilConstants.sport_running)
                        ss = 0;

                    if(sport_type == UtilConstants.sport_squat)
                        ss = 3;

                    if (BluetoothConstant.mBluetoothLeService != null
                            && BluetoothConstant.mwriteCharacteristic != null)
                        BluetoothConstant.mBluetoothLeService.startRun(1, (byte)ss,
                                new Date());
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

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.BLEAPI_GATT_FOUNDDEVICE.equals(action)) {
                BluetoothDevice curBleDevice = (BluetoothDevice) intent
                        .getExtras().get("device");
                String strMacAddress = (String) intent.getExtras().get(
                        "macaddress");
                scanAndCheckDevice(curBleDevice, strMacAddress);

            } else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Toast.makeText(MainMenuActivity.this, "device connected",
                        Toast.LENGTH_SHORT).show();
                BluetoothConstant.mConnected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
                    .equals(action)) {
                BluetoothConstant.mConnected = false;

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(MainMenuActivity.this, "device disconnected",
                        Toast.LENGTH_SHORT).show();
                BluetoothConstant.mBluetoothLeService.close();
                BluetoothConstant.mConnected = false;
                BluetoothConstant.mdevice = null;
                handler.sendEmptyMessage(1);

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(MainMenuActivity.this, "services disovered",
                        Toast.LENGTH_SHORT).show();
                if (isStartRun) {
                    Date d = new Date();
                    BluetoothConstant.mBluetoothLeService.startRun(1,(byte)sport_type, d);
                }
                handler.sendEmptyMessage(2);
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //在这个位置接收蓝牙数据 ken
                data = intent
                        .getByteArrayExtra(BluetoothLeService.EXTRA_DATA_BYTE);

                if (tgt == 0 && isAdd) {
                    isAdd = false;
                    jldTimes = (int) (System.currentTimeMillis() - bakjldTimes);
                    handler.sendEmptyMessage(7);
                }
                if (!isAdd & tgt == 100) {
                    isAdd = true;
                    bakjldTimes = System.currentTimeMillis();
                }
            }
        }
    };
    private final Object m_CritObj = new Object();

    private class UpdateUiTimerTask extends TimerTask {
        @Override
        public void run() {
            synchronized (m_CritObj) {
                if (data != null && data.length > 0)
                    //在这个updateView的函数里处理data ken
                    CheckPacket();
            }

        }
    }

    ;

    @Override
    public void onResume() {
        super.onResume();
        tts = new TextToSpeech(this, this);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter
                .addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.BLEAPI_GATT_FOUNDDEVICE);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        //intentFilter.addAction(FragmentMessage.SHOW_MODEL);
        return intentFilter;
    }

    public void scanAndCheckDevice(BluetoothDevice device, String strMacAddress) {
        if (null == device || null == device.getAddress()) {
            return;
        }
        leDeviceListAdapter.addDevice(device);

        if (BluetoothConstant.mdevice == null) {
            BluetoothConstant.mdevice = device;
            final boolean result = BluetoothConstant.mBluetoothLeService
                    .connect(device.getAddress());
            progressDialog.setMessage("");
            progressDialog.show();
        } else {
            if (!BluetoothConstant.mConnected) {
                BluetoothConstant.mBluetoothLeService.connect(device
                        .getAddress());
                progressDialog.setMessage("");
                progressDialog.show();
            }
        }

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

        step = ByteHelp.ByteArrayToShort(new byte[]{data[9], data[10]});    //步数

        int z = ByteHelp.ByteArrayToShort(new byte[]{data[1], data[2]});
        int h = ByteHelp.ByteArrayToShort(new byte[]{data[3], data[4]});
        pressure = (int) Math.sqrt(z * z + h * h);

        int xuzhuan = ByteHelp.byteArrayToInt(new byte[]{data[5], data[6]});    //旋转
        int fanzhuan = ByteHelp.byteArrayToInt(new byte[]{data[7], data[8]});   //翻转

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

        //
        int stride_dis = (int) (legLen * Math.sin((stride / 2) * PI / 180) * 2);

        //
        if (step < 0) {
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


        if (stride > 140 && steprate > 120) {
            if (step_history[3] != step_history[0]) {
                warn_lock = 1;
                //步幅太大
                big_stride_count++;
                if(big_stride_count < 5) {
                    anino = 2;
                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] > 10) {
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

                    if(indiCaseCount[anino] > 10) {
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

                if (count >= 4 && steprate > 130) {
                    anino = 6;

                    indiCaseCount[anino]++;

                    if(indiCaseCount[anino] > 10) {
                        //触地时间过长
                        updateVoice(R.string.tx_lift_faster);

                        handler.sendEmptyMessage(2);
                        indiCaseCount[anino] = 0;
                    }
                }


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


                if (pressure > UtilConstants.Weight * 9.8f * 13) {
                    //膝盖压力太大
                    if (steprate > 130)
                    {
                        bend_kneeNelbow++;

                        if( bend_kneeNelbow < 5) {
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
                if (xuzhuan > 80 && fanzhuan < -50) {
                    //if (steprate > 100)
                        //rw4 = getWarning(4, rw4);
                }

                //内旋
                if (xuzhuan < -80 && fanzhuan > 50) {
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
        // int jrll = FftUtil.getJrll(power);

        bak = System.currentTimeMillis();

        index++;


        float distance = 0f;
        if(UtilConstants.MapType==UtilConstants.MAP_GAODE)
                fragmentMap.getDistance();
        else
            fragmentMapGoogle.getDistance();

        if (distance >= 5000) {
            distance += distance;
        } else {
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

    int old_reps = 0;
    int reps = 0;

    void CheckPacket() {
        switch (data[0])
        {
            //实时数据包
            case (byte)0xa5:
                //临时 testing
                if(data[1] == 0xa5)
                {
                    if(data[2] == 0xa5)
                    {
                        reps = ByteHelp.byteArrayToInt(new byte[]{data[3], data[4]});

                        if(reps == 0)
                        {
                            isAnyMove = false;
                        }
                        else
                        {
                            //将每一个计数读出来
                            if(reps != old_reps)
                            {
                                handler.sendEmptyMessage(7);
                                String s;
                                s = "" + reps;
                                tts.speak(s,
                                        TextToSpeech.QUEUE_FLUSH, null);
                                old_reps = reps;
                            }
                        }
                        return;
                    }
                }

                updateView();
                break;

            //运动参数及提示
            /*
            case (byte)0xab:
                //动作次数
                int reps = ByteHelp.byteArrayToInt(new byte[]{data[2], data[3]});
                if(reps == 0)
                {
                    isAnyMove = false;
                }
                else
                {
                    //将每一个计数读出来
                    if(reps != old_reps)
                    {
                        String s;
                        s = "" + reps;
                        tts.speak(s,
                                TextToSpeech.QUEUE_FLUSH, null);
                        old_reps = reps;
                    }
                }

                int duration = ByteHelp.byteArrayToInt(new byte[]{data[4], data[5]});

                int bias = (int)data[6];
                int stability = (int)data[7];
                int muscle = (int)data[8];

                anino = (int)data[9];

                //更新参数框
                if (mLastFragment == fragmentHistory) {
                    fragmentHistory.updateParaBox(reps, muscle, duration, stability);
                }

                //提示信息不为空
                if(anino!=0)
                {
                    handler.sendEmptyMessage(2);
                }

                //语音报数
                //updateVoice(anino);
                break;
                */
        }
    }

    /*
    private RunWaring getWarning(int index, RunWaring brw) {

        nowIndex = index;
        if (brw != null) {
            // float jl = AMapUtils.calculateLineDistance(
            // new LatLng(fragmentMap.getLatitude(), fragmentMap
            // .getLongitude()),
            // new LatLng(brw.getLatitude(), brw.getLongitude()));
            int sublc = lc - brw.getLc();
            if (sublc >= 4000) {
                RunWaring rw = new RunWaring();
                rw.setIndex(index + "");
                if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
                    rw.setLatitude(fragmentMap.getLatitude());
                    rw.setLongitude(fragmentMap.getLongitude());
                }else
                {
                    rw.setLatitude(fragmentMapGoogle.getLatitude());
                    rw.setLongitude(fragmentMapGoogle.getLongitude());
                }

                        rw.setCreateTime(System.currentTimeMillis());
                rw.setLc(lc);
                rw.setRunId(keenBrace_sports.getId());
                //ken
                KeenbraceDBHelper.getInstance(this).insertRunWaring(rw);
                wrCount++;
                // iv_messagecount.setVisibility(View.VISIBLE);

                return rw;
            } else {
                return brw;
            }
        } else {
            RunWaring rw = new RunWaring();
            rw.setIndex(index+"");
            if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
                rw.setLatitude(fragmentMap.getLatitude());
                rw.setLongitude(fragmentMap.getLongitude());
            }else
            {
                rw.setLatitude(fragmentMapGoogle.getLatitude());
                rw.setLongitude(fragmentMapGoogle.getLongitude());
            }
            rw.setCreateTime(System.currentTimeMillis());
            rw.setLc(lc);
            wrCount++;
            rw.setRunId(keenBrace_sports.getId());
            KeenbraceDBHelper.getInstance(this).insertRunWaring(rw);

            return rw;
        }

    }
    */

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
        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
        }
        isStartRun = false;

        //无法创建文件 ken
        try {
            fos.close();
            fos = null;
        } catch (IOException e) {
        }

        runResult.setDuration(mins);
        runResult.setMileage(distance);
        //runResult.setFileName(file.getAbsolutePath());    //ken
        runResult.setEndTime(bak);
        if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
            runResult.setLatLngs(fragmentMap.getMap());

            runResult.setEndlatitude(fragmentMap.getLatitude());
            runResult.setEndlongitude(fragmentMap.getLongitude());
        }else
        {
            runResult.setLatLngs(fragmentMapGoogle.getMaps());

            runResult.setEndlatitude(fragmentMapGoogle.getLatitude());
            runResult.setEndlongitude(fragmentMapGoogle.getLongitude());
        }
        //总共发生过的警报数
        //runResult.setSumwarings(wrCount);
        RunResultDBHelper.getInstance(this).updateRunResult(runResult);


        btn_end.setVisibility(View.GONE);
        btn_pause.setVisibility(View.GONE);
        btn_continue.setVisibility(View.GONE);
        btn_start.setVisibility(View.VISIBLE);

        /*
        TransferObserver observer = transferUtility.upload(
                UtilConstants.BUCKET_NAME, file.getName(), file);
                */

        Intent intent = new Intent();
        intent.putExtra("bleData", runResult);
        intent.putExtra("sport_type", sport_type);
        intent.setClass(this, ViewRecordActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View arg0) {

        return false;
    }

    AlertDialog popupWindow;

    public void openDeives() {
        // leDeviceListAdapter.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        popupWindow = builder.create();
        popupWindow.show();
        Window window = popupWindow.getWindow();
        window.setContentView(R.layout.ble_select);
        popupWindow.setCancelable(true);
        ListView listview = (ListView) window.findViewById(R.id.blelist);
        listview.setAdapter(leDeviceListAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                BluetoothDevice ble = leDeviceListAdapter.getDevice(arg2);
                if (BluetoothConstant.mdevice != null) {
                    if (BluetoothConstant.mConnected) {
                        BluetoothConstant.mBluetoothLeService.close();
                    }
                    BluetoothConstant.mdevice = ble;
                    final boolean result = BluetoothConstant.mBluetoothLeService
                            .connect(ble.getAddress());
                    progressDialog.setMessage("正在连接...");
                    progressDialog.show();
                }
            }
        });
        Button cancle = (Button) window.findViewById(R.id.cancel);
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.show();
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
                                                .startRun(0, (byte)sport_type, new Date());
                                    isStartRun = false;
                                    try {
                                        fos.close();
                                        fos = null;
                                    } catch (IOException e) {
                                    }
                                    runResult.setDuration(mins);
                                    //keenBrace_sports.setSumscore(80);
                                    // ble.setCadence((int)
                                    // bpChart.getAverage());
                                    // ble.setStride((int)
                                    // bfChart.getAverage());
                                    runResult.setMileage(distance);
                                    //runResult.setFileName(file.getAbsolutePath());
                                    runResult.setEndTime(bak);
                                    if(UtilConstants.MapType==UtilConstants.MAP_GAODE)
                                        runResult.setLatLngs(fragmentMap.getMap());
                                    else
                                        runResult.setLatLngs(fragmentMapGoogle.getMaps());
                                    //runResult.setSumwarings(wrCount);
                                    RunResultDBHelper.getInstance(MainMenuActivity.this).updateRunResult(runResult);
                                } catch (Exception e) {

                                }
                                if (BluetoothConstant.mConnected
                                        && BluetoothConstant.mBluetoothLeService != null)
                                    BluetoothConstant.mBluetoothLeService
                                            .close();
                                finish();
                            }
                        })
                .setNegativeButton(getText(R.string.cancle),
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

        //已经开始运动后就不说这句
        if(isStartRun == false) {
            tts.speak("beginning workout",
                    TextToSpeech.QUEUE_FLUSH, null);
        }

        btn_start.performClick();

        //得到运动类型 再传到fragment
        sport_type = this.getIntent().getIntExtra("sport_type", 0);
        data2Fragment.putInt("sport_type", sport_type);

        //testing
        /*
        countdown_times = 40;
        fragmentHistory.CountDownBegin();
        handler.sendEmptyMessage(3);
        */
    }
}
