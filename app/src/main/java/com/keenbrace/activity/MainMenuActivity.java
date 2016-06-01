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
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AnimationUtils;
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
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.services.BluetoothConstant;
import com.keenbrace.services.BluetoothLeService;
import com.keenbrace.storage.BleData;
import com.keenbrace.greendao.RunWaring;
import com.keenbrace.util.ByteHelp;
import com.keenbrace.util.StringUtil;
import com.keenbrace.util.Util;

import rx.subscriptions.CompositeSubscription;

public class MainMenuActivity extends BaseActivity implements
        OnClickListener, OnLongClickListener,TextToSpeech.OnInitListener {
    public CompositeSubscription _subscriptions = new CompositeSubscription();
    FragmentRun fragmentHistory;
    FragmentMapGoogle fragmentMapGoogle;
    FragmentMap fragmentMap;
    FragmentReport fragmentReport;
    RelativeLayout rl_map, rl_performance,rl_report;
    private Fragment mLastFragment;
    Button btn_start, btn_stop, btn_end, btn_continue;
    LinearLayout ly_tab;
    ImageView iv_map, iv_performance, iv_report, iv_trainer;
    int nowIndex = 0;
    ProgressDialog progressDialog;
    LeDeviceListAdapter leDeviceListAdapter;
    TextView tv_nowday, tv_nowtime;
    private TransferUtility transferUtility;
    int wrCount = 0;

    int lc_old;
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
        this.setActionBarTitle("Running");
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openDeives();
                return true;
            }
        });



        ly_tab = (LinearLayout) findViewById(R.id.ly_tab);
        rl_map = (RelativeLayout) findViewById(R.id.rl_map);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        rl_report = (RelativeLayout) findViewById(R.id.rl_report);
        rl_performance = (RelativeLayout) findViewById(R.id.rl_performance);

        rl_map.setOnClickListener(this);
        rl_report.setOnClickListener(this);
        rl_performance.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_end = (Button) findViewById(R.id.btn_end);
        btn_end.setOnClickListener(this);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);

        updateViewIcon();

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
        changeFragment(this, fragmentHistory, null, null);

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
        handler.sendEmptyMessageDelayed(8, 5000);
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
    KeenBrace keenBrace;


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        ly_tab.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(8, 1000);
        switch (v.getId()) {
            case R.id.rl_report:
                updateBG();
                iv_report.setImageResource(R.mipmap.menu_report_sel);

                if (fragmentReport == null)
                    fragmentReport = new FragmentReport();
                changeFragment(this, fragmentReport, null, null);
                break;
            case R.id.iv_trainer:
                updateBG();
                iv_trainer.setBackgroundResource(R.mipmap.trainer_selected);
                if (keenBrace != null) {
                    intent.putExtra("id", keenBrace.getId());
                    intent.setClass(this, MessageActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.rl_map:
                if (isStartRun) {
                    btn_stop.setVisibility(View.VISIBLE);
                } else {
                    btn_start.setVisibility(View.VISIBLE);

                }
                iv_trainer.setVisibility(View.GONE);
                btn_end.setVisibility(View.GONE);
                btn_continue.setVisibility(View.GONE);
                updateBG();
                iv_map.setImageResource(R.mipmap.menu_map_sel);
                if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
                    if (fragmentMap == null)
                        fragmentMap = new FragmentMap();
                    changeFragment(this, fragmentMap, null, null);
                }else
                {
                    if (fragmentMapGoogle == null)
                        fragmentMapGoogle = new FragmentMapGoogle();
                    changeFragment(this, fragmentMapGoogle, null, null);
                }
                // ly_tab.setVisibility(View.GONE);
                break;
            // case R.id.rl_animate:
            // updateBG();
            // iv_animate.setImageResource(R.mipmap.animate_y);
            // if (fragmentModel == null)
            // fragmentModel = new FragmentModel();
            // changeFragment(this, fragmentModel, null, null);
            // fragmentModel.update(UtilConstants.WaringMap.get("" + nowIndex));
            // break;
            // case R.id.rl_message:
            //
            // iv_messagecount.setVisibility(View.GONE);
            // updateBG();
            // iv_message.setImageResource(R.mipmap.bottom_message_y);
            // if (fragmentMessage == null)
            // fragmentMessage = new FragmentMessage();
            // changeFragment(this, fragmentMessage, null, null);
            //
            // break;
            case R.id.rl_performance:

                if (isStartRun) {
                    btn_stop.setVisibility(View.VISIBLE);
                } else {
                    btn_start.setVisibility(View.VISIBLE);

                }
                btn_end.setVisibility(View.GONE);
                btn_continue.setVisibility(View.GONE);
                updateBG();
                iv_trainer.setVisibility(View.VISIBLE);

                iv_report.setVisibility(View.VISIBLE);
                iv_performance.setImageResource(R.mipmap.menu_performance_sel);
                if (fragmentHistory == null)
                    fragmentHistory = new FragmentRun();
                changeFragment(this, fragmentHistory, null, null);
                break;
            case R.id.btn_start:
                btn_start.setVisibility(View.GONE);
                btn_stop.setVisibility(View.VISIBLE);
                handler.post(runnable);
                Date d = new Date();
                if (BluetoothConstant.mConnected) {

                    if (BluetoothConstant.mBluetoothLeService != null
                            && BluetoothConstant.mwriteCharacteristic != null) {
                        BluetoothConstant.mBluetoothLeService.startRun(1, d);
                        handler.sendEmptyMessageDelayed(199, 1000);
                        handler.sendEmptyMessage(5);
                        updateTimer = new Timer();
                        updateUiTimerTask = new UpdateUiTimerTask();
                        updateTimer.schedule(updateUiTimerTask, 0,
                                ACQ_TASK_TIMER_PERIOD);
                        bakjldTimes = System.currentTimeMillis();
                    }
                    // startMarker = aMap.addMarker(new MarkerOptions().icon(
                    // BitmapDescriptorFactory
                    // .fromResource(R.mipmap.start_map)).anchor(
                    // (float) 0.5, (float) 1));
                    // startMarker.setPosition(new LatLng(latitude, longitude));
                    // mins = 0;

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
                keenBrace = new KeenBrace();
                keenBrace.setType(1);
                keenBrace.setUserID(Constant.user.getId());
                keenBrace.setStartTime(d.getTime());
                keenBrace.setTimelength(new Long(0L));
                keenBrace.setState(0);
                if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
                    keenBrace.setLatitude(fragmentMap.getLatitude());
                    keenBrace.setLongitude(fragmentMap.getLongitude());
                    keenBrace.setEndlatitude(fragmentMap.getLatitude());
                    keenBrace.setEndlongitude(fragmentMap.getLongitude());
                }else {
                    keenBrace.setLatitude(fragmentMapGoogle.getLatitude());
                    keenBrace.setLongitude(fragmentMapGoogle.getLongitude());
                    keenBrace.setEndlatitude(fragmentMapGoogle.getLatitude());
                    keenBrace.setEndlongitude(fragmentMapGoogle.getLongitude());
                }

                keenBrace.setId(KeenbraceDBHelper.getInstance(this).insertKeenBrace(keenBrace));
                if (fragmentHistory != null)
                    fragmentHistory.updateBleId(keenBrace.getId());
                RunWaring rw = new RunWaring();
                rw.setIndex(0+"");
                rw.setCreateTime(System.currentTimeMillis());

                break;
            case R.id.btn_stop:
                btn_end.setVisibility(View.VISIBLE);
                btn_continue.setVisibility(View.VISIBLE);
                btn_stop.setVisibility(View.GONE);
                break;

            case R.id.btn_bluetooth:
                openDeives();
                break;
            case R.id.btn_back:
                showTips();
                break;
            case R.id.btn_end:
                endRun();
                break;
            case R.id.btn_continue:
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
    int alagn=255;
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 19:
                    mins += 1000;
                    if (steprate < 40) {
                        blue += 1;
                    } else if (steprate <= 190) {
                        yellow += 1;

                    } else if (steprate > 190) {
                        red += 1;
                    }
                    if (mLastFragment == fragmentHistory)
                        fragmentHistory.updateTime(mins / 1000, blue, yellow, red);
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
                        if (rw8 != null) {
                            if (step_history[3] != step_history[0])
                                rw8 = getWarning(8, rw8);
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
                    if (BluetoothConstant.mBluetoothLeService != null
                            && BluetoothConstant.mwriteCharacteristic != null)
                        BluetoothConstant.mBluetoothLeService.startRun(1,
                                new Date());
                    break;
                case 5:
                    if (isStartRun) {
                        int lc_gap = lc - lc_old;
                        int speed = lc_gap / 5;
                        fragmentMap.updateSpeed(speed * 3600);
                        fragmentHistory.updateSpeed(speed * 3600);
                        lc_old = lc;
                        handler.sendEmptyMessageDelayed(5, 5000);
                        com.umeng.socialize.utils.Log.e("speed-------------------"
                                + speed);
                    }
                    break;
                case 6:

                    fragmentReport.addLineEntry(power_valid);
                    fragmentReport.addBarEntry(osc, (int) ya, steprate);
                    //
                    if (updateStep != step_true) {

                        if (mLastFragment == fragmentHistory)
                            fragmentHistory.updateView(wrCount, steprate, stride,
                                    bkstep, mins, lc);
                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateView(wrCount, steprate, stride,
                                    bkstep, mins, lc);
                    } else {

                        if (mLastFragment == fragmentHistory)
                            fragmentHistory.updateView(wrCount, steprate, stride,
                                    step_true, mins, lc);
                        if (mLastFragment == fragmentMap)
                            fragmentMap.updateView(wrCount, steprate, stride,
                                    step_true, mins, lc);
                    }
                    break;
                case 7:
                    fragmentReport.addJldBarEntry(jldTimes);
                    break;
                case 8:
                    ly_tab.setVisibility(View.GONE);
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
                    BluetoothConstant.mBluetoothLeService.startRun(1, d);
                }
                handler.sendEmptyMessage(2);
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                data = intent
                        .getByteArrayExtra(BluetoothLeService.EXTRA_DATA_BYTE);

                if (shi == 0 && isAdd) {
                    isAdd = false;
                    jldTimes = (int) (System.currentTimeMillis() - bakjldTimes);
                    handler.sendEmptyMessage(7);
                }
                if (!isAdd & shi == 100) {
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
                    updateView();
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
        intentFilter.addAction(FragmentMessage.SHOW_MODEL);
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
    RunWaring rw1, rw2, rw3, rw4, rw5, rw6, rw7, rw8, rw9, rw10, rw11;
    int step, stride, lc, index, steprate, osc, ya, shi;
    int[] bs = new int[5];
    List<RunWaring> rws = new ArrayList<RunWaring>();
    int count;
    int jzdfbOne, jzdfbTwo;
    int second; //
    int[] step_history = new int[4]; //
    int[] power_record = new int[200];
    int power_index;
    boolean power_flag = false;
    int power_valid = 0;

    boolean iangle_flag = false;

    int step_true;
    int old_step = 0;
    int updateStep = 0;
    int bkstep;
    float PI = 3.1415926f;

    int old_steprate;

    void updateView() {
        step = ByteHelp.ByteArrayToShort(new byte[]{data[9], data[10]});// ����

        int z = ByteHelp.ByteArrayToShort(new byte[]{data[1], data[2]});
        int h = ByteHelp.ByteArrayToShort(new byte[]{data[3], data[4]});
        ya = (int) Math.sqrt(z * z + h * h);

        int xuzhuan = ByteHelp.byteArrayToInt(new byte[]{data[5], data[6]});// ��ת��ֵ
        int fanzhuan = ByteHelp.byteArrayToInt(new byte[]{data[7], data[8]});// ��ת��ֵ

        int iangle = ByteHelp.byteArrayToInt(new byte[]{data[11], data[12]});// ϥ�Ǽн�
        // lj

        osc = ByteHelp.byteArrayToInt(new byte[]{data[13], data[14]}); // ��ֱ��ظ߶�
        // lj

        int power = data[17] & 0xFF;//
        stride = data[16] & 0xFF;// /

        int jzdfb = data[18] & 0xFF;//


        float Height = 175;
        if (UtilConstants.user != null
                && !"".equals(UtilConstants.user.getHeight()))
            Height = UtilConstants.user.getHeight();
        float legLen = 0.0f; // �ȳ�

        if (UtilConstants.user != null
                && "0".equals(UtilConstants.user.getSex())) {
            legLen = (float) (Height / (4.57 + (Height - 180) * 0.01625) + (Height - 5) / 3.74);
        } else { // ���е�
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


        if (stride > 110 && steprate > 100) {
            if (step_history[3] != step_history[0]) {
                rw3 = getWarning(3, rw3);
            }
        }


        if (step_history[3] != step_history[0]) {

            if (shi == 100) {
                count++;


                if (count >= 5 && steprate > 100)
                    rw11 = getWarning(11, rw11);


                if (iangle > 130 && steprate > 100) {
                    rw7 = getWarning(7, rw7);

                    iangle_flag = true;
                }


                if (ya > UtilConstants.Weight * 9.8f * 15) {
                    if (steprate > 100)
                        rw1 = getWarning(1, rw1);
                }


                if (xuzhuan > 80 && fanzhuan < -50) {
                    if (steprate > 100)
                        rw4 = getWarning(4, rw4);
                }


                if (xuzhuan < -80 && fanzhuan > 50) {
                    if (steprate > 100)
                        rw5 = getWarning(5, rw5);
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
                        if (steprate > 100)
                            rw6 = getWarning(6, rw6);
                    }
                }
            }
        }


        if (osc > 15) {
            if (steprate > 100)
                rw10 = getWarning(10, rw10);
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
            lc += distance;
        } else {
            if (updateStep != step_true) {
                lc += stride_dis;
            }
        }

        updateStep = step_true;
        old_step = step;
        handler.sendEmptyMessage(6);
    }

    // void updateMessageIco(int type) {
    // switch (type) {
    // case 0:
    // iv_message.setBackgroundResource(R.mipmap.bottom_message_g);
    // break;
    // case 1:
    // iv_message.setBackgroundResource(R.mipmap.bottom_message_y);
    // break;
    // case 2:
    // iv_message.setBackgroundResource(R.mipmap.bottom_message_r);
    // case 3:
    // iv_message.setBackgroundResource(R.mipmap.bottom_message_r);
    // }
    // }

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
                rw.setRunId(keenBrace.getId());
                KeenbraceDBHelper.getInstance(this).insertRunWaring(rw);
                wrCount++;
                // iv_messagecount.setVisibility(View.VISIBLE);
                int type = UtilConstants.WaringMap.get(index + "").getGrade();
                tts.speak(UtilConstants.WaringMap.get("" + index).getFunction(),
                        TextToSpeech.QUEUE_FLUSH, null);
                Intent intent = new Intent();
                intent.putExtra("modelIndex", index);
                intent.putExtra("isClose", 1);
                intent.setClass(MainMenuActivity.this, ModelAcitvity.class);
                startActivity(intent);
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
            rw.setRunId(keenBrace.getId());
            KeenbraceDBHelper.getInstance(this).insertRunWaring(rw);
            tts.speak(UtilConstants.WaringMap.get("" + index).getFunction(),
                    TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent();
            intent.putExtra("modelIndex", index);
            intent.putExtra("isClose", 1);

            intent.setClass(MainMenuActivity.this, ModelAcitvity.class);
            startActivity(intent);
            return rw;
        }
        //

    }

    public void continueRun() {
        btn_end.setVisibility(View.GONE);
        btn_stop.setVisibility(View.VISIBLE);
        btn_continue.setVisibility(View.GONE);
    }

    public void endRun() {

        isStartRun = false;
        handler.removeCallbacks(runnable);

        if (BluetoothConstant.mBluetoothLeService != null
                && BluetoothConstant.mwriteCharacteristic != null)
            BluetoothConstant.mBluetoothLeService.startRun(0, new Date());
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
        keenBrace.setTimelength(mins);
        keenBrace.setSumscore(80);
        // ble.setCadence((int) bpChart.getAverage());
        // ble.setStride((int) bfChart.getAverage());
        keenBrace.setMileage(lc);
        keenBrace.setFileName(file.getAbsolutePath());
        keenBrace.setEndTime(bak);
        if(UtilConstants.MapType==UtilConstants.MAP_GAODE) {
            keenBrace.setLatLngs(fragmentMap.getMap());

            keenBrace.setEndlatitude(fragmentMap.getLatitude());
            keenBrace.setEndlongitude(fragmentMap.getLongitude());
        }else
        {
            keenBrace.setLatLngs(fragmentMapGoogle.getMaps());

            keenBrace.setEndlatitude(fragmentMapGoogle.getLatitude());
            keenBrace.setEndlongitude(fragmentMapGoogle.getLongitude());
        }
        keenBrace.setSumwarings(wrCount);
        KeenbraceDBHelper.getInstance(this).updateKeenBrace(keenBrace);
        // if (fragmentReport == null)
        // fragmentReport = new FragmentReport();
        // changeFragment(this, fragmentReport, null, null);

        btn_end.setVisibility(View.GONE);
        btn_stop.setVisibility(View.GONE);
        btn_continue.setVisibility(View.GONE);
        btn_start.setVisibility(View.VISIBLE);

        TransferObserver observer = transferUtility.upload(
                UtilConstants.BUCKET_NAME, file.getName(), file);





        Intent intent = new Intent();
        intent.putExtra("bleData", keenBrace);
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
        Button cancle = (Button) window.findViewById(R.id.cancle);
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
                                    btn_stop.setVisibility(View.GONE);
                                    btn_start.setVisibility(View.VISIBLE);
                                    if (BluetoothConstant.mBluetoothLeService != null
                                            && BluetoothConstant.mwriteCharacteristic != null)
                                        BluetoothConstant.mBluetoothLeService
                                                .startRun(0, new Date());
                                    isStartRun = false;
                                    try {
                                        fos.close();
                                        fos = null;
                                    } catch (IOException e) {
                                    }
                                    keenBrace.setTimelength(mins);
                                    keenBrace.setSumscore(80);
                                    // ble.setCadence((int)
                                    // bpChart.getAverage());
                                    // ble.setStride((int)
                                    // bfChart.getAverage());
                                    keenBrace.setMileage(lc);
                                    keenBrace.setFileName(file.getAbsolutePath());
                                    keenBrace.setEndTime(bak);
                                    if(UtilConstants.MapType==UtilConstants.MAP_GAODE)
                                        keenBrace.setLatLngs(fragmentMap.getMap());
                                    else
                                        keenBrace.setLatLngs(fragmentMapGoogle.getMaps());
                                    keenBrace.setSumwarings(wrCount);
                                    KeenbraceDBHelper.getInstance(MainMenuActivity.this).updateKeenBrace(keenBrace);
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

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            // 刚按下
            case MotionEvent.ACTION_DOWN:
                if (View.GONE == ly_tab.getVisibility()) {

                    ly_tab.setVisibility(View.VISIBLE);

                    ly_tab.startAnimation(
                            AnimationUtils.loadAnimation(this, R.anim.pop_enter_anim));
                    handler.sendEmptyMessageDelayed(1, 5000);
                } else {

                    ly_tab.setVisibility(View.GONE);

                    ly_tab.startAnimation(
                            AnimationUtils.loadAnimation(this, R.anim.pop_exit_anim));
                }
                break;
            // 移动
            case MotionEvent.ACTION_MOVE:
                return true;
            // 离开
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_OUTSIDE:

                break;
        }


        return false;
    }

}
