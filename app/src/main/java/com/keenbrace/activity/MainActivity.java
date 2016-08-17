package com.keenbrace.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.keenbrace.R;
import com.keenbrace.adapter.LeDeviceListAdapter;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.core.base.KeenbraceApplication;
import com.keenbrace.core.slidemenu.lib.SlidingMenu;
import com.keenbrace.core.slidemenu.lib.app.SlidingFragmentActivity;
import com.keenbrace.core.utils.PreferenceHelper;
import com.keenbrace.core.utils.WLoger;
import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.fragment.LeftFragment;
import com.keenbrace.fragment.MainFragment;
import com.keenbrace.services.BluetoothConstant;
import com.keenbrace.services.BluetoothLeService;

import java.util.Date;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends SlidingFragmentActivity {

    private long firstTime = 0;
    private Fragment mContent;
    private SlidingMenu sm;
    byte[] data;

    Fragment leftFragment;
    KeenbraceApplication application;

    ProgressDialog progressDialog;
    //LeDeviceListAdapter leDeviceListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        initSlidingMenu(savedInstanceState);

        leDeviceListAdapter = new LeDeviceListAdapter(this);

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        //切换到选择运动的activity
        switchConent(new MainFragment());

        application = (KeenbraceApplication) getApplication();

        progressDialog = new ProgressDialog(this, R.style.dialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("");

        // mypDialog.setIcon(R.mipmap.w);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
    }

    public void reConnectBLE(){
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        //将连接上的设备再加入列表
        if(BluetoothConstant.mdevice != null){
            leDeviceListAdapter.addDevice(BluetoothConstant.mdevice);
        }

        if (BluetoothConstant.mConnected){
            //如果有连接 先发一个停止运动
            BluetoothConstant.mBluetoothLeService.startRun(0, (byte)0, new Date());
        }

        if (!PreferenceHelper.readBoolean(AppContext.getInstance(), UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_LOGIN)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mGattUpdateReceiver);
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        // 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }

        if (mContent == null) {
            mContent = new  MainFragment();
        }

        // 设置左侧滑动菜单
        setBehindContentView(R.layout.menu_frame_left);
        leftFragment = new LeftFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, leftFragment).commit();

        // 实例化滑动菜单对象
        sm = getSlidingMenu();
        // 设置可以左右滑动的菜单
        sm.setMode(SlidingMenu.LEFT);
        // 设置滑动阴影的宽度
        sm.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单阴影的图像资源
        sm.setShadowDrawable(null);
        // 设置滑动菜单视图的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        sm.setFadeDegree(0.35f);
        // 设置触摸屏幕的模式,这里设置为全屏 关闭手势滑动
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        // 设置下方视图的在滚动时的缩放比例
        sm.setBehindScrollScale(0.0f);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    public void switchConent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        getSlidingMenu().showContent();
    }

    public void showMenu(){
        sm.showMenu(true);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                //如果两次按键时间间隔大于2秒，则不退出
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                //两次按键小于2秒时，退出应用
                moveTaskToBack(false);
                unregisterReceiver(mGattUpdateReceiver);
                finish();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    //--------------------------------------建立蓝牙连接----------------------------------------
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.BLEAPI_GATT_FOUNDDEVICE.equals(action)) {
                BluetoothDevice curBleDevice = (BluetoothDevice) intent
                        .getExtras().get("device");
                String strMacAddress = (String) intent.getExtras().get(
                        "macaddress");

                System.out.println("----------------------> scanAndCheckDevice");
                //扫描并建立连接
                scanAndCheckDevice(curBleDevice, strMacAddress);

            } else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Toast.makeText(MainActivity.this, "device connected",
                        Toast.LENGTH_SHORT).show();

                leDeviceListAdapter.clear();
                leDeviceListAdapter.addDevice(BluetoothConstant.mdevice);

                BluetoothConstant.mConnected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
                    .equals(action)) {
                BluetoothConstant.mConnected = false;

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "device disconnected",
                        Toast.LENGTH_SHORT).show();

                leDeviceListAdapter.clear();
                leDeviceListAdapter.addDevice(BluetoothConstant.mdevice);

                BluetoothConstant.mBluetoothLeService.close();
                BluetoothConstant.mConnected = false;
                BluetoothConstant.mdevice = null;
                //handler.sendEmptyMessage(1);

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "services disovered",
                        Toast.LENGTH_SHORT).show();

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //在这个位置接收蓝牙数据 ken
                data = intent
                        .getByteArrayExtra(BluetoothLeService.EXTRA_DATA_BYTE);

                //将数据给到全局变量
                application.setBleData(data);

            }
        }
    };

    //运行这个函数的时候会调用BluetoothLeService.java中的回调函数LeScanCallback,并根据设备名称建立连接
    public void scanAndCheckDevice(BluetoothDevice device, String strMacAddress) {
        if (null == device || null == device.getAddress()) {

            return;
        }

        leDeviceListAdapter.addDevice(device);

        //调试信息的打印方法 ken
        System.out.println("----------------------> has ble name");

        //连接设备
        if (BluetoothConstant.mdevice == null) {
            BluetoothConstant.mdevice = device;

            final boolean result = BluetoothConstant.mBluetoothLeService
                    .connect(device.getAddress());
            progressDialog.setMessage("connecting");
            progressDialog.show();
        } else {
            if (!BluetoothConstant.mConnected) {
                BluetoothConstant.mBluetoothLeService.connect(device
                        .getAddress());
                progressDialog.setMessage("connecting");
                progressDialog.show();
            }
        }

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

    AlertDialog popupWindow;
    LeDeviceListAdapter leDeviceListAdapter;

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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //按了没反应
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                BluetoothDevice ble = leDeviceListAdapter.getDevice(arg2);

                //问题出在BluetoothConstant.mdevice == null
                if (BluetoothConstant.mdevice != null) {
                    //如果已经连接了 先断掉
                    if (BluetoothConstant.mConnected) {
                        BluetoothConstant.mBluetoothLeService.close();
                    }
                    Toast.makeText(MainActivity.this, "connecting " + ble.getName(),
                            Toast.LENGTH_SHORT).show();

                    //将点选的设备名保存进配置文件
                    PreferenceHelper.write(AppContext.getInstance(), UtilConstants.SHARE_PREF, UtilConstants.BLE_NAME, ble.getName());
                    com.umeng.socialize.utils.Log.e("-------------------connect" + ble.getName());
                    BluetoothConstant.mdevice = ble;
                    final boolean result = BluetoothConstant.mBluetoothLeService
                            .connect(ble.getAddress());
                    //progressDialog.setMessage("connecting...");
                    //progressDialog.show();
                }
            }
        });
        Button cancel = (Button) window.findViewById(R.id.close);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.show();
    }


}
