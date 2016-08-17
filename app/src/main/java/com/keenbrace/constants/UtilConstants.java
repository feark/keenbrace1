package com.keenbrace.constants;

import android.bluetooth.BluetoothDevice;

import java.util.HashMap;
import java.util.Map;

import com.keenbrace.greendao.ShortPlan;
import com.keenbrace.greendao.SportsStructure;
import com.keenbrace.greendao.User;

public class UtilConstants {

    public static User user;

    //这个是构造类似结构体数组的方法 ken
    //第一个值是索引 第二个是结构体的值
    public static HashMap<String, SportsStructure> SportsContent = new HashMap<String, SportsStructure>();

    //不同的单次计划
    public static HashMap<String, ShortPlan> ShortPlanContent = new HashMap<String, ShortPlan>();

    //public static HashMap<String, BluetoothDevice> DeviceList = new HashMap<String, BluetoothDevice>();
    //public static int DeviceNumber = 0;

    /*
     * You should replace these values with your own. See the README for details
     * on what to fill in.
     * ap-northeast-1:713c5b04-46e5-4241-8d5b-2ed673d9aae7
     */
    public static final String COGNITO_POOL_ID = "ap-northeast-1:a3c2b68e-6dd4-4414-b146-d0410dca4201";
    public static final int MAP_GOOGLE=0;
    public static final int MAP_GAODE=1;

    /*
     * Note, you must first create a bucket using the S3 console before running
     * the sample (https://console.aws.amazon.com/s3/). After creating a bucket,
     * put it's name in the field below.
     */
    public static final String BUCKET_NAME = "keenbrace";
    public static int Weight = 65;
    public static int Height = 175;
    public static int MapType=1;        //这种可以直接设置值
    public static final String SHARE_PREF = "keenbrace_share.pref";
    public static final String KEY_HAS_LOGIN = "has_login";
    public static final String KEY_ACCOUNT  ="key_account";
    public static final String BLE_NAME = "ble_name";   //上一次连接的设备名称

    public static final String KEY_HAS_SHORTPLAN = "has_shortplan"; //是否有单项计划
    public static final String KEY_TRAIN_TODAY  = "train_today"; //今天的单项计划是否已经完成

    public static final String KEY_HAS_LONGPLAN = "has_longplan"; //是否有长期计划
    public static final String KEY_LONGPLAN_FINISH  = "long_plan_finish"; //长期计划是否已经完成

    //历史记录的数据查询名称同user一样用登录名

    //不同的运动种类
    public static final int sport_running = 0;
    public static final int sport_squat = 3;
    public static final int sport_pushup = 4;
    public static final int sport_pullup = 5;
    public static final int sport_dumbbell = 6;
    public static final int sport_plank = 9;
    public static final int sport_bicyclesitup = 10;
    public static final int sport_closestancesquat = 11;



}
